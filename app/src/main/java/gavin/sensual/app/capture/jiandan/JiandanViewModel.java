package gavin.sensual.app.capture.jiandan;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * 煎蛋妹子图
 *
 * @author gavin.xiong 2017/8/11
 */
class JiandanViewModel extends ImageViewModel {

    private Integer pageCount;

    JiandanViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return pageCount != null ?
                getDataLayer()
                        .getJiandanService()
                        .getPic(mFragment.get(), isMore ? pageCount - pagingOffset : pageCount) :
                getDataLayer()
                        .getJiandanService()
                        .getPageCount()
                        .flatMap(i -> {
                            pageCount = i;
                            return getDataSrc(isMore);
                        });
    }

}
