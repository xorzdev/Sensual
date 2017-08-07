package gavin.sensual.app.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.LoadMoreEvent;
import gavin.sensual.app.common.ToolbarRecyclerViewModel;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import gavin.sensual.db.util.DbUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 我的收藏
 *
 * @author gavin.xiong 2017/4/25
 */
public class CollectionFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ToolbarRecyclerViewModel(_mActivity, this, binding);

        binding.includeToolbar.toolbar.setTitle("我的收藏");
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
        mViewModel.getImage(
                Observable.just(isMore)
                        .delay(500, TimeUnit.MILLISECONDS)
                        .map(arg0 -> arg0 ? binding.recycler.offset - 1 : 0)
                        .flatMap(offset -> Observable.just(DbUtil.getCollectionService()
                                .queryDesc(offset)))
                        .flatMap(Observable::fromIterable)
                        .map(Collection::getImage)
                        .map(s -> Image.newImage(this, s)),
                isMore);
    }

}
