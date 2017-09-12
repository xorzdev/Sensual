package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 干货集中营 - 福利
 *
 * @author gavin.xiong 2017/8/14
 */
public class GankFragment extends BindingFragment<LayoutPagingToolbarBinding, GankViewModel> {

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new GankViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v ->
                RxBus.get().post(new DrawerToggleEvent(true)));
        mBinding.includeToolbar.toolbar.setTitle("干货集中营");
    }

}
