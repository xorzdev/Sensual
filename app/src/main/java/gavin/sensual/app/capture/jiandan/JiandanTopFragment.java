package gavin.sensual.app.capture.jiandan;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/16
 */
public class JiandanTopFragment extends BindingFragment<LayoutPagingToolbarBinding, JiandanTopViewModel> {

    public static JiandanTopFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.PAGE_TYPE, type);
        JiandanTopFragment fragment = new JiandanTopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new JiandanTopViewModel(getContext(), this, mBinding, getArguments().getInt(BundleKey.PAGE_TYPE));
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("煎蛋妹子图");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }
}
