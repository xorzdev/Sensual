package gavin.sensual.test.multi;

import android.content.Context;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestItemFolderBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/6/3
 */
class FolderAdapter extends RecyclerAdapter<Image, TestItemFolderBinding> {

    FolderAdapter(Context context, List<Image> mData) {
        super(context, mData, R.layout.test_item_folder);
    }

    @Override
    public void onBind(RecyclerHolder<TestItemFolderBinding> holder, Image t, int position) {
        holder.binding.setItem(t);
        holder.binding.item.setOnClickListener(v -> RxBus.get().post(new FolderChangedEvent(t)));
    }

}
