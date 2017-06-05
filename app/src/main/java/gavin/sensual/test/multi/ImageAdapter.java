package gavin.sensual.test.multi;

import android.content.Context;
import android.widget.CompoundButton;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestItemImageBinding;
import gavin.sensual.util.DisplayUtil;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/6/3
 */
public class ImageAdapter extends RecyclerAdapter<Image, TestItemImageBinding> {

    private int height;

    private int mMaxCount;
    private List<String> mSelectedImagePath;

    public ImageAdapter(Context context, List<Image> mData) {
        super(context, mData, R.layout.test_item_image);
        height = (DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(32)) / 3;
    }

    public void setCountAndData(int maxCount, List<String> mSelectedImagePath) {
        this.mMaxCount = maxCount;
        this.mSelectedImagePath = mSelectedImagePath;
    }

    @Override
    public void onBind(RecyclerHolder<TestItemImageBinding> holder, Image t, int position) {
        holder.binding.imageView.getLayoutParams().height = height;
        holder.binding.setItem(t);
        holder.binding.setAdapter(this);
    }

    public void onCheckedChanged(CompoundButton v, Image t, boolean isChecked) {
        if (!isChecked || mSelectedImagePath.size() < mMaxCount) {
            t.setChecked(isChecked);
        } else {
            t.setChecked(false);
            v.setChecked(false);
        }
        RxBus.get().post(new TestCheckedChangedEvent(t.getPath(), isChecked));
    }
}
