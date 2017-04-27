package gavin.sensual.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.TestFragBinding;

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
        binding.textView.setText(getDataLayer().toString());
    }
}
