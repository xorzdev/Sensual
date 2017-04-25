package gavin.sensual.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.setting.PermissionFragment;
import gavin.sensual.base.BaseActivity;
import gavin.sensual.databinding.ActMainBinding;

public class MainActivity extends BaseActivity<ActMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, PermissionFragment.newInstance());
        }
    }
}
