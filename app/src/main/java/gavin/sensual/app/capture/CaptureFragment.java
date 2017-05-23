package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.app.capture.zhihu.ZhihuFragment;
import gavin.sensual.app.capture.zhihu.ZhihuQuestion;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;

/**
 * 抓图
 *
 * @author gavin.xiong 2017/5/10
 */
public class CaptureFragment extends BindingFragment<LayoutToolbarRecyclerBinding> implements BindingAdapter.OnItemClickListener {

    private List<ZhihuQuestion> targetList;

    public static CaptureFragment newInstance() {
        return new CaptureFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.includeToolbar.toolbar.setTitle("发现");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> RxBus.get().post(new DrawerToggleEvent(true)));

        binding.refreshLayout.setEnabled(false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));

        init();
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                RxBus.get().post(new StartFragmentEvent(ZhihuFragment.newInstance()));
                break;
            default:
                break;
        }
    }

    private void init() {
        targetList = new ArrayList<>();
        targetList.add(new ZhihuQuestion(0L, 0, "知乎问题", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(1L, 1, "知乎收藏", "https://img3.doubanio.com/lpic/s28586695.jpg"));

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, targetList, R.layout.item_capture);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
}
