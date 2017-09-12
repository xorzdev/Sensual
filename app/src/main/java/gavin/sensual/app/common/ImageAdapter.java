package gavin.sensual.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.function.IntConsumer;
import gavin.sensual.base.recycler.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.recycler.RecyclerHolder;
import gavin.sensual.databinding.ItemImageBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.ImageLoader;

/**
 * 图片列表适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class ImageAdapter extends RecyclerHeaderFooterAdapter<Image, ItemImageBinding> {

    private Fragment mFragment;
    private int mWidth;

    private IntConsumer mListener;

    ImageAdapter(Context context, Fragment fragment, List<Image> mData) {
        super(context, mData, R.layout.item_image);
        mFragment = fragment;
        mWidth = DisplayUtil.getScreenWidth() / 2 - DisplayUtil.dp2px(12);
    }

    public void setListener(IntConsumer listener) {
        this.mListener = listener;
    }

    @Override
    protected void onBind(RecyclerHolder<ItemImageBinding> holder, int position, Image t) {
        int tempHeight = (int) (t.getHeight() / (t.getWidth() + 0f) * mWidth);
        holder.binding.imageView.getLayoutParams().height = tempHeight;
        ImageLoader.loadImage(mFragment, holder.binding.imageView, t.getUrl(), mWidth, tempHeight);

        holder.binding.item.setOnClickListener((v) -> {
            if (mListener != null) {
                mListener.accept(position);
            }
        });
    }
}
