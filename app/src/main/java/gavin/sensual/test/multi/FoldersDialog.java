package gavin.sensual.test.multi;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.TestDialogBinding;
import gavin.sensual.util.DisplayUtil;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/6/6
 */
class FoldersDialog extends BottomSheetDialog {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TestDialogBinding binding;

    private Context mContext;
    private Long mSelectedFolderId;

    FoldersDialog(@NonNull Context context, Long selectedFolderId) {
        super(context);
        mContext = context;
        mSelectedFolderId = selectedFolderId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestDialogBinding.inflate(LayoutInflater.from(mContext));
        setContentView(binding.getRoot());
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) (binding.getRoot().getParent()));
        behavior.setPeekHeight((int) (DisplayUtil.getScreenHeight() * (5f / 7f)));

        subscribeEvent();
        initData();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCompositeDisposable.dispose();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(FolderChangedEvent.class)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> dismiss());
    }

    private void initData() {
        queryImageFolder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> {
//                    imageList = images;
                    FolderAdapter adapter = new FolderAdapter(mContext, images);
                    binding.recycler.setAdapter(adapter);
                }, L::e);
    }


    /**
     * 按文件位置获取所有包含图片的文件夹列表
     */
    private Observable<List<Image>> queryImageFolder() {
        return Observable.just(mContext)
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
                .map(this::getImageList)
                .map(this::addLately);
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

    private List<Image> addLately(List<Image> images) {
        Image lately = new Image();
        lately.setParent("全部图片");
        int count = 0;
        boolean flag = false;
        for (Image t : images) {
            count += t.getCount();
            if (t.getParentId().equals(mSelectedFolderId)) {
                t.setChecked(true);
                flag = true;
            }
        }
        lately.setChecked(!flag);
        lately.setCount(count);
        lately.setPath(images.get(0).getPath());
        images.add(0, lately);
        return images;
    }
}
