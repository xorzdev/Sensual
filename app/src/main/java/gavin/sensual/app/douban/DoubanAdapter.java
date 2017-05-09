package gavin.sensual.app.douban;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.gank.Welfare;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemDoubanBinding;
import gavin.sensual.databinding.ItemWelfareBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.ImageLoader;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
class DoubanAdapter extends RecyclerHeaderFooterAdapter<Image, ItemDoubanBinding, ViewDataBinding, FooterLoadingBinding> {

    private OnItemClickListener mListener;

    private int mWidth;

    DoubanAdapter(Context context, List<Image> mData) {
        super(context, mData, R.layout.item_douban);
        mWidth = DisplayUtil.getScreenWidth() / 2 - DisplayUtil.dp2px(12);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ItemDoubanBinding> holder, int position, Image t) {
        int tempHeight = (int)(t.getHeight() / (t.getWidth() + 0f) * mWidth);
        holder.binding.imageView.getLayoutParams().height = tempHeight;
        ImageLoader.loadImage(holder.binding.imageView, t.getUrl(), mWidth, tempHeight);

        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
