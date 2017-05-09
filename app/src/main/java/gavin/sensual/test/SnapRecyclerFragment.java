package gavin.sensual.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RecyclerAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.FragBigImageSingleBinding;
import gavin.sensual.databinding.TestFragSnapBinding;
import gavin.sensual.util.DisplayUtil;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/9
 */
public class SnapRecyclerFragment extends BindingFragment<TestFragSnapBinding> {

    public static SnapRecyclerFragment newInstance() {
        return new SnapRecyclerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_frag_snap;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
        }
        Adapter adapter = new Adapter(_mActivity, list);
        binding.recycler.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recycler);
    }

    private class Adapter extends RecyclerAdapter<String, FragBigImageSingleBinding> {

        Adapter(Context context, List<String> mData) {
            super(context, mData, R.layout.frag_big_image_single);
        }

        @Override
        public void onBind(RecyclerHolder<FragBigImageSingleBinding> holder, String s, int position) {
            holder.binding.root.getLayoutParams().width = DisplayUtil.getScreenWidth();
            holder.binding.progressBar.setVisibility(View.GONE);
            Glide.with(SnapRecyclerFragment.this)
                    .load(s)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .listener(this)
                    .into(holder.binding.photoView);
        }

    }
}
