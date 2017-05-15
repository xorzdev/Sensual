package gavin.sensual.app.douban;

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
public class DoubanTabFragment extends BindingFragment<FragDoubanTabBinding> {

    public static DoubanTabFragment newInstance() {
        return new DoubanTabFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_douban_tab;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

//    @Override
//    public void onSupportVisible() {
//        L.e("onSupportVisible");
//        super.onSupportVisible();
//        if (Build.VERSION.SDK_INT >= 21) {
//            _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, R.color.colorPrimaryDark));
//        }
//    }
//
//    @Override
//    public void onSupportInvisible() {
//        L.e("onSupportInvisible");
//        super.onSupportInvisible();
//        if (Build.VERSION.SDK_INT >= 21) {
//            _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, android.R.color.transparent));
//        }
//    }

    private void init() {
        binding.toolbar.setTitle("豆瓣妹子");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> RxBus.get().post(new DrawerToggleEvent(true)));
        initViewPager();
    }

    private void initViewPager() {
        DoubanPagerAdapter pagerAdapter = new DoubanPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(5);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
