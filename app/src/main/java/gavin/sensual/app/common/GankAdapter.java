package gavin.sensual.app.common;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.base.Image;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemImageBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.ImageLoader;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class GankAdapter extends RecyclerHeaderFooterAdapter<Image, ItemImageBinding, ViewDataBinding, FooterLoadingBinding> {

    private OnItemClickListener mListener;

    private int mWidth;

    public GankAdapter(Context context, List<Image> mData) {
        super(context, mData, R.layout.item_image);
        mWidth = DisplayUtil.getScreenWidth() / 2 - DisplayUtil.dp2px(12);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ItemImageBinding> holder, int position, Image t) {
        int tempHeight = (int)(t.getHeight() / (t.getWidth() + 0f) * mWidth);
        holder.binding.imageView.getLayoutParams().height = tempHeight;
        ImageLoader.loadImage(holder.binding.imageView, t.getUrl(), mWidth, tempHeight);

        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
