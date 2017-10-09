package gavin.sensual.app.douban;

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
public class DoubanFragment extends BindingFragment<LayoutPagingBinding, DoubanViewModel> {

    public static DoubanFragment newInstance(String cid) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, cid);
        DoubanFragment fragment = new DoubanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new DoubanViewModel(getContext(), this, mBinding,
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
