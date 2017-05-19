package gavin.sensual.app.gank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.base.BigImageViewModel;
import gavin.sensual.app.base.SaveImageEvent;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToobleRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class GankFragment extends BindingFragment<LayoutToobleRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BigImageViewModel mViewModel;

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tooble_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new BigImageViewModel(_mActivity, this, binding);
//        binding.setViewModel(mViewModel);
        binding.includeToolbar.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));

        getImage(false);
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

    private void getImage(boolean isMore) {
        getDataLayer().getGankService().getImage(this, binding.recycler.limit, isMore ? binding.recycler.offset + 1 : 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loading = false;
                    binding.recycler.offset--;
                })
                .subscribe(image -> {
                    binding.recycler.haveMore = true;
                    mViewModel.onNext(isMore, image);
                }, e -> mViewModel.onError(e, isMore));
    }

}
