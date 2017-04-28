package gavin.sensual.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.TestFargScrollingBinding;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class ScrollingFragment extends BindingFragment<TestFargScrollingBinding> {

    public static ScrollingFragment newInstance() {
        return new ScrollingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_farg_scrolling;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.toolbar.inflateMenu(R.menu.frag_navigation_drawer);
    }
}
