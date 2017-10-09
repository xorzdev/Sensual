package gavin.sensual.app.mzitu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutPagingBinding;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
public class MzituFragment extends BindingFragment<LayoutPagingBinding, MzituViewModel> {

    public static MzituFragment newInstance(String cid) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, cid);
        MzituFragment fragment = new MzituFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new MzituViewModel(getContext(), this, mBinding,
                getArguments().getString(BundleKey.PAGE_TYPE));
        mBinding.setVm(mViewModel);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewModel.afterCreate();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
