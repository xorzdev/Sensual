package gavin.sensual.app.common.bm;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import gavin.sensual.app.common.Image;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.base.recycler.PagingViewModel;
import gavin.sensual.databinding.FragBigImageBinding;
import gavin.sensual.db.util.DbUtil;
import gavin.sensual.util.ImageLoader;
import gavin.sensual.util.ShareUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 查看大图
 *
 * @author gavin.xiong 2017/8/15
 */
class BigImageViewModel extends PagingViewModel<Image, BigImageAdapter> {

    private int requestCode;

    private LinearLayoutManager linearLayoutManager;

    BigImageViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    void setImageList(List<Image> list) {
        mList = list;
    }

    @Override
    protected void initAdapter() {
        adapter = new BigImageAdapter(mContext.get(), mFragment.get(), mList);
        adapter.setRequestCode(requestCode);
    }

    @Override
    protected void getData(boolean isMore) {
        if (isMore) {
            RxBus.get().post(new BigImageLoadMoreEvent(requestCode, null));
        } else {
            pagingPreCount = 3;
            pagingHaveMore = true;
        }
    }

    @Override
    public void afterCreate() {
        super.afterCreate();
        mFooterViewModel.setVertical(false);
        linearLayoutManager = (LinearLayoutManager) ((FragBigImageBinding) mBinding.get()).recycler.getLayoutManager();
        subscribeEvent();
    }

    void toPop() {
        RxBus.get().post(new BigImageLoadMoreEvent(requestCode, linearLayoutManager.findFirstVisibleItemPosition()));
        mFragment.get().pop();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(BigImageLoadStateEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .filter(event -> event.requestCode == requestCode)
                .subscribe(event -> {
                    if (event.disposable != null) {
                        doOnSubscribe(true);
                    } else if (event.throwable != null) {
                        doOnError(true, event.throwable);
                    } else if (event.haveMore != null) {
                        pagingHaveMore = event.haveMore;
                    } else if (event.t != null) {
                        adapter.notifyDataSetChanged();
                    } else if (event.ts != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        doOnComplete(true);
                    }
                });
    }

    boolean isImageCollected(String imageUrl) {
        return DbUtil.getCollectionService().hasCollected(imageUrl);
    }

    void toggleCollect(String imageUrl) {
        DbUtil.getCollectionService().toggle(imageUrl);
    }

    void shareImage(String imageUrl) {
        Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(s -> ShareUtil.shareImage(mFragment.get(), s),
                        throwable -> notifyMsg(throwable.getMessage()));
    }

    void saveBitmap(String imageUrl, Uri uri) {
        Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(uri1 -> mContext.get().getContentResolver().openOutputStream(uri1))
                .filter(outputStream -> outputStream != null)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(outputStream -> {
                    try {
                        Bitmap bitmap = ImageLoader.getBitmap(mFragment.get(), imageUrl);
                        boolean state = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        notifyMsg(state ? "保存成功" : "保存失败");
                    } finally {
                        outputStream.close();
                    }
                }, throwable -> notifyMsg(throwable.getMessage()));
    }
}
