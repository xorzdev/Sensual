package gavin.sensual.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.databinding.FragNavigationBinding;
import gavin.sensual.test.ScrollingFragment;

/**
 * 侧滑菜单页
 *
 * @author gavin.xiong 2017/4/25
 */
public class NavigationFragment extends BaseFragment<FragNavigationBinding> {

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_navigation;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, ScrollingFragment.newInstance());
        }
    }
}
