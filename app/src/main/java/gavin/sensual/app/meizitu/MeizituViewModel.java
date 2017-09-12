package gavin.sensual.app.meizitu;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import io.reactivex.Observable;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class MeizituViewModel extends ImageViewModel {

    private String cid;

    MeizituViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, String cid) {
        super(context, fragment, binding);
        this.cid = cid;
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.setListener(i -> {
            if (TextUtils.isEmpty(cid)) {
                RxBus.get().post(new StartFragmentEvent(
                        MeizituRangFragment.newInstance(mList.get(i).getUrl())));
            } else {
                RxBus.get().post(new StartFragmentEvent(
                        MeizituRangFragment.newInstance(mList.get(i).getUrl().replace("limg", "00"))));
            }
        });
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getMeizituService().getPic(mFragment.get(), cid, isMore ? pagingOffset + 1 : 1);
    }

}
