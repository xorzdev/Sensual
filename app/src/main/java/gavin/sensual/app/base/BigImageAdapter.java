package gavin.sensual.app.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.ItemBigImageBinding;
import gavin.sensual.databinding.RighterLoadingBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/17
 */
public class BigImageAdapter extends RecyclerHeaderFooterAdapter<Image, ItemBigImageBinding, RighterLoadingBinding, RighterLoadingBinding> {

    private Fragment fragment;

    public BigImageAdapter(Context context, Fragment fragment, List<Image> mList) {
        super(context, mList, R.layout.item_big_image);
        this.fragment = fragment;
    }

    @Override
    public void onBind(RecyclerHolder<ItemBigImageBinding> holder, int position, Image t) {
        holder.binding.bigImageView.setData(fragment, t.getUrl(), position);
    }
}
