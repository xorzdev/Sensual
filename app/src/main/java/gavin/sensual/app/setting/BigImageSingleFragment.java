package gavin.sensual.app.setting;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.tbruyelle.rxpermissions2.RxPermissions;

import gavin.sensual.R;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.CacheHelper;
import gavin.sensual.base.RequestCode;
import gavin.sensual.databinding.FragBigImageSingleBinding;
import gavin.sensual.util.ImageLoader;
import gavin.sensual.util.L;
import gavin.sensual.util.ShareUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 查看大图 - photoView - 单图模式
 *
 * @author gavin.xiong 2017/2/28
 */
public class BigImageSingleFragment extends BindingFragment<FragBigImageSingleBinding>
        implements OnPhotoTapListener, OnOutsidePhotoTapListener, View.OnLongClickListener, DialogInterface.OnClickListener, RequestListener<String, GlideDrawable> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private String imageUrl;
    private boolean popIntercept = false;

    public static BaseFragment newInstance(String imageUrl, boolean popIntercept) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKey.IMAGE_URL, imageUrl);
        bundle.putBoolean(BundleKey.POP_INTERCEPT, popIntercept);
        BaseFragment fragment = new BigImageSingleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_big_image_single;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
    }

    private void init() {
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(binding.photoView);
        mAttacher.setOnPhotoTapListener(this);
        mAttacher.setOnOutsidePhotoTapListener(this);
        mAttacher.setOnLongClickListener(this);

        imageUrl = getArguments().getString(BundleKey.IMAGE_URL);
        popIntercept = getArguments().getBoolean(BundleKey.POP_INTERCEPT);

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(this)
                .into(binding.photoView);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        if (popIntercept) {
            ((BaseFragment) getParentFragment()).pop();
        } else {
            pop();
        }
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        if (popIntercept) {
            ((BaseFragment) getParentFragment()).pop();
        } else {
            pop();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        String[] items = new String[]{"保存到手机", "分享", "其他"};
        new AlertDialog.Builder(_mActivity)
                .setItems(items, this)
                .show();
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                requestPermission();
                break;
            case 1:
                shareImage();
                break;
        }
    }

    private void requestPermission() {
        new RxPermissions(_mActivity)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(permission -> {
                    if (permission.granted) {
                        saveBitmap();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        Snackbar.make(binding.photoView, "无写入 sdcard 卡权限", Snackbar.LENGTH_LONG)
                                .setAction("重试", v -> requestPermission())
                                .show();
                    } else {
                        Snackbar.make(binding.photoView, "无写入 sdcard 卡权限", Snackbar.LENGTH_INDEFINITE)
                                .setAction("开启", v -> openAppSetting())
                                .show();
                    }
                });
    }

    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + _mActivity.getPackageName()));
        startActivityForResult(intent, RequestCode.PERMISSION_SETTING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestPermission();
    }

    private void saveBitmap() {
        Observable.just(imageUrl)
                .map(permission -> ImageLoader.getBitmap(this, imageUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(bitmap -> {
                    L.e(Looper.myLooper() == Looper.getMainLooper());
                    CacheHelper.saveBitmap(bitmap, imageUrl.substring(imageUrl.lastIndexOf("/"), imageUrl.lastIndexOf(".")));
                    Snackbar.make(binding.photoView, "保存成功", Snackbar.LENGTH_LONG).show();
                }, throwable -> Snackbar.make(binding.photoView, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void shareImage() {
        Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(s -> ShareUtil.shareImage(this, s),
                        e -> Snackbar.make(binding.photoView, e.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(this)
                .into(binding.photoView);
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        binding.progressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
