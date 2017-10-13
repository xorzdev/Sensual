package gavin.sensual.app.capture.topit;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * TopitDetailsModel
 *
 * @author gavin.xiong 2017/8/11
 */
class TopitDetailsModel extends ImageViewModel {

    private final long id;

    TopitDetailsModel(Context context, BaseFragment fragment, ViewDataBinding binding, long id) {
        super(context, fragment, binding);
        this.id = id;
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getTopitService().getAlbum(id, isMore ? pagingOffset : 0);
    }

}
