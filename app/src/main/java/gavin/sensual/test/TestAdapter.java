package gavin.sensual.test;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.douban.Image;
import gavin.sensual.base.RecyclerHeaderFooterAdapter;
import gavin.sensual.base.RecyclerHolder;
import gavin.sensual.databinding.RighterLoadingBinding;
import gavin.sensual.databinding.TestItemTwoBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/17
 */
public class TestAdapter extends RecyclerHeaderFooterAdapter<Image, TestItemTwoBinding, RighterLoadingBinding, RighterLoadingBinding> {

    private Fragment fragment;

    public TestAdapter(Context context, Fragment fragment, List<Image> mList) {
        super(context, mList, R.layout.test_item_two);
        this.fragment = fragment;
    }

    @Override
    public void onBind(RecyclerHolder<TestItemTwoBinding> holder, int position, Image t) {
        holder.binding.root.setData(fragment, t.getUrl(), position);
    }
}
