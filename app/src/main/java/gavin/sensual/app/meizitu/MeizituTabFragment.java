package gavin.sensual.app.meizitu;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
public class MeizituTabFragment extends BindingFragment<FragDoubanTabBinding> {

    public static MeizituTabFragment newInstance() {
        return new MeizituTabFragment();
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
        binding.includeToolbar.toolbar.setTitle("妹子图 - meizitu");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> RxBus.get().post(new DrawerToggleEvent(true)));
        initViewPager();
    }

    private void initViewPager() {
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(5);
        binding.includeToolbar.tabLayout.setupWithViewPager(binding.viewPager);
    }
}
