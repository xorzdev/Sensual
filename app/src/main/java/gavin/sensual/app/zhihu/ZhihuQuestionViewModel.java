package gavin.sensual.app.zhihu;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.douban.DoubanAdapter;
import gavin.sensual.app.douban.Image;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.FragZhihuQuestionBinding;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/5/8
 */
public class ZhihuQuestionViewModel extends BindingViewModel<FragZhihuQuestionBinding> {

    private WeakReference<Context> mContext;
    private Callback callback;

    private List<Image> welfareList = new ArrayList<>();
    private DoubanAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ZhihuQuestionViewModel(Context context, FragZhihuQuestionBinding binding, Callback callback) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.callback = callback;
        init();
    }

    private void init() {
        binding.toolbar.setTitle("知乎看图");
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new DoubanAdapter(mContext.get(), welfareList);
        adapter.setOnItemClickListener(i -> callback.onItemClick(welfareList, i));
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
            welfareList.clear();
            adapter.notifyDataSetChanged();
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
        welfareList.add(image);
        adapter.notifyItemInserted(welfareList.size() - 1);
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
