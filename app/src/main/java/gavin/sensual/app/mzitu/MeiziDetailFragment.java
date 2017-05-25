package gavin.sensual.app.mzitu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import gavin.sensual.R;
import gavin.sensual.app.common.BigImagePopEvent;
import gavin.sensual.app.common.ToolbarRecyclerViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/15
 */
public class MeiziDetailFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ToolbarRecyclerViewModel mViewModel;

    public static MeiziDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, url);
        MeiziDetailFragment fragment = new MeiziDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ToolbarRecyclerViewModel(_mActivity, this, binding);

        String url = getArguments().getString(BundleKey.PAGE_TYPE);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String title = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".") - 2);
        binding.includeToolbar.toolbar.setTitle(TextUtils.isEmpty(title) ? "妹子图" : title);
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false, url));

        subscribeEvent();

        getImage(false, url);
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
        RxBus.get().toObservable(BigImagePopEvent.class)
                .doOnSubscribe(compositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.requestCode != hashCode()) return;
                    binding.recycler.smoothScrollToPosition(event.position);
                });
    }

    private void getImage(boolean isMore, String url) {
        mViewModel.getImage(getDataLayer().getMeiziPicService().getImageRange(this, url), isMore);
    }
}
