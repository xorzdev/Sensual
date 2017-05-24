package gavin.sensual.app.capture.jiandan;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
public class JiandanFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private long question;

    private ToolbarRecyclerViewModel mViewModel;

    public static JiandanFragment newInstance(long question) {
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.PAGE_TYPE, question);
        JiandanFragment fragment = new JiandanFragment();
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

        question = getArguments().getLong(BundleKey.PAGE_TYPE);

        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(() -> getImage(true));
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(LoadMoreEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (event.requestCode != hashCode()) return;
                    getImage(true);
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
//        mViewModel.getImage(getDataLayer().getZhihuPicService().getCollectionPic(this, question, isMore ? binding.recycler.offset + 1 : 1), isMore);
        mViewModel.getImage(getImage(), isMore);
    }

    private Observable<Image> getImage() {
        return Observable.just(question)
                .map(type -> String.format("jiandantop%s.img", question))
                .map(s -> AssetsUtils.readText(_mActivity, s))
                .map(s -> s.split(","))
                .flatMap(Observable::fromArray)
                .take(10)
                .map(s -> Image.newImage(this, s));
    }
}
