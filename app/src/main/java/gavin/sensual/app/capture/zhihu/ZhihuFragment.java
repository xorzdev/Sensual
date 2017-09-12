package gavin.sensual.app.capture.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 发现
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuFragment extends BindingFragment<LayoutPagingToolbarBinding, ZhihuViewModel> {

    private int pageType;

    public static ZhihuFragment newInstance(int pageType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKey.PAGE_TYPE, pageType);
        ZhihuFragment fragment = new ZhihuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new ZhihuViewModel(getContext(), this, mBinding, getArguments().getInt(BundleKey.PAGE_TYPE));
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        pageType = getArguments().getInt(BundleKey.PAGE_TYPE);

        mBinding.includeToolbar.toolbar.setTitle(String.format("知乎看图 - %s",
                pageType == ZhihuViewModel.TYPE_COLLECTION ? "收藏" : "问题"));
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        mBinding.includeToolbar.toolbar.inflateMenu(R.menu.action_search);

        SearchView searchView = (SearchView) mBinding.includeToolbar.toolbar.getMenu().findItem(R.id.actionSearch).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint(String.format("请输入%s id", pageType == ZhihuViewModel.TYPE_COLLECTION ? "收藏" : "问题"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    start(ZhihuDetailsFragment.newInstance(pageType, Long.parseLong(query)));
                } catch (NumberFormatException e) {
                    Snackbar.make(mBinding.recycler, "请输入正确 id", Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
    }
}
