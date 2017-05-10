package gavin.sensual.app.douban;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragDoubanTabBinding;
import gavin.sensual.util.L;

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

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        L.e("onSupportVisible");
        if (Build.VERSION.SDK_INT >= 21) {
            _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        L.e("onSupportInvisible");
        if (Build.VERSION.SDK_INT >= 21) {
            _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, android.R.color.transparent));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        L.e("onHiddenChanged - " + hidden);
        if (Build.VERSION.SDK_INT >= 21) {
            if (hidden) {
                _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, android.R.color.transparent));
            } else {
                _mActivity.getWindow().setStatusBarColor(ContextCompat.getColor(_mActivity, R.color.colorPrimaryDark));
            }
        }
    }

    private void init() {
        binding.toolbar.setTitle("豆瓣妹子");
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> pop());
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
