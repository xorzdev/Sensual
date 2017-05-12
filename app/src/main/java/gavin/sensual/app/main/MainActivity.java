package gavin.sensual.app.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.capture.CaptureFragment;
import gavin.sensual.app.daily.DailyFragment;
import gavin.sensual.app.douban.DoubanTabFragment;
import gavin.sensual.app.gank.GankFragment;
import gavin.sensual.app.setting.LicenseFragment;
import gavin.sensual.app.setting.PermissionFragment;
import gavin.sensual.base.BindingActivity;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.ActMainBinding;
import gavin.sensual.test.TestFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BindingActivity<ActMainBinding>
        implements NavigationView.OnNavigationItemSelectedListener {

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

        subscribeEvent();
        binding.navigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawer.closeDrawer(Gravity.START);
        switch (item.getItemId()) {
            case R.id.nav_news:
                return true;
            case R.id.nav_gank:
                startDelay(GankFragment.newInstance());
                return false;
            case R.id.nav_douban:
                startDelay(DoubanTabFragment.newInstance());
                return false;
            case R.id.nav_capture:
                startDelay(CaptureFragment.newInstance());
                return false;
            case R.id.nav_unknown:
                Snackbar.make(binding.drawer, item.getTitle(), Snackbar.LENGTH_LONG).show();
                return false;
            case R.id.nav_license:
                startDelay(LicenseFragment.newInstance());
                return false;
            case R.id.nav_about:
                Snackbar.make(binding.drawer, item.getTitle(), Snackbar.LENGTH_LONG).show();
                return false;
            case R.id.nav_test:
//                start(SnapRecyclerFragment.newInstance());
                startDelay(TestFragment.newInstance());
                return true;
        }
        return false;
    }


    @Override
    public void onBackPressedSupport() {
        if (binding.drawer.isDrawerOpen(Gravity.START)) {
            binding.drawer.closeDrawer(Gravity.START);
        } else if (!(getTopFragment() instanceof DailyFragment)) {
            pop();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void startDelay(SupportFragment fragment) {
        Observable.just(fragment)
                .delay(380, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(this::start);
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(StartFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> start(event.supportFragment));

        RxBus.get().toObservable(DrawerToggleEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe((event -> {
                    if (event.open) {
                        binding.drawer.openDrawer(Gravity.START);
                    } else {
                        binding.drawer.closeDrawer(Gravity.START);
                    }
                }));
    }
}
