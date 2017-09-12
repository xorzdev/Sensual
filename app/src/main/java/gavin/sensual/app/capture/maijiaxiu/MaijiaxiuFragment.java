package gavin.sensual.app.capture.maijiaxiu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 买家秀
 *
 * @author gavin.xiong 2017/8/14
 */
public class MaijiaxiuFragment extends BindingFragment<LayoutPagingToolbarBinding, MaijiaxiuViewModel> {

    public static MaijiaxiuFragment newInstance() {
        return new MaijiaxiuFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new MaijiaxiuViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("买家秀");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }

}
