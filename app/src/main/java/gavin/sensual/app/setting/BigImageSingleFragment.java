package gavin.sensual.app.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import gavin.sensual.R;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.FragBigImageSingleBinding;

/**
 * 查看大图 - photoView - 单图模式
 *
 * @author gavin.xiong 2017/2/28
 */
public class BigImageSingleFragment extends BindingFragment<FragBigImageSingleBinding>
        implements OnPhotoTapListener, OnOutsidePhotoTapListener, RequestListener<String, GlideDrawable> {

    private String imageUrl;

    public static BaseFragment newInstance(String imageUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKey.IMAGE_URL, imageUrl);
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
    protected void afterCreate(@Nullable Bundle savedInstanceState) { }

    private void init() {
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(binding.photoView);
        mAttacher.setOnPhotoTapListener(this);
        mAttacher.setOnOutsidePhotoTapListener(this);

        imageUrl = getArguments().getString(BundleKey.IMAGE_URL);

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(this)
                .into(binding.photoView);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        pop();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        pop();
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
}
