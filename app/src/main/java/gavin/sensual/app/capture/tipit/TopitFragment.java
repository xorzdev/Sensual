package gavin.sensual.app.capture.tipit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import gavin.sensual.BR;
import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.LayoutPagingToolbarBinding;

/**
 * 发现
 *
 * @author gavin.xiong 2017/5/10
 */
public class TopitFragment extends BindingFragment<LayoutPagingToolbarBinding, TopitViewModel> {

    public static TopitFragment newInstance() {
        return new TopitFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new TopitViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVariable(BR.vm, mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_paging_toolbar;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("Topit");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        mBinding.includeToolbar.toolbar.inflateMenu(R.menu.action_search);

        SearchView searchView = (SearchView) mBinding.includeToolbar.toolbar.getMenu().findItem(R.id.actionSearch).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("请输入精选集 id");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    start(TopitDetailsFragment.newInstance(Long.parseLong(query)));
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
