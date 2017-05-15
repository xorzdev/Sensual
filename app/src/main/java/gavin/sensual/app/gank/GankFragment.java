package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragGankBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/5/6
 */
public class GankFragment extends BindingFragment<FragGankBinding>
        implements AutoLoadRecyclerView.OnLoadListener, GankViewModel.Callback {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final int limit = 10;

    private GankViewModel mViewModel;

    private SharedPager<Welfare> sharedPager;

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_gank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getWelfare(false);
    }

    @Override
    public void onLoad() {
        getWelfare(true);
    }

    @Override
    public void onItemClick(List<Welfare> welfareList, int position) {
        sharedPager = new SharedPager<>();
        sharedPager.list = welfareList;
        sharedPager.limit = limit;
        sharedPager.no = binding.recycler.pageNo;
        sharedPager.index = position;
        RxBus.get().post(new StartFragmentEvent(BigImage2.newInstance(sharedPager)));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (sharedPager != null && binding.recycler.getAdapter() != null) {
            binding.recycler.pageNo = sharedPager.no;
            binding.recycler.getAdapter().notifyDataSetChanged();
            binding.recycler.smoothScrollToPosition(sharedPager.index);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
        compositeDisposable.dispose();
    }

    private void init() {
        mViewModel = new GankViewModel(_mActivity, binding, this);
        binding.setViewModel(mViewModel);

        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getWelfare(false));
        binding.recycler.setOnLoadListener(this);
    }

    private void getWelfare(boolean isMore) {
        getDataLayer().getGankService().getWelfare(this, limit, isMore ? binding.recycler.pageNo + 1 : 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(arg0 -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loading = false;
                    binding.recycler.pageNo--;
                })
                .subscribe(welfareList -> {
                    binding.recycler.haveMore = limit == welfareList.size();
                    mViewModel.onNext(isMore, welfareList);
                }, e -> mViewModel.onError(e, isMore));
    }
}
