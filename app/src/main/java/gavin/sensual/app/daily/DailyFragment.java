package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.FragDailyBinding;
import gavin.sensual.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding> implements AutoLoadRecyclerView.OnLoadListener {

    private List<Daily.Story> storyList = new ArrayList<>();
    private DailyAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getDaily(0);
    }

    @Override
    public void onLoad() {
        getDaily(binding.recycler.pageNo);
    }

    private void init() {
        binding.toolbar.setTitle("知乎日报");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);
        binding.refreshLayout.setOnRefreshListener(() -> getDaily(0));

        binding.recycler.setOnLoadListener(this);
        adapter = new DailyAdapter(_mActivity, storyList);
        adapter.setOnItemClickListener(i -> start(NewsFragment.newInstance(storyList.get(i).getId())));
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(_mActivity));
        adapter.setFooterBinding(loadingBinding);
    }

    private void getDaily(int dayDiff) {
        getDataLayer().getDailyService().getDaily(dayDiff)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (dayDiff == 0) {
                        binding.refreshLayout.setRefreshing(true);
                    } else {
                        loadingBinding.root.setVisibility(View.VISIBLE);
                        loadingBinding.progressBar.setVisibility(View.VISIBLE);
                        loadingBinding.textView.setText("加载中...");
                    }
                    binding.recycler.loadData(dayDiff != 0);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(daily -> {
                    daily.getStories().get(0).setDate(dayDiff == 0 ? "今日热文" : daily.getDate());
                    return daily;
                })
                .doOnNext(daily -> {
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    binding.recycler.haveMore = Integer.parseInt(daily.getDate()) > 20130519;
                    if (dayDiff == 0) {
                        initBanner(daily.getTopStories());
                    }
                })
                .doOnComplete(() -> {
                    binding.refreshLayout.setRefreshing(false);
                    binding.recycler.loadingMore = false;
                    loadingBinding.progressBar.setVisibility(View.GONE);
                    loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
                })
                .doOnError(throwable -> {
                    binding.refreshLayout.setRefreshing(false);
                    binding.recycler.loadingMore = false;
                    binding.recycler.pageNo--;
                })
                .subscribe(daily -> {
                    List<Daily.Story> newList = new ArrayList<>();
                    if (dayDiff != 0) {
                        newList.addAll(storyList);
                    }
                    newList.addAll(daily.getStories());
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(storyList, newList));
                    diffResult.dispatchUpdatesTo(adapter);
                    if (dayDiff == 0) {
                        storyList.clear();
                    }
                    storyList.addAll(daily.getStories());
                }, e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show());
    }

    private void initBanner(List<Daily.Story> topStoryList) {
        List<String> urlList = topStoryList.stream()
                .map(Daily.Story::getImageUrl)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        List<String> titleList = topStoryList.stream()
                .map(Daily.Story::getTitle)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        binding.banner.setUrlList(urlList, titleList);
        binding.banner.setOnItemClickListener(i -> start(NewsFragment.newInstance(topStoryList.get(i).getId())));
    }
}
