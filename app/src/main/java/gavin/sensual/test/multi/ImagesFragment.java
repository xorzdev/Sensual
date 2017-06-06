package gavin.sensual.test.multi;

import android.content.Context;
import android.database.Cursor;
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
import gavin.sensual.util.JsonUtil;
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
public class ImagesFragment extends BindingFragment<TestFragImageBinding> {

    private final int MAX_CHOOSE_COUNT = 9;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Image mFolder;
    private ArrayList<String> mSelectedImagePath = new ArrayList<>();

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
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
        RxBus.get().toObservable(FolderChangedEvent.class)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    mFolder = event.image;
                    binding.tvDir.setText(mFolder.getParent());
                    showImage();
                });
        RxBus.get().toObservable(ImageCheckedChangedEvent.class)
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
                    Bundle bundle = new Bundle();
                    bundle.putString("result", JsonUtil.toJson(mSelectedImagePath));
                    setFragmentResult(RESULT_OK, bundle);
                    pop();
                    return true;
                case R.id.actionView:
                    RxBus.get().post(new StartFragmentEvent(BigImageMultiFragment.newInstance(mSelectedImagePath, 0)));
                    return true;
                default:
                    return false;
            }
        });

        binding.tvDir.setOnClickListener(v -> new FoldersDialog(_mActivity, mFolder == null ? null : mFolder.getParentId()).show());
    }

    /**
     * 显示图片
     */
    private void showImage() {
        getImageSrc()
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

    private Observable<List<Image>> getImageSrc() {
        return mFolder == null || mFolder.getParentId() == null ? queryImageLately() : queryImageByFolder();
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
                .map(this::getImageList);
    }

    /**
     * 获取指定文件夹下的所有图片
     */
    private Observable<List<Image>> queryImageByFolder() {
        return Observable.just(_mActivity)
                .map(Context::getContentResolver)
                .map(resolver -> resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.DATA},
                        MediaStore.Images.Media.BUCKET_ID + " = ? ",
                        new String[]{String.valueOf(mFolder.getParentId())},
                        MediaStore.Images.Media.DATE_ADDED + " DESC"
                ))
                .filter(cursor -> cursor != null)
                .map(this::getImageList);
    }

    private List<Image> getImageList(Cursor cursor) {
        try {
            List<Image> images = new ArrayList<>();
            while (cursor.moveToNext()) {
                Image image = new Image();
                image.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                for (String s : mSelectedImagePath) {
                    if (s.equals(image.getPath())) {
                        image.setChecked(true);
                        break;
                    }
                }
                images.add(image);
            }
            return images;
        } finally {
            cursor.close();
        }
    }

}
