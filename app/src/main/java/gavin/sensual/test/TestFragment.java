package gavin.sensual.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.TestFragBinding;
import gavin.sensual.widget.banner.BannerModel;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class TestFragment extends BindingFragment<TestFragBinding> {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_frag;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://i2.hdslb.com/bfs/archive/d4f02bfb6bd5ea22c8ca9f082dac9429ff6fe399.jpg");
        urlList.add("http://i0.hdslb.com/bfs/space/42401a068c2d6d4b254b7c53348fc8929ad4d8e9.jpg");
        urlList.add("http://i0.hdslb.com/bfs/space/e138569049093b9114f988d198b5e299975a5389.jpg");
        List<BannerModel> modelList = new ArrayList<>();
        for (String s : urlList) {
            modelList.add(new BannerModel(0L, s, s));
        }
        binding.banner.setModelList(modelList);
    }
}
