package gavin.sensual.app.douban;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.app.setting.BigImageMultiFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDoubanBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/9
 */
public class DoubanFragment extends BindingFragment<FragDoubanBinding>
        implements AutoLoadRecyclerView.OnLoadListener, DoubanViewModel.Callback {

    private String cid;

    private DoubanViewModel mViewModel;

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
        return R.layout.frag_douban;
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
        ArrayList<String> stringList = new ArrayList<>();
        for (Image image : imageList) {
            stringList.add(image.getUrl());
        }
        RxBus.get().post(new StartFragmentEvent(
                BigImageMultiFragment.newInstance(stringList, position)));
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
        cid = getArguments().getString(BundleKey.PAGE_TYPE);

        mViewModel = new DoubanViewModel(_mActivity, binding, this);
        binding.setViewModel(mViewModel);

        binding.refreshLayout.setOnRefreshListener(() -> getData(false));
        binding.recycler.setOnLoadListener(this);
    }

    private Observable<Image> getResult(boolean isMore) {
        if (TextUtils.isEmpty(cid)) {
            return getDataLayer().getDoubanService().getRank(this, isMore ? binding.recycler.pageNo + 1 : 1);
        } else {
            return getDataLayer().getDoubanService().getShow(this, cid, isMore ? binding.recycler.pageNo + 1 : 1);
        }
    }

    private void getData(boolean isMore) {
        getResult(isMore)
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
                    binding.recycler.loadingMore = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loadingMore = false;
                    binding.recycler.pageNo--;
                })
                .subscribe(image -> {
                    binding.recycler.haveMore = true;
                    mViewModel.onNext(image);
                }, e -> mViewModel.onError(e, isMore));
    }

}
