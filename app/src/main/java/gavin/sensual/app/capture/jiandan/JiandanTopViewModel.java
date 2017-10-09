package gavin.sensual.app.capture.jiandan;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.concurrent.TimeUnit;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.util.AssetsUtils;
import io.reactivex.Observable;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class JiandanTopViewModel extends ImageViewModel {

    private final int type;

    private String[] images;

    JiandanTopViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, int type) {
        super(context, fragment, binding);
        this.type = type;
    }

    @Override
    public void afterCreate() {
        super.afterCreate();
        pagingLimit = 10;
        Observable.just(type)
                .doOnSubscribe(mCompositeDisposable::add)
                .map(type -> type == 2017 ? "json/jiandantop2017.img" : "json/jiandantop2016.img")
                .map(s -> AssetsUtils.readText(mContext.get(), s))
                .map(s -> s.split(","))
                .subscribe(strings -> this.images = strings, e -> notifyMsg(e.getMessage()));
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return Observable.just(isMore ? pagingOffset : 0)
                .delay(1, TimeUnit.SECONDS)
                .map(offset1 -> offset1 * pagingLimit)
                .filter(srcPos -> images != null && srcPos < images.length)
                .map(srcPos -> {
                    int countEnable = images.length - srcPos;
                    pagingHaveMore = countEnable > pagingLimit;
                    int length = pagingHaveMore ? pagingLimit : countEnable - srcPos;
                    String[] temp = new String[length];
                    System.arraycopy(images, srcPos, temp, 0, length);
                    return temp;
                })
                .flatMap(Observable::fromArray)
                .map(s -> Image.newImage(mFragment.get(), s));
    }
}
