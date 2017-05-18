package gavin.sensual.app.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.lang.reflect.Field;

import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestItemBinding;
import gavin.sensual.util.ImageLoader;
import gavin.sensual.util.L;
import gavin.sensual.util.ShareUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/18
 */
public class BigImageView extends FrameLayout {

    private CompositeDisposable compositeDisposable;

    private TestItemBinding binding;
    private Fragment mFragment;
    private String imageUrl;
    private int position;

    public BigImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = TestItemBinding.inflate(LayoutInflater.from(context));

        PhotoViewAttacher attacher = binding.photoView.getAttacher();

        try {
            Field mScaleDragDetector = attacher.getClass().getDeclaredField("mScaleDragDetector");
            mScaleDragDetector.setAccessible(true);
            Object customGestureDetector = mScaleDragDetector.get(attacher);
            float touchShop = ViewConfiguration.get(context).getScaledTouchSlop();
            Field mTouchSlop = customGestureDetector.getClass().getDeclaredField("mTouchSlop");
            mTouchSlop.setAccessible(true);
            mTouchSlop.set(customGestureDetector, touchShop * 3);
        } catch (Exception e) {
            L.e(e);
        }

        attacher.setOnPhotoTapListener((view, x, y) -> RxBus.get().post(new BigImageClickEvent(position)));
        attacher.setOnOutsidePhotoTapListener(imageView -> RxBus.get().post(new BigImageClickEvent(position)));
        attacher.setOnLongClickListener(v -> {
            String[] items = new String[]{"保存到手机", "分享"};
            new AlertDialog.Builder(context)
                    .setItems(items, (dialog, which) -> {
                        if (which == 0) {
                            createFile(imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")) + ".jpg");
                        } else if (which == 1) {
                            shareImage();
                        }
                    })
                    .show();
            return false;
        });
        this.addView(binding.getRoot(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setData(Fragment fragment, String url, int position) {
        mFragment = fragment;
        imageUrl = url;
        this.position = position;

        Glide.with(fragment)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.photoView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        compositeDisposable = new CompositeDisposable();

        RxBus.get().toObservable(SaveImageEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> saveBitmap(event.uri));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.dispose();
    }

    private void createFile(String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        mFragment.startActivityForResult(intent, 99);
    }

    private void saveBitmap(Uri uri) {
        Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(uri1 -> getContext().getContentResolver().openOutputStream(uri1))
                .filter(outputStream -> outputStream != null)
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(outputStream -> {
                    try {
                        Bitmap bitmap = ImageLoader.getBitmap(mFragment, imageUrl);
                        boolean state = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        Snackbar.make(binding.photoView, state ? "保存成功" : "保存失败", Snackbar.LENGTH_LONG).show();
                    } finally {
                        outputStream.close();
                    }
                }, throwable -> Snackbar.make(binding.photoView, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void shareImage() {
        Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(s -> ShareUtil.shareImage(mFragment, s),
                        e -> Snackbar.make(binding.photoView, e.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
