package gavin.sensual.app.daily;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingViewModel;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.FragDailyBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 日报 ViewModel
 *
 * @author gavin.xiong 2017/5/5
 */
public class DailyViewModel extends BindingViewModel<FragDailyBinding> {

    private WeakReference<Context> mContext;
    private Callback callback;

    private List<Daily.Story> storyList = new ArrayList<>();
    private DailyAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    DailyViewModel(Context context, FragDailyBinding binding, Callback callback) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        this.callback = callback;
        init();
    }

    private void init() {
        binding.toolbar.setTitle("知乎日报");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new DailyAdapter(mContext.get(), storyList);
        adapter.setOnItemClickListener(i -> callback.onItemClick(storyList.get(i)));
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);
    }

    void doOnSubscribe(int dayDiff) {
        if (dayDiff == 0) {
            binding.refreshLayout.setRefreshing(true);
        } else {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");
        }
    }

    void doOnNext(int dayDiff, Daily daily) {
        daily.getStories().get(0).setDate(dayDiff == 0 ? "今日热文" : daily.getDate());
        if (dayDiff == 0) {
            initBanner(daily.getTopStories());
        }
    }

    void doOnComplete() {
        binding.refreshLayout.setRefreshing(false);
        loadingBinding.progressBar.setVisibility(View.GONE);
        loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
    }

    void doOnError() {
        binding.refreshLayout.setRefreshing(false);
    }

    void onNext(int dayDiff, Daily daily) {
        if (dayDiff == 0 && storyList.isEmpty()) {
            storyList.addAll(daily.getStories());
            adapter.notifyDataSetChanged();
            return;
        }
        List<Daily.Story> newList = new ArrayList<>();
        if (dayDiff != 0) newList.addAll(storyList);
        newList.addAll(daily.getStories());
        Observable.just(newList)
                .map(stories -> DiffUtil.calculateDiff(new DiffCallback(storyList, stories)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .doOnNext(diffResult -> {
                    if (dayDiff == 0) storyList.clear();
                    storyList.addAll(daily.getStories());
                })
                .subscribe(diffResult -> diffResult.dispatchUpdatesTo(adapter));
    }

    void onError(Throwable e, int dayDiff) {
        if (dayDiff != 0) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initBanner(List<Daily.Story> topStoryList) {
        List<String> urlList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (Daily.Story story : topStoryList) {
            urlList.add(story.getImageUrl());
            titleList.add(story.getTitle());
        }
        binding.banner.setUrlList(urlList, titleList);
        binding.banner.setOnItemClickListener(i -> callback.onBannerItemClick(topStoryList.get(i)));
    }

    void onDestroy() {
        compositeDisposable.dispose();
    }

    interface Callback {
        void onItemClick(Daily.Story story);

        void onBannerItemClick(Daily.Story story);
    }
}
