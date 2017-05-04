package gavin.sensual.app.daily;

import android.content.Context;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemDailyBinding;

/**
 * 日报列表适配器
 *
 * @author gavin.xiong 2017/5/4
 */
class DailyAdapter extends RecyclerHeaderFooterAdapter<Daily.Story, ItemDailyBinding, FooterLoadingBinding, FooterLoadingBinding> {

    private OnItemClickListener onItemClickListener;

    DailyAdapter(Context context, List<Daily.Story> mList) {
        super(context, mList, R.layout.item_daily);
    }

    @Override
    public void onBind(RecyclerHolder<ItemDailyBinding> holder, int position, Daily.Story t) {
        holder.binding.setItem(t);
        holder.binding.item.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
