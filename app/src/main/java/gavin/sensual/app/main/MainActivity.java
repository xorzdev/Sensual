package gavin.sensual.app.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import gavin.sensual.R;
import gavin.sensual.app.setting.PermissionFragment;
import gavin.sensual.base.BindingActivity;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.ActMainBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BindingActivity<ActMainBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        RxBus.get().toObservable(StartFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> start(event.supportFragment));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
