package gavin.sensual.app.gank;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemWelfareBinding;
import gavin.sensual.util.DisplayUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        holder.binding.imageView.getLayoutParams().height = (int)(t.getHeight() / (t.getWidth() + 1f) * mWidth);
        Glide.with(mContext).load(t.getUrl()).into(holder.binding.imageView);

        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
