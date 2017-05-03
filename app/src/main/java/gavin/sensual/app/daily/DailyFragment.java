package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDailyBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.widget.LinearItemDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding> {

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
        getDaily();
    }

    private void init() {
        binding.toolbar.setTitle("知乎日报");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.toolbar.inflateMenu(R.menu.action_refresh);
        binding.toolbar.setOnMenuItemClickListener((item) -> {
            getDaily();
            return false;
        });

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);
        binding.refreshLayout.setOnRefreshListener(this::getDaily);
    }

    private void getDaily() {
        getDataLayer().getDailyService().getDaily()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.refreshLayout.setRefreshing(true))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> binding.refreshLayout.setRefreshing(false))
                .doOnError(e -> binding.refreshLayout.setRefreshing(false))
                .subscribe(daily -> {
                    List<String> urlList = daily.getTopStories().stream()
                            .map(Daily.Story::getImageUrl)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                    List<String> titleList = daily.getTopStories().stream()
                            .map(Daily.Story::getTitle)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                    binding.banner.setUrlList(urlList, titleList);
                    binding.banner.setOnItemClickListener(i -> start(NewsFragment.newInstance(daily.getTopStories().get(i).getId())));
                    BindingAdapter adapter = new BindingAdapter<>(_mActivity, daily.getStories(), R.layout.item_daily, BR.item);
                    adapter.setOnItemClickListener(i -> start(NewsFragment.newInstance(daily.getStories().get(i).getId())));
                    binding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
                    binding.recycler.setAdapter(adapter);
                }, e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show());

    }
}
