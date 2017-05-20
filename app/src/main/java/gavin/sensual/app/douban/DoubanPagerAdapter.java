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

    private static final String TYPE_RANK = "";
    private static final String TYPE_BREAST = "2";
    private static final String TYPE_BUTT = "6";
    private static final String TYPE_SILK = "7";
    private static final String TYPE_LEG = "3";
    private static final String TYPE_BEAUTY = "4";
    private static final String TYPE_OTHER = "5";

    private String[] tabs = new String[]{
            "福利",
            "大胸妹",
            "小翘臀",
            "黑丝袜",
            "美腿控",
            "有颜值",
            "大杂烩",
    };

    DoubanPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DoubanFragment.newInstance(TYPE_RANK);
            case 1:
                return DoubanFragment.newInstance(TYPE_BREAST);
            case 2:
                return DoubanFragment.newInstance(TYPE_BUTT);
            case 3:
                return DoubanFragment.newInstance(TYPE_SILK);
            case 4:
                return DoubanFragment.newInstance(TYPE_LEG);
            case 5:
                return DoubanFragment.newInstance(TYPE_BEAUTY);
            case 6:
                return DoubanFragment.newInstance(TYPE_OTHER);
            default:
                return DoubanFragment.newInstance(null);
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
