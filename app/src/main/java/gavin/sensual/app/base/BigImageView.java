package gavin.sensual.app.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import gavin.sensual.databinding.ItemBigImageBinding;
import gavin.sensual.util.L;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/18
 */
public class BigImageView extends FrameLayout {

    private ItemBigImageBinding binding;
    private int position;

    public BigImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = ItemBigImageBinding.inflate(LayoutInflater.from(context));

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
        this.addView(binding.getRoot(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setData(Fragment fragment, String url, int position) {
        this.position = position;
        Glide.with(fragment)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.photoView);
    }

}
