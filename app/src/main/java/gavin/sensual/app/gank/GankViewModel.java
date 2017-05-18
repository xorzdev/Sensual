package gavin.sensual.app.gank;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.base.BigImageAdapter;
import gavin.sensual.app.base.DiffCallback;
import gavin.sensual.app.base.BigImageClickEvent;
import gavin.sensual.app.base.Image;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.LayoutToobleRecyclerBinding;
import gavin.sensual.databinding.RighterLoadingBinding;
import gavin.sensual.util.DisplayUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/5/8
 */
public class GankViewModel extends BindingViewModel<LayoutToobleRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private WeakReference<Context> mContext;
    private WeakReference<Fragment> mFragment;

    private List<Image> imageList = new ArrayList<>();
    private GankAdapter adapter;
    private BigImageAdapter adapter2;
    private PagerSnapHelper snapHelper;
    private FooterLoadingBinding loadingBinding;
    private RighterLoadingBinding loadingBinding2;

    private boolean clearFlag;

    public GankViewModel(Context context, Fragment fragment, LayoutToobleRecyclerBinding binding) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.mFragment = new WeakReference<>(fragment);
        init();
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle("干货集中营");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new GankAdapter(mContext.get(), imageList);
        adapter.setOnItemClickListener(this::change);
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);

        adapter2 = new BigImageAdapter(mContext.get(), mFragment.get(), imageList);
        loadingBinding2 = RighterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter2.setFooterBinding(loadingBinding2);
        snapHelper = new PagerSnapHelper();

        RxBus.get().toObservable(BigImageClickEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (adapter2 == binding.recycler.getAdapter()) {
                        change(event.position);
                    }
                });
    }

    private void change(int position) {
        RecyclerView.LayoutManager layoutManager = binding.recycler.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            binding.recycler.setAdapter(null);
            binding.root.setFitsSystemWindows(true);
            binding.includeToolbar.appBarLayout.setVisibility(View.VISIBLE);
            binding.refreshLayout.setEnabled(true);
            binding.recycler.setPadding(DisplayUtil.dp2px(4), DisplayUtil.dp2px(4), DisplayUtil.dp2px(4), DisplayUtil.dp2px(4));
            binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            snapHelper.attachToRecyclerView(null);
            binding.recycler.setAdapter(adapter);
            binding.recycler.scrollToPosition(position);
            binding.recycler.preCount = 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            binding.recycler.setAdapter(null);
            binding.root.setFitsSystemWindows(false);
            binding.includeToolbar.appBarLayout.setVisibility(View.GONE);
            binding.refreshLayout.setRefreshing(false);
            binding.refreshLayout.setEnabled(false);
            binding.recycler.setPadding(0, 0, 0, 0);
            binding.recycler.setLayoutManager(new LinearLayoutManager(mContext.get(), LinearLayoutManager.HORIZONTAL, false));
            snapHelper.attachToRecyclerView(binding.recycler);
            binding.recycler.setAdapter(adapter2);
            binding.recycler.scrollToPosition(position);
            binding.recycler.preCount = 3;
        }
    }

    public boolean onBackPressedSupport() {
        if (binding.recycler.getAdapter() == adapter2) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.recycler.getLayoutManager());
            change(layoutManager.findFirstVisibleItemPosition());
            return true;
        }
        return false;
    }

    public void doOnSubscribe(boolean isMore) {
        if (isMore) {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");

            loadingBinding2.root.setVisibility(View.VISIBLE);
            loadingBinding2.progressBar.setVisibility(View.VISIBLE);
            loadingBinding2.textView.setText("加载中...");
        } else {
            clearFlag = true;
            binding.refreshLayout.setRefreshing(true);
        }
    }

    public void doOnError(boolean isMore) {
        if (isMore) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");

            loadingBinding2.progressBar.setVisibility(View.GONE);
            loadingBinding2.textView.setText("玩坏了...");
        } else {
            binding.refreshLayout.setRefreshing(false);
        }
    }

    public void doOnComplete() {
        binding.refreshLayout.setRefreshing(false);
        loadingBinding.progressBar.setVisibility(View.GONE);
        loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");

        loadingBinding2.progressBar.setVisibility(View.GONE);
        loadingBinding2.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
    }

    public void onNext(boolean isMore, Image image) {
        if (!isMore && clearFlag) {
            clearFlag = false;
            imageList.clear();
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
        }
        imageList.add(image);
        adapter.notifyItemInserted(imageList.size() - 1);
        // TODO: 2017/5/18 大图刷新时新增在前面
        adapter2.notifyItemInserted(imageList.size() - 1);
    }

    public void onNext(boolean isMore, List<Image> list) {
        if (!isMore && imageList.isEmpty()) {
            imageList.addAll(list);
            adapter.notifyDataSetChanged();
            return;
        }
        List<Image> newList = new ArrayList<>();
        if (isMore) newList.addAll(imageList);
        newList.addAll(list);
        Observable.just(newList)
                .map(stories -> DiffUtil.calculateDiff(new DiffCallback(imageList
                        , stories)))
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

                    loadingBinding2.progressBar.setVisibility(View.GONE);
                    loadingBinding2.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
                })
                .subscribe(diffResult -> {
                    // TODO: 2017/5/18 大图是否有 diff 的必要
                    if (adapter == binding.recycler.getAdapter()) {
                        diffResult.dispatchUpdatesTo(adapter);
                    } else if (adapter2 == binding.recycler.getAdapter()) {
                        adapter2.notifyDataSetChanged();
                    }
                });
    }

    public void onError(Throwable e, boolean isMore) {
        if (isMore) {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void onDestroy() {
        compositeDisposable.dispose();
    }
}
