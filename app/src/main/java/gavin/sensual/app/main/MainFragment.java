package gavin.sensual.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.app.capture.CaptureFragment;
import gavin.sensual.app.collection.CollectionFragment;
import gavin.sensual.app.daily.DailyFragment;
import gavin.sensual.app.douban.DoubanTabFragment;
import gavin.sensual.app.gank.GankFragment;
import gavin.sensual.app.meizitu.MeizituTabFragment;
import gavin.sensual.app.mzitu.MeiziTabFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragMainBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/15
 */
public class MainFragment extends BindingFragment<FragMainBinding> {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIFTH = 4;
    public static final int SIXTH = 5;
    public static final int SEVENTH = 6;
    private int currPoint;

    private SupportFragment[] mFragments = new SupportFragment[7];

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragments[FIRST] = DailyFragment.newInstance();
            mFragments[SECOND] = GankFragment.newInstance();
            mFragments[THIRD] = DoubanTabFragment.newInstance();
            mFragments[FOURTH] = MeiziTabFragment.newInstance();
            mFragments[FIFTH] = MeizituTabFragment.newInstance();
            mFragments[SIXTH] = CaptureFragment.newInstance();
            mFragments[SEVENTH] = CollectionFragment.newInstance();

            currPoint = FIRST;

            loadMultipleRootFragment(R.id.holder, currPoint,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH],
                    mFragments[FIFTH],
                    mFragments[SIXTH],
                    mFragments[SEVENTH]);
        } else {
            mFragments[FIRST] = findChildFragment(DailyFragment.class);
            mFragments[SECOND] = findChildFragment(GankFragment.class);
            mFragments[THIRD] = findChildFragment(DoubanTabFragment.class);
            mFragments[FOURTH] = findChildFragment(MeiziTabFragment.class);
            mFragments[FIFTH] = findChildFragment(MeizituTabFragment.class);
            mFragments[SIXTH] = findChildFragment(CaptureFragment.class);
            mFragments[SEVENTH] = findChildFragment(CollectionFragment.class);

            currPoint = savedInstanceState.getInt(BundleKey.MAIN_CURR_POSITION);
        }

        subscribeEvent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BundleKey.MAIN_CURR_POSITION, currPoint);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(ShowHideFragmentEvent.class)
                .delay(showHideFragmentEvent -> Observable.just(showHideFragmentEvent).delay(showHideFragmentEvent.delay, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (event.position != currPoint) {
                        showHideFragment(mFragments[event.position], mFragments[currPoint]);
                        currPoint = event.position;
                    }
                });

        RxBus.get().toObservable(BackPressedEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (currPoint != FIRST) {
                        showHideFragment(mFragments[FIRST], mFragments[currPoint]);
                        RxBus.get().post(new NavigationItemCheckedEvent(R.id.nav_news));
                        currPoint = FIRST;
                    } else {
                        _mActivity.finish();
                    }
                });
    }
}
