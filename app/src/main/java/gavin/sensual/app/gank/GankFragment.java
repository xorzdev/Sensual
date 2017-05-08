package gavin.sensual.app.gank;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragGankBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.Observable;
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

    private GankViewModel mViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        getWelfare(false);
    }

    @Override
    public void onLoad() {
        getWelfare(true);
    }

    @Override
    public void onItemClick(Welfare welfare) {

    }

    private void init() {
        mViewModel = new GankViewModel(_mActivity, binding, this);
        binding.setViewModel(mViewModel);

        binding.toolbar.setNavigationOnClickListener((v) -> pop());
        binding.refreshLayout.setOnRefreshListener(() -> getWelfare(false));
        binding.recycler.setOnLoadListener(this);
    }

    private void getWelfare(boolean isMore) {
        getDataLayer().getGankService().getWelfare(8, isMore ? binding.recycler.pageNo + 1 : 1)
                .throttleFirst(6000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(isMore);
                    binding.recycler.loadData(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> {
//                    mViewModel.doOnNext(isMore, result);
                    fixData(isMore, result.getResults());
                    binding.recycler.haveMore = true;
                })
                .doOnComplete(() -> {
//                    mViewModel.doOnComplete();
//                    binding.recycler.loadingMore = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError();
                    binding.recycler.loadingMore = false;
                    binding.recycler.pageNo--;
                })
                .subscribe(result -> mViewModel.onNext(isMore, result.getResults()),
                        e -> mViewModel.onError(e, isMore));
    }

    private void fixData(boolean isMore, List<Welfare> welfareList) {
        Observable.just(welfareList)
                .flatMap(Observable::fromIterable)
                .map(welfare -> {
                    try {
                        Bitmap bm = getBitmap(welfare);
                        welfare.setWidth(bm.getWidth());
                        welfare.setHeight(bm.getHeight());
                        bm.recycle();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        welfare.setWidth(500);
                        welfare.setHeight(500);
                    }
                    return welfare;
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .doAfterSuccess(arg0 -> binding.recycler.loadingMore = false)
                .doOnError(throwable -> binding.recycler.loadingMore = false)
                .subscribe(list -> mViewModel.onNext(isMore, welfareList), Throwable::printStackTrace);
    }

    private Bitmap getBitmap(Welfare t) throws InterruptedException, ExecutionException {
        return Glide.with(this)
                .load(t.getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
        compositeDisposable.dispose();
    }
}
