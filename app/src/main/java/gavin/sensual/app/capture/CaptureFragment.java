package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 发现
 *
 * @author gavin.xiong 2017/5/10
 */
public class CaptureFragment extends BindingFragment<LayoutPagingToolbarBinding, CaptureViewModel> {

    public static CaptureFragment newInstance() {
        return new CaptureFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new CaptureViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("发现");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
    }
}
