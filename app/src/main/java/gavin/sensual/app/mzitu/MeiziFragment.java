package gavin.sensual.app.mzitu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.app.setting.BigImageMultiFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutRecyclerBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/9
 */
public class MeiziFragment extends BindingFragment<LayoutRecyclerBinding>
        implements AutoLoadRecyclerView.OnLoadListener, MeiziViewModel.Callback {

    private String type;

    private MeiziViewModel mViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static MeiziFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKey.PAGE_TYPE, type);
        MeiziFragment fragment = new MeiziFragment();
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
        getData(false);
    }

    @Override
    public void onLoad() {
        getData(true);
    }

    @Override
    public void onItemClick(List<Image> imageList, int position) {
        if ("zipai".equals(type)) {
            // TODO: 2017/5/19
            ArrayList<String> stringList = new ArrayList<>();
            for (Image image : imageList) {
                stringList.add(image.getUrl());
            }
            RxBus.get().post(new StartFragmentEvent(
                    BigImageMultiFragment.newInstance(stringList, position)));
        } else {
            RxBus.get().post(new StartFragmentEvent(
                    MeiziDetailFragment.newInstance(imageList.get(position).getUrl())));
        }
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
        type = getArguments().getString(BundleKey.PAGE_TYPE);

        mViewModel = new MeiziViewModel(_mActivity, binding, this);
//        binding.setViewModel(mViewModel);

        binding.refreshLayout.setOnRefreshListener(() -> getData(false));
        binding.recycler.setOnLoadListener(this);
    }

    private void getData(boolean isMore) {
        getDataLayer().getMeiziPicService().getPic(this, type, isMore ? binding.recycler.offset + 1 : 1)
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
                    mViewModel.onNext(image);
                }, e -> mViewModel.onError(e, isMore));
    }

}
