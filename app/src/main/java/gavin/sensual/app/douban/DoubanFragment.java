package gavin.sensual.app.douban;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import gavin.sensual.R;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.LoadMoreEvent;
import gavin.sensual.app.common.RecyclerViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutRecyclerBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 豆瓣图片列表页
 *
 * @author gavin.xiong 2017/5/9
 */
public class DoubanFragment extends BindingFragment<LayoutRecyclerBinding> {

    private String cid;

    private RecyclerViewModel mViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static DoubanFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKey.PAGE_TYPE, type);
        DoubanFragment fragment = new DoubanFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
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

    private void init() {
        cid = getArguments().getString(BundleKey.PAGE_TYPE);

        mViewModel = new RecyclerViewModel(_mActivity, this, binding);

        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));
    }

    private Observable<Image> getResult(boolean isMore) {
        if (TextUtils.isEmpty(cid)) {
            return getDataLayer().getDoubanService().getRank(this, isMore ? binding.recycler.offset + 1 : 1);
        } else {
            return getDataLayer().getDoubanService().getShow(this, cid, isMore ? binding.recycler.offset + 1 : 1);
        }
    }

    private void getImage(boolean isMore) {
        mViewModel.getImage(getResult(isMore), isMore);
    }
}
