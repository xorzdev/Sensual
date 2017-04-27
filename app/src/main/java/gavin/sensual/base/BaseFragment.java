package gavin.sensual.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import gavin.sensual.inject.component.ApplicationComponent;
import gavin.sensual.service.base.DataLayer;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2016/12/30  2016/12/30
 */
public abstract class BaseFragment extends SupportFragment {

    @Inject
    DataLayer mDataLayer;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ApplicationComponent.Instance.get().inject(this);
        afterCreate(savedInstanceState);
    }

    public DataLayer getDataLayer() {
        return mDataLayer;
    }

    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);

}
