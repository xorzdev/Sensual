package gavin.sensual.app.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 收藏
 *
 * @author gavin.xiong 2017/8/16
 */
public class CollectionFragment extends BindingFragment<LayoutPagingToolbarBinding, CollectionViewModel> {

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new CollectionViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("收藏");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v
                -> RxBus.get().post(new DrawerToggleEvent(true)));
    }
}
