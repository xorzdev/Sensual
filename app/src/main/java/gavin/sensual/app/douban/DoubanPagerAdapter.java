package gavin.sensual.app.douban;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
class DoubanPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = new String[]{
            "RANDOM",
            "RANK",
            "BREAST",
            "BUTT",
            "SILK",
            "LEG",
            "BEAUTY",
            "HODGEPODGE",
    };

    private String[] type = new String[]{
            "0",
            "",
            "2",
            "6",
            "7",
            "3",
            "4",
            "5",
    };

    DoubanPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return DoubanFragment.newInstance(type[position]);
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
