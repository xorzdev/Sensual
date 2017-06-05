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
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class ImageFolderFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private List<Image> imageList;

    public static ImageFolderFragment newInstance() {
        return new ImageFolderFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        showImage();
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle("已选择(0/9)");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.includeToolbar.toolbar.inflateMenu(R.menu.action_done);
        binding.includeToolbar.toolbar.setOnMenuItemClickListener(item -> {
            int count = 0;
            for (Image t : imageList) {
                if (t.isChecked()) count++;
            }
            Snackbar.make(binding.recycler, "count - " + count, Snackbar.LENGTH_LONG).show();
            return false;
        });
        binding.refreshLayout.setEnabled(false);
    }

    private void showImage() {
//        queryImageLately()
        queryImageFolder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> {
                    imageList = images;
                    ImageAdapter adapter = new ImageAdapter(_mActivity, images);
                    binding.recycler.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                    binding.recycler.setAdapter(adapter);
                }, L::e);
    }

    /**
     * 按时间获取所有图片
     */
    private Observable<List<Image>> queryImageLately() {
        return Observable.just(_mActivity)
                .map(Context::getContentResolver)
                .map(resolver -> resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{
                                MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.BUCKET_ID,
                                MediaStore.Images.Media.DISPLAY_NAME},
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC"))
                .filter(cursor -> cursor != null)
                .map(this::getImageList);
    }

    /**
     * 按文件位置获取所有包含图片的文件夹列表
     */
    private Observable<List<Image>> queryImageFolder() {
        return Observable.just(_mActivity)
                .map(Context::getContentResolver)
                .map(resolver -> resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.BUCKET_ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                "COUNT(*) AS count"},
                        "0 = 0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID,
                        null,
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
                image.setParentId(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)));
                image.setParent(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                image.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                images.add(image);
            }
            return images;
        } finally {
            cursor.close();
        }
    }
}
