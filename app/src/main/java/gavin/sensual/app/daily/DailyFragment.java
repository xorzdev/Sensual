package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDailyBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding>
        implements AutoLoadRecyclerView.OnLoadListener, DailyViewModel.Callback {

    private DailyViewModel mViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getDaily(0);
    }

    @Override
    public void onLoad() {
        getDaily(binding.recycler.pageNo);
    }

    @Override
    public void onItemClick(Daily.Story story) {
        start(NewsFragment.newInstance(story.getId()));
    }

    @Override
    public void onBannerItemClick(Daily.Story story) {
        start(NewsFragment.newInstance(story.getId()));
    }

    private void init() {
        mViewModel = new DailyViewModel(_mActivity, binding, this);
        binding.setViewModel(mViewModel);

        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getDaily(0));
        binding.recycler.setOnLoadListener(this);
    }

    private void getDaily(int dayDiff) {
        getDataLayer().getDailyService().getDaily(dayDiff)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(dayDiff);
                    binding.recycler.loadData(dayDiff != 0);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(daily -> {
                    mViewModel.doOnNext(dayDiff, daily);
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    binding.recycler.haveMore = !autoLoadMore(dayDiff, daily) && Integer.parseInt(daily.getDate()) > 20130519;
                })
                .doOnComplete(() -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loadingMore = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError();
                    binding.recycler.loadingMore = false;
                    binding.recycler.pageNo--;
                })
                .doAfterNext(daily -> {
                    if (autoLoadMore(dayDiff, daily))
                        onLoad();
                })
                .subscribe(daily -> mViewModel.onNext(dayDiff, daily),
                        e -> mViewModel.onError(e, dayDiff));
    }

    /**
     * 满足什么条件时自动加载下一页
     * 解决今日热文过少时下拉刷新后上拉加载更多失效问题
     *
     * @param dayDiff dayDiff
     * @param daily   Daily
     * @return boolean
     */
    private boolean autoLoadMore(int dayDiff, Daily daily) {
        return dayDiff == 0 && daily.getStories().size() < 10;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
        compositeDisposable.dispose();
    }
}
