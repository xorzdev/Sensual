package gavin.sensual.app.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class TestCommentFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    public static TestCommentFragment newInstance() {
        return new TestCommentFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ToolbarRecyclerViewModel(_mActivity, this, binding);
//        binding.setViewModel(mViewModel);
        binding.includeToolbar.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));

        subscribeEvent();

        getImage(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.onDestroy();
        }
        compositeDisposable.dispose();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(LoadMoreEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (event.requestCode != hashCode()) return;
                    binding.recycler.performLoad();
                });

        RxBus.get().toObservable(BigImagePopEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.requestCode != hashCode()) return;
                    binding.recycler.smoothScrollToPosition(event.position);
                });
    }

    private void getImage(boolean isMore) {
        mViewModel.getImage(getDataLayer().getGankService().getImage(this, binding.recycler.limit, isMore ? binding.recycler.offset + 1 : 1), isMore);
    }

}
