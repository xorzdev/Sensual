package gavin.sensual.app.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.FragBigImageMultiBinding;

/**
 * 查看大图 - photoView - 多图模式
 *
 * @author gavin.xiong 2017/2/28
 */
public class BigImageMultiFragment extends BindingFragment<FragBigImageMultiBinding> {

    private List<String> urlList;

    public static BaseFragment newInstance(ArrayList<String> imageUrl, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.IMAGE_URL, imageUrl);
        bundle.putInt(BundleKey.POSITION, position);
        BaseFragment fragment = new BigImageMultiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_big_image_multi;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        urlList = (List<String>) getArguments().getSerializable(BundleKey.IMAGE_URL);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(getArguments().getInt(BundleKey.POSITION));
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return urlList == null ? 0 : urlList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BigImageSingleFragment.newInstance(urlList.get(position));
        }
    }
}
