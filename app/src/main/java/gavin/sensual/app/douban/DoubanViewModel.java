package gavin.sensual.app.douban;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.base.Image;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.FragDoubanBinding;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 豆瓣妹子 ViewModel
 *
 * @author gavin.xiong 2017/5/8
 */
public class DoubanViewModel extends BindingViewModel<FragDoubanBinding> {

    private WeakReference<Context> mContext;
    private Callback callback;

    private List<Image> imageList = new ArrayList<>();
    private DoubanAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    DoubanViewModel(Context context, FragDoubanBinding binding, Callback callback) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.callback = callback;
        init();
    }

    private void init() {
        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new DoubanAdapter(mContext.get(), imageList);
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

    void onNext(Image image) {
        imageList.add(image);
        adapter.notifyItemInserted(imageList.size() - 1);
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
