package gavin.sensual.app.gank;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemWelfareBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.ImageLoader;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
class GankAdapter extends RecyclerHeaderFooterAdapter<Welfare, ItemWelfareBinding, ViewDataBinding, FooterLoadingBinding> {

    private OnItemClickListener mListener;

    private int mWidth;

    GankAdapter(Context context, List<Welfare> mData) {
        super(context, mData, R.layout.item_welfare);
        mWidth = DisplayUtil.getScreenWidth() / 2 - DisplayUtil.dp2px(12);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ItemWelfareBinding> holder, int position, Welfare t) {
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
