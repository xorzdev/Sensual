package gavin.sensual.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;

import javax.inject.Inject;

import gavin.sensual.inject.component.ApplicationComponent;
import gavin.sensual.service.base.DataLayer;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2016/12/30  2016/12/30
 */
public abstract class BaseActivity extends SupportActivity {

    @Inject
    DataLayer mDataLayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView();
        ApplicationComponent.Instance.get().inject(this);
        afterCreate(savedInstanceState);
    }

    public DataLayer getDataLayer() {
        return mDataLayer;
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }

    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);


    public abstract void setContentView();
}
