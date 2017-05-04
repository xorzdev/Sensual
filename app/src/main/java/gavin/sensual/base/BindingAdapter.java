package gavin.sensual.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import java.util.List;

import gavin.sensual.R;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingAdapter<T> extends RecyclerAdapter<T, ViewDataBinding> {

    private int variableId;
    private OnItemClickListener mListener;

    public BindingAdapter(Context context, List<T> mData, @LayoutRes int layoutId, int variableId) {
        super(context, mData, layoutId);
        this.variableId = variableId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ViewDataBinding> holder, T t, final int position) {
        holder.binding.setVariable(variableId, t);
//        holder.binding.executePendingBindings();
        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
