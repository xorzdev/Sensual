package gavin.sensual.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

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
        init();
    }

    private void init() {
        TextView textView = (TextView) binding.navigation.getMenu().findItem(R.id.nav_tip).getActionView();
        textView.setText("9+");
        TextView textView2 = (TextView) binding.navigation.getMenu().findItem(R.id.nav_msg).getActionView().findViewById(R.id.textView);
        textView2.setText("提示信息");
        SwitchCompat switchCompat = (SwitchCompat) binding.navigation.getMenu().findItem(R.id.nav_switch).getActionView();
        switchCompat.setChecked(true);
    }
}
