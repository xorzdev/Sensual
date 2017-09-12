package gavin.sensual.app.meizitu;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BaseViewModel;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.LayoutTabPagerBinding;

/**
 * meizitu
 *
 * @author gavin.xiong 2017/8/15
 */
public class MeizituTabFragment extends BindingFragment<LayoutTabPagerBinding, BaseViewModel> {

    public static MeizituTabFragment newInstance() {
        return new MeizituTabFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab_pager;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        mBinding.includeToolbar.toolbar.setTitle("妹子图 - meizitu");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v ->
                RxBus.get().post(new DrawerToggleEvent(true)));
        initViewPager();
    }

    private void initViewPager() {
        MeizituPagerAdapter pagerAdapter = new MeizituPagerAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(5);
        mBinding.includeToolbar.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}
