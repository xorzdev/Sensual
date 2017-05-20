package gavin.sensual.app.meizitu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
class PagerAdapter extends FragmentPagerAdapter {

    private String[] type = new String[]{
            "",
            "xinggan_2",
            "japan",
            "taiwan",
            "mm",
            "zipai",
    };

    private String[] tabs = new String[]{
            "首页",
            "性感",
            "首页",
            "首页",
            "首页",
            "首页",
    };

    PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return MeiziFragment.newInstance(type[position]);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
