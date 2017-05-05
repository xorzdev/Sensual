package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDailyTwoBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment2 extends BindingFragment<FragDailyTwoBinding>
        implements AutoLoadRecyclerView.OnLoadListener, DailyViewModel2.Callback {

    private DailyViewModel2 mViewModel;

    public static DailyFragment2 newInstance() {
        return new DailyFragment2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily_two;
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
        NewsFragment.newInstance(story.getId());
    }

    @Override
    public void onBannerItemClick(Daily.Story story) {
        NewsFragment.newInstance(story.getId());
    }

    private void init() {
        mViewModel = new DailyViewModel2(_mActivity, binding, this);
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
                    mViewModel.doOnSubscribe(dayDiff);
                    binding.recycler.loadData(dayDiff != 0);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(daily -> {
                    mViewModel.doOnNext(dayDiff, daily);
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    binding.recycler.haveMore = Integer.parseInt(daily.getDate()) > 20130519;
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
                .subscribe(daily -> mViewModel.onNext(dayDiff, daily), e -> mViewModel.onError(e));
    }
}
