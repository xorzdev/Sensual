package gavin.sensual.app.capture.zhihu;

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
public class ZhihuDetailsFragment extends BindingFragment<LayoutPagingToolbarBinding, ZhihuDetailsModel> {

    public static ZhihuDetailsFragment newInstance(int type, long id) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.PAGE_TYPE, type);
        args.putLong(BundleKey.NEWS_ID, id);
        ZhihuDetailsFragment fragment = new ZhihuDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new ZhihuDetailsModel(getContext(), this, mBinding,
                getArguments().getInt(BundleKey.PAGE_TYPE),
                getArguments().getLong(BundleKey.NEWS_ID));
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle(String.format("知乎看图 - %s",
                getArguments().getInt(BundleKey.PAGE_TYPE) == ZhihuViewModel.TYPE_COLLECTION ? "收藏" : "问题"));
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }
}
