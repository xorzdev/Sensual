package gavin.sensual.app.mzitu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import gavin.sensual.R;
import gavin.sensual.app.base.BigImageViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutToobleRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/15
 */
public class MeiziDetailFragment extends BindingFragment<LayoutToobleRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BigImageViewModel mViewModel;

    public static MeiziDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, url);
        MeiziDetailFragment fragment = new MeiziDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tooble_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new BigImageViewModel(_mActivity, this, binding);
//        binding.setViewModel(mViewModel);
        String url = getArguments().getString(BundleKey.PAGE_TYPE);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String title = url.substring(url.lastIndexOf("/") + 1, url.length() - 6);
        binding.includeToolbar.toolbar.setTitle(title);
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false, url));

        getImage(false, url);
    }

    @Override
    public boolean onBackPressedSupport() {
        return mViewModel.onBackPressedSupport() || super.onBackPressedSupport();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }

    private void getImage(boolean isMore, String url) {
        getDataLayer().getMeiziPicService().getPic2(this, url)
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
