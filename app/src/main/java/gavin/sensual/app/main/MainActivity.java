package gavin.sensual.app.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import gavin.sensual.R;
import gavin.sensual.app.setting.PermissionFragment;
import gavin.sensual.base.BindingActivity;
import gavin.sensual.databinding.ActMainBinding;

public class MainActivity extends BindingActivity<ActMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        // 状态栏深色图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, PermissionFragment.newInstance());
        }
    }
}
