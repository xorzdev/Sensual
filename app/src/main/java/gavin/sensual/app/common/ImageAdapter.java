package gavin.sensual.app.common;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.base.Image;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FooterLoadingBinding;
import gavin.sensual.databinding.ItemImageBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.ImageLoader;

/**
 * 图片列表适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class ImageAdapter extends RecyclerHeaderFooterAdapter<Image, ItemImageBinding, ViewDataBinding, FooterLoadingBinding> {

    private Fragment mFragment;
    private int mWidth;

    public ImageAdapter(Context context, Fragment fragment, List<Image> mData) {
        super(context, mData, R.layout.item_image);
        mFragment = fragment;
        mWidth = DisplayUtil.getScreenWidth() / 2 - DisplayUtil.dp2px(12);
    }

    @Override
    public void onBind(RecyclerHolder<ItemImageBinding> holder, int position, Image t) {
        int tempHeight = (int) (t.getHeight() / (t.getWidth() + 0f) * mWidth);
        holder.binding.imageView.getLayoutParams().height = tempHeight;
        ImageLoader.loadImage(mFragment, holder.binding.imageView, t.getUrl(), mWidth, tempHeight);

        holder.binding.item.setOnClickListener((v) -> {
            RxBus.get().post(new StartFragmentEvent(BigImageFragment.newInstance((ArrayList<Image>) mList, position, mFragment.hashCode())));
        });
    }
}
