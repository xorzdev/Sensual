package gavin.sensual.app.meizitu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
class MeizituPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = new String[]{
            "首页",
            "性感",
            "私房",
            "清纯",
            "萌妹子",
            "小清新",
            "女神",
            "气质",
            "模特",
            "比基尼",
            "宝贝",
            "萝莉",
            "90后",
            "日韩",
    };

    private String[] type = new String[]{
            "",
            "xinggan_2",
            "sifang_5",
            "qingchun_3",
            "meizi_4",
            "xiaoqingxin_6",
            "nvshen_7",
            "qizhi_8",
            "mote_9",
            "bijini_10",
            "baobei_11",
            "luoli_12",
            "wangluo_13",
            "rihan_14",
    };

    MeizituPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return MeizituFragment.newInstance(type[position]);
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
