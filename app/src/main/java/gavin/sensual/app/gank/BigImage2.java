package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import gavin.sensual.R;
import gavin.sensual.app.douban.Image;
import gavin.sensual.app.setting.BigImageSingleFragment;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.FragBigImageMultiBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 查看大图 - photoView - 多图模式
 *
 * @author gavin.xiong 2017/2/28
 */
public class BigImage2 extends BindingFragment<FragBigImageMultiBinding> implements ViewPager.OnPageChangeListener {

    private SharedPager<Image> sharedPager;
    private boolean haveMore = true;
    private boolean loadingMore = false;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static BaseFragment newInstance(SharedPager<Image> sharedPager) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.SHARED_PAGER, sharedPager);
        BaseFragment fragment = new BigImage2();
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        sharedPager.index = position;
        if (haveMore && !loadingMore && position == sharedPager.list.size() - 4) {
            getImage();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.viewPager.removeOnPageChangeListener(this);
        compositeDisposable.dispose();
    }

    private void init() {
        sharedPager = (SharedPager<Image>) getArguments().getSerializable(BundleKey.SHARED_PAGER);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(sharedPager.index);
        binding.viewPager.addOnPageChangeListener(this);
    }

    private void getImage() {
        getDataLayer().getGankService().getImage(this, 10, sharedPager.no + 1)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    loadingMore = true;
                    sharedPager.no++;
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(arg0 -> loadingMore = false)
                .doOnError(throwable -> {
                    loadingMore = false;
                    sharedPager.no--;
                })
                .subscribe(result -> {
                    haveMore = sharedPager.limit == result.size();
                    sharedPager.list.addAll(result);
                    binding.viewPager.getAdapter().notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return sharedPager.list == null ? 0 : sharedPager.list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return BigImageSingleFragment.newInstance(sharedPager.list.get(position).getUrl(), true);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
