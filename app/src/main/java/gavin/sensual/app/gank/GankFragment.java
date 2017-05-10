package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
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

    private final int limit = 10;

    private GankViewModel mViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        startForResult(BigImage2.newInstance(sharedPager), 999);
    }

    SharedPager<Welfare> sharedPager;

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            binding.recycler.pageNo = sharedPager.no;
            if (binding.recycler.getAdapter() != null) {
                binding.recycler.getAdapter().notifyDataSetChanged();
                binding.recycler.smoothScrollToPosition(sharedPager.index);
            }
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

        binding.toolbar.setNavigationOnClickListener((v) -> pop());
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
                    binding.recycler.loadingMore = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loadingMore = false;
                    binding.recycler.pageNo--;
                })
                .subscribe(welfareList -> {
                    binding.recycler.haveMore = limit == welfareList.size();
                    mViewModel.onNext(isMore, welfareList);
                }, e -> mViewModel.onError(e, isMore));
    }
}
