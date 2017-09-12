package gavin.sensual.app.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 开源许可
 *
 * @author gavin.xiong 2017/5/10
 */
public class LicenseFragment extends BindingFragment<LayoutPagingToolbarBinding, LicenseViewModel> {

    public static LicenseFragment newInstance() {
        return new LicenseFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new LicenseViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("开源许可");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
