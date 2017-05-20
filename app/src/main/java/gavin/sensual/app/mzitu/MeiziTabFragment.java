package gavin.sensual.app.mzitu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDoubanTabBinding;

/**
 * 豆瓣妹子
 *
 * @author gavin.xiong 2017/5/10
 */
public class MeiziTabFragment extends BindingFragment<FragDoubanTabBinding> {

    public static MeiziTabFragment newInstance() {
        return new MeiziTabFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_douban_tab;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle("妹子图 - mzitu");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> RxBus.get().post(new DrawerToggleEvent(true)));
        initViewPager();
    }

    private void initViewPager() {
        MeiziPagerAdapter pagerAdapter = new MeiziPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(5);
        binding.includeToolbar.tabLayout.setupWithViewPager(binding.viewPager);
        binding.includeToolbar.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
