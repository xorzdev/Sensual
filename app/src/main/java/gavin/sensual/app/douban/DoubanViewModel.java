package gavin.sensual.app.douban;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class DoubanViewModel extends ImageViewModel {

    private String cid;

    DoubanViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, String cid) {
        super(context, fragment, binding);
        this.cid = cid;
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        if ("0".equals(cid)) {
            return getDataLayer().getDoubanService().getShow(mFragment.get(), cid, (int) (Math.random() * 5070));
        } else if (TextUtils.isEmpty(cid)) {
            return getDataLayer().getDoubanService().getRank(mFragment.get(), isMore ? pagingOffset + 1 : 1);
        } else {
            return getDataLayer().getDoubanService().getShow(mFragment.get(), cid, isMore ? pagingOffset + 1 : 1);
        }
    }
}
