package gavin.sensual.app.capture.jiandan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.LoadMoreEvent;
import gavin.sensual.app.common.ToolbarRecyclerViewModel;
import gavin.sensual.app.setting.AssetsUtils;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/11
 */
public class JiandanTopFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    private String[] images;

    public static JiandanTopFragment newInstance(long question) {
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.PAGE_TYPE, question);
        JiandanTopFragment fragment = new JiandanTopFragment();
        fragment.setArguments(bundle);
        return fragment;
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

        binding.includeToolbar.toolbar.setTitle("煎蛋妹子精选");
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));
        binding.recycler.limit = 10;

        Observable.just(getArguments().getLong(BundleKey.PAGE_TYPE))
                .doOnSubscribe(compositeDisposable::add)
                .map(type -> type == 2017 ? "jiandantop2017.img" : "jiandantop2016.img")
                .map(s -> AssetsUtils.readText(_mActivity, s))
                .map(s -> s.split(","))
                .subscribe(strings -> this.images = strings,
                        e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show());
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
        mViewModel.getImage(getImage(isMore ? binding.recycler.offset : 0), isMore);
    }

    private Observable<Image> getImage(int offset) {
        return Observable.just(offset)
                .delay(1, TimeUnit.SECONDS)
                .map(offset1 -> offset1 * binding.recycler.limit)
                .filter(srcPos -> images != null && srcPos < images.length)
                .map(srcPos -> {
                    int countEnable = images.length - srcPos;
                    binding.recycler.haveMore = countEnable > binding.recycler.limit;
                    int length = binding.recycler.haveMore ? binding.recycler.limit : countEnable - srcPos;
                    String[] temp = new String[length];
                    System.arraycopy(images, srcPos, temp, 0, length);
                    return temp;
                })
                .flatMap(Observable::fromArray)
                .map(s -> Image.newImage(this, s));
    }
}
