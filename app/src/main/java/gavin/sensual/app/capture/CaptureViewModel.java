package gavin.sensual.app.capture;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.R;
import gavin.sensual.app.capture.jiandan.JiandanFragment;
import gavin.sensual.app.capture.jiandan.JiandanTopFragment;
import gavin.sensual.app.capture.maijiaxiu.MaijiaxiuFragment;
import gavin.sensual.app.capture.zhihu.ZhihuFragment;
import gavin.sensual.app.capture.zhihu.ZhihuViewModel;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.base.recycler.BindingHeaderFooterAdapter;
import gavin.sensual.base.recycler.PagingViewModel;

/**
 * 发现
 *
 * @author gavin.xiong 2017/8/14
 */
class CaptureViewModel extends PagingViewModel<Capture, BindingHeaderFooterAdapter<Capture>> {

    CaptureViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
        refreshable.set(false);
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_capture);
        adapter.setOnItemClickListener(position -> {
            switch (position) {
                case 0:
                    RxBus.get().post(new StartFragmentEvent(ZhihuFragment.newInstance(ZhihuViewModel.TYPE_QUESTION)));
                    break;
                case 1:
                    RxBus.get().post(new StartFragmentEvent(ZhihuFragment.newInstance(ZhihuViewModel.TYPE_COLLECTION)));
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
                case 5:
                    RxBus.get().post(new StartFragmentEvent(MaijiaxiuFragment.newInstance()));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void getData(boolean isMore) {
        mList.add(new Capture("知乎看图 - 问题", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        mList.add(new Capture("知乎看图 - 收藏", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        mList.add(new Capture("煎蛋妹子图", "https://img3.doubanio.com/lpic/s29203893.jpg"));
        mList.add(new Capture("煎蛋妹子图精选 1", "https://img3.doubanio.com/lpic/s29203893.jpg"));
        mList.add(new Capture("煎蛋妹子图精选 2", "https://img3.doubanio.com/lpic/s29203893.jpg"));
        mList.add(new Capture("买家秀", "https://img3.doubanio.com/lpic/s29387793.jpg"));
        adapter.notifyDataSetChanged();
    }
}
