package gavin.sensual.base;

import android.app.Application;

import gavin.sensual.inject.component.ApplicationComponent;
import gavin.sensual.inject.component.DaggerApplicationComponent;
import gavin.sensual.inject.module.ApplicationModule;

/**
 * 自定义 Application
 *
 * @author gavin.xiong 2017/4/25
 */
public class App extends Application {

    private static App sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        initDagger();
    }

    private void initDagger() {
        ApplicationComponent.Instance.init(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());
    }

    public static App getApplication() {
        return sApplication;
    }
}
