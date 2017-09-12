package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.app.common.banner.BannerFragment;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragDailyBinding;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/8/11
 */
public class DailyFragment extends BindingFragment<FragDailyBinding, DailyViewModel> {

    private int mBannerType;

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mBannerType = hashCode();
        mViewModel = new DailyViewModel(getContext(), this, mBinding, mBannerType);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        loadRootFragment(R.id.bannerHolder, BannerFragment.newInstance(mBannerType));

        mBinding.toolbar.setNavigationOnClickListener(v ->
                RxBus.get().post(new DrawerToggleEvent(true)));
    }
}
