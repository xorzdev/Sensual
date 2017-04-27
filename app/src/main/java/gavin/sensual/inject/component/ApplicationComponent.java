package gavin.sensual.inject.component;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import gavin.sensual.base.BaseActivity;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.inject.module.ApplicationModule;
import gavin.sensual.service.base.BaseManager;

/**
 * @author lsxiao
 * @date 2015-11-04 00:47
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

//    void inject(BaseDialogFragment dialogFragment);

    void inject(BaseManager manager);

    // 可以获取 ApplicationModule 及其 includes 的所有 Module 提供的对象（方法名随意）
    Application getApplication();

    class Instance {
        private static ApplicationComponent sComponent;

        public static void init(@NonNull ApplicationComponent component) {
            sComponent = component;
        }

        public static ApplicationComponent get() {
            return sComponent;
        }
    }
}
