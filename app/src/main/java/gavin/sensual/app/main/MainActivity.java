package gavin.sensual.app.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.capture.CaptureFragment;
import gavin.sensual.app.capture.topit.TopitmeFragment;
import gavin.sensual.app.collection.CollectionFragment;
import gavin.sensual.app.daily.DailyFragment;
import gavin.sensual.app.douban.DoubanTabFragment;
import gavin.sensual.app.gank.GankFragment;
import gavin.sensual.app.meizitu.MeizituTabFragment;
import gavin.sensual.app.mzitu.MzituTabFragment;
import gavin.sensual.app.setting.AboutFragment;
import gavin.sensual.base.BindingActivity;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.ActMainBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BindingActivity<ActMainBinding>
        implements NavigationView.OnNavigationItemSelectedListener {

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
            loadRootFragment(R.id.holder, DailyFragment.newInstance());
        }

        subscribeEvent();
        binding.navigation.setNavigationItemSelectedListener(this);
        binding.navigation.getMenu().findItem(R.id.nav_news).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawer.closeDrawer(Gravity.START);
        if (item.isChecked()) return true;
        switch (item.getItemId()) {
            case R.id.nav_news:
                popTo();
                break;
            case R.id.nav_topitme:
                next(TopitmeFragment.newInstance());
                break;
            case R.id.nav_gank:
                next(GankFragment.newInstance());
                break;
            case R.id.nav_douban:
                next(DoubanTabFragment.newInstance());
                break;
            case R.id.nav_mzitu:
                next(MzituTabFragment.newInstance());
                break;
            case R.id.nav_meizitu:
                next(MeizituTabFragment.newInstance());
                break;
            case R.id.nav_capture:
                next(CaptureFragment.newInstance());
                break;
            case R.id.nav_collection:
                next(CollectionFragment.newInstance());
                break;
            case R.id.nav_about:
                next(AboutFragment.newInstance());
                break;
            case R.id.nav_test:
//                start(SnapRecyclerFragment.newInstance());
//                startDelay(ImagesFragment.newInstance());
//                startDelay(TestCommentFragment.newInstance());
                break;
        }
        return false;
    }

    @Override
    public void onBackPressedSupport() {
        if (binding.drawer.isDrawerOpen(Gravity.START)) {
            binding.drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressedSupport();
        }
    }

    private void popTo() {
        Observable.just(0)
                .delay(380, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(arg0 -> popTo(DailyFragment.class, false));
    }

    private void next(SupportFragment fragment) {
        Observable.just(fragment)
                .delay(380, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(this::start);
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(StartFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> start(event.supportFragment));

        RxBus.get().toObservable(DrawerStateEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (event.open) {
                        binding.drawer.openDrawer(Gravity.START);
                    } else {
                        binding.drawer.closeDrawer(Gravity.START);
                    }
                });

        RxBus.get().toObservable(DrawerEnableEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> binding.drawer.setDrawerLockMode(event.enable
                        ? DrawerLayout.LOCK_MODE_UNLOCKED
                        : DrawerLayout.LOCK_MODE_LOCKED_CLOSED));
    }
}
