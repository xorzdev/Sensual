package gavin.sensual.app.douban;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
// TODO: 2017/5/10 FragmentPagerAdapter or FragmentStatePagerAdapter
class DoubanPagerAdapter extends FragmentPagerAdapter {

    private static final String TYPE_BREAST = "2";
    private static final String TYPE_BUTT = "6";
    private static final String TYPE_SILK = "7";
    private static final String TYPE_LEG = "3";
    private static final String TYPE_RANK = "5";

    private String[] tabs = new String[]{
            "Rank",
            "BREAST",
            "LEG",
            "BUTT",
            "SILK",
    };

    DoubanPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DoubanFragment.newInstance(null);
            case 1:
                return DoubanFragment.newInstance(TYPE_BREAST);
            case 2:
                return DoubanFragment.newInstance(TYPE_LEG);
            case 3:
                return DoubanFragment.newInstance(TYPE_BUTT);
            case 4:
                return DoubanFragment.newInstance(TYPE_SILK);
            case 5:
                return DoubanFragment.newInstance(TYPE_RANK);
            default:
                return null;
        }
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
