package gavin.sensual.app.capture.zhihu;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * 干货集中营
 *
 * @author gavin.xiong 2017/8/11
 */
class ZhihuDetailsModel extends ImageViewModel {

    private final int type;
    private final long id;

    ZhihuDetailsModel(Context context, BaseFragment fragment, ViewDataBinding binding, int type, long id) {
        super(context, fragment, binding);
        this.type = type;
        this.id = id;
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return type == ZhihuViewModel.TYPE_COLLECTION
                ? getDataLayer().getZhihuPicService().getCollectionPic(mFragment.get(), id, isMore ? pagingOffset + 1 : 1)
                : getDataLayer().getZhihuPicService().getQuestionPic(mFragment.get(), id, 5, isMore ? pagingOffset * 5 : 0);
    }
}
