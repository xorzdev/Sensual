package gavin.sensual.app.capture.maijiaxiu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.LoadMoreEvent;
import gavin.sensual.app.common.ToolbarRecyclerViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 买家秀
 *
 * @author gavin.xiong 2017/5/11
 */
public class MaijiaxiuFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    private Integer pageCount;

    public static MaijiaxiuFragment newInstance() {
        return new MaijiaxiuFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
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

    private void init() {
        mViewModel = new ToolbarRecyclerViewModel(_mActivity, this, binding);

        binding.includeToolbar.toolbar.setTitle("买家秀");
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));
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

    /**
     * 网络请求
     */
    private void getImage(boolean isMore) {
        getDataLayer().getMaijiaxiuService().getPic(this, 0)
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
                    binding.recycler.haveMore = false;
                    mViewModel.onNext(isMore, image);
                }, e -> mViewModel.onError(e, isMore));
    }
}
