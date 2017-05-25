package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.capture.jiandan.JiandanFragment;
import gavin.sensual.app.capture.jiandan.JiandanTopFragment;
import gavin.sensual.app.capture.zhihu.ZhihuFragment;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.app.main.StartFragmentEvent;
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
                RxBus.get().post(new StartFragmentEvent(ZhihuFragment.newInstance(ZhihuFragment.TYPE_QUESTION)));
                break;
            case 1:
                RxBus.get().post(new StartFragmentEvent(ZhihuFragment.newInstance(ZhihuFragment.TYPE_COLLECTION)));
                break;
            case 2:
                RxBus.get().post(new StartFragmentEvent(JiandanFragment.newInstance()));
                break;
            case 3:
                RxBus.get().post(new StartFragmentEvent(JiandanTopFragment.newInstance(2016)));
                break;
            case 4:
                RxBus.get().post(new StartFragmentEvent(JiandanTopFragment.newInstance(2017)));
                break;
            default:
                break;
        }
    }

    private void init() {
        List<Capture> captureList = new ArrayList<>();
        captureList.add(new Capture("知乎看图 - 问题", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        captureList.add(new Capture("知乎看图 - 收藏", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        captureList.add(new Capture("煎蛋妹子图", "http://wx4.sinaimg.cn/mw600/a0cd8cacgy1ffsqeyplujj20xc0xcjrr.jpg"));
        captureList.add(new Capture("煎蛋妹子图精选 1", "http://wx4.sinaimg.cn/mw600/a0cd8cacgy1ffsqeyplujj20xc0xcjrr.jpg"));
        captureList.add(new Capture("煎蛋妹子图精选 2", "http://wx4.sinaimg.cn/mw600/a0cd8cacgy1ffsqeyplujj20xc0xcjrr.jpg"));

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, captureList, R.layout.item_capture);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
}
