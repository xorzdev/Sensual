package gavin.sensual.test.multi;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.app.setting.BigImageMultiFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestFragImageBinding;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 多图选择
 *
 * @author gavin.xiong 2017/4/25
 */
public class TestFragment extends BindingFragment<TestFragImageBinding> {

    private final int MAX_CHOOSE_COUNT = 9;
    private final int MAX_LATELY_COUNT = 50;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ArrayList<String> mSelectedImagePath = new ArrayList<>();

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_frag_image;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        subscribeEvent();
        init();
        showImage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.dispose();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(TestCheckedChangedEvent.class)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (!event.isChecked) {
                        mSelectedImagePath.remove(event.path);
                    } else if (mSelectedImagePath.size() < MAX_CHOOSE_COUNT) {
                        mSelectedImagePath.add(event.path);
                    } else {
                        Snackbar.make(binding.recycler, String.format("最多选择 %s 张图片", MAX_CHOOSE_COUNT), Snackbar.LENGTH_LONG).show();
                    }
                    binding.includeToolbar.toolbar.setTitle(String.format("已选择(%s/%s)", mSelectedImagePath.size(), MAX_CHOOSE_COUNT));
                    binding.includeToolbar.toolbar.getMenu().findItem(R.id.actionView).setVisible(mSelectedImagePath.size() > 0);
                });
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle(String.format("已选择(%s/%s)", mSelectedImagePath.size(), MAX_CHOOSE_COUNT));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.includeToolbar.toolbar.inflateMenu(R.menu.action_done);
        binding.includeToolbar.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.actionDone:
                    L.e(mSelectedImagePath.toString());
                    return true;
                case R.id.actionView:
                    RxBus.get().post(new StartFragmentEvent(BigImageMultiFragment.newInstance(mSelectedImagePath, 0)));
                    return true;
                default:
                    return false;
            }
        });
    }

    /**
     * 显示图片
     */
    private void showImage() {
        queryImageLately()
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> {
                    ImageAdapter adapter = new ImageAdapter(_mActivity, images);
                    adapter.setCountAndData(MAX_CHOOSE_COUNT, mSelectedImagePath);
                    binding.recycler.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                    binding.recycler.setAdapter(adapter);
                }, L::e);
    }

    /**
     * 按时间获取最近 50 张图片
     */
    private Observable<List<Image>> queryImageLately() {
        return Observable.just(_mActivity)
                .map(Context::getContentResolver)
                .map(resolver -> resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC"))
                .filter(cursor -> cursor != null)
                .map(cursor -> {
                    try {
                        List<Image> images = new ArrayList<>();
                        while (cursor.moveToNext() && cursor.getPosition() < MAX_LATELY_COUNT) {
                            Image image = new Image();
                            image.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            images.add(image);
                        }
                        return images;
                    } finally {
                        cursor.close();
                    }
                });
    }

}
