package gavin.sensual.app.mzitu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
class MzituPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = new String[]{
            "首页",
            "性感",
            "日本",
            "台湾",
            "清纯",
            "自拍",
            "热门",
            "推荐",
            "最新",
    };

    private String[] type = new String[]{
            "",
            "xinggan",
            "japan",
            "taiwan",
            "mm",
            "zipai",
            "hot",
            "best",
            "all",
    };

    MzituPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return MzituFragment.newInstance(type[position]);
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
