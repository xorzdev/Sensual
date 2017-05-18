package gavin.sensual.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestFragBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class TestFragment extends BindingFragment<TestFragBinding>  {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GankViewModel mViewModel;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_frag;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new GankViewModel(_mActivity, this, binding);
//        binding.setViewModel(mViewModel);
        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getData(false));
        binding.recycler.setOnLoadListener(() -> getData(true));

        getData(false);
    }

    @Override
    public boolean onBackPressedSupport() {
        return mViewModel.onBackPressedSupport() || super.onBackPressedSupport();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK && data.getData() != null) {
            RxBus.get().post(new SaveImageEvent(data.getData()));
        }
    }

    private void getData(boolean isMore) {
        getDataLayer().getGankService().getImage(this, binding.recycler.limit, isMore ? binding.recycler.offset + 1 : 1)
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
                    binding.recycler.offset--;
                })
                .subscribe(imageList -> {
                    binding.recycler.haveMore = binding.recycler.limit == imageList.size();
                    mViewModel.onNext(isMore, imageList);
                }, e -> mViewModel.onError(e, isMore));
    }

}
