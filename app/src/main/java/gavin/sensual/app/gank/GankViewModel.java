package gavin.sensual.app.gank;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.douban.Image;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.FragGankBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/5/8
 */
public class GankViewModel extends BindingViewModel<FragGankBinding> {

    private WeakReference<Context> mContext;
    private Callback callback;

    private List<Image> imageList = new ArrayList<>();
    private GankAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    GankViewModel(Context context, FragGankBinding binding, Callback callback) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.callback = callback;
        init();
    }

    private void init() {
        binding.toolbar.setTitle("干货集中营");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new GankAdapter(mContext.get(), imageList);
        adapter.setOnItemClickListener(i -> callback.onItemClick(imageList, i));
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);
    }

    void doOnSubscribe(boolean isMore) {
        if (isMore) {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");
        } else {
            binding.refreshLayout.setRefreshing(true);
        }
    }

    void doOnError(boolean isMore) {
        if (isMore) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");
        } else {
            binding.refreshLayout.setRefreshing(false);
        }
    }

    void doOnComplete() {
        binding.refreshLayout.setRefreshing(false);
        loadingBinding.progressBar.setVisibility(View.GONE);
        loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
    }

    void onNext(boolean isMore, List<Image> list) {
        if (!isMore && imageList.isEmpty()) {
            imageList.addAll(list);
            adapter.notifyDataSetChanged();
            return;
        }
        List<Image> newList = new ArrayList<>();
        if (isMore) newList.addAll(imageList);
        newList.addAll(list);
        Observable.just(newList)
                .map(stories -> DiffUtil.calculateDiff(new DiffCallback(imageList, stories)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                // 使用 DiffUtil 刷新数据时 adapter 数据列表在 dispatchUpdatesTo 后更新有可能会报 IndexOutOfBoundsException
                // 将 adapter 更新数据放在 dispatchUpdatesTo 前面，待跟进
                .doOnNext(diffResult -> {
                    if (!isMore) imageList.clear();
                    imageList.addAll(list);
                })
                .doOnComplete(() -> {
                    binding.refreshLayout.setRefreshing(false);
                    loadingBinding.progressBar.setVisibility(View.GONE);
                    loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
                })
                .subscribe(diffResult -> diffResult.dispatchUpdatesTo(adapter));
    }

    void onError(Throwable e, boolean isMore) {
        if (isMore) {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    void onDestroy() {
        compositeDisposable.dispose();
    }

    interface Callback {
        void onItemClick(List<Image> imageList, int position);
    }

}
