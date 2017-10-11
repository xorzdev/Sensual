package gavin.sensual.app.meizitu;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.concurrent.TimeUnit;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.app.common.bm.BigImageLoadStateEvent;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.base.recycler.FooterViewModel;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class MeizituRangViewModel extends ImageViewModel {

    private String url;

    MeizituRangViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, String url) {
        super(context, fragment, binding);
        this.url = url;
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getMeiziPicService().getImageRange(mFragment.get(), url);
    }

    @Override
    protected void getData(boolean isMore) {
        getDataSrc(isMore)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    doOnSubscribe(isMore);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), disposable, null, null, null, null));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    doOnComplete(isMore);
                    mFooterViewModel.notifyStateChanged(FooterViewModel.STATE_PERIOD);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, null, null, null));
                })
                .doOnError(e -> {
                    doOnError(isMore, e);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, e, null, null, null));
                })
                .doOnNext(image -> {
                    pagingHaveMore = false;
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, null, null, pagingHaveMore));
                })
                .subscribe(image -> {
                    accept(isMore, image);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, image, null, null));
                }, L::e);
    }
}
