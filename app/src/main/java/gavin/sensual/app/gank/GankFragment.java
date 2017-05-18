package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.douban.Image;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragGankBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/5/6
 */
public class GankFragment extends BindingFragment<FragGankBinding>
        implements AutoLoadRecyclerView.OnLoadListener, GankViewModel.Callback {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GankViewModel mViewModel;

    private SharedPager<Image> sharedPager;

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_gank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getImage(false);
    }

    @Override
    public void onLoad() {
        getImage(true);
    }

    @Override
    public void onItemClick(List<Image> imageList, int position) {
        sharedPager = new SharedPager<>();
        sharedPager.list = imageList;
        sharedPager.limit = binding.recycler.limit;
        sharedPager.no = binding.recycler.offset;
        sharedPager.index = position;
        RxBus.get().post(new StartFragmentEvent(BigImage2.newInstance(sharedPager)));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (sharedPager != null && binding.recycler.getAdapter() != null) {
            binding.recycler.offset = sharedPager.no;
            binding.recycler.getAdapter().notifyDataSetChanged();
            binding.recycler.smoothScrollToPosition(sharedPager.index);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
        compositeDisposable.dispose();
    }

    private void init() {
        mViewModel = new GankViewModel(_mActivity, binding, this);
        binding.setViewModel(mViewModel);

        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.refreshLayout.setOnRefreshListener(() -> getImage(false));
        binding.recycler.setOnLoadListener(this);
    }

    private void getImage(boolean isMore) {
        getDataLayer().getGankService().getImage(this, binding.recycler.limit, isMore ? binding.recycler.offset + 1 : 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(arg0 -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError(isMore);
                    binding.recycler.loading = false;
                    binding.recycler.offset--;
                })
                .subscribe(images -> {
                    binding.recycler.haveMore = binding.recycler.limit == images.size();
                    mViewModel.onNext(isMore, images);
                }, e -> mViewModel.onError(e, isMore));
    }
}
