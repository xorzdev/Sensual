package gavin.sensual.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gavin.sensual.app.douban.Image;
import gavin.sensual.app.gank.Result;
import gavin.sensual.app.gank.Welfare;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class GankManager extends BaseManager implements DataLayer.GankService {

    @Override
    public Single<List<Welfare>> getWelfare(Fragment fragment, int limit, int no) {
        return getGankApi().getWelfare(limit, no)
                .map(Result::getResults)
                .flatMap(Observable::fromIterable)
                .map(welfare -> {
                    try {
                        Bitmap bm = getBitmap(fragment, welfare);
                        welfare.setWidth(bm.getWidth());
                        welfare.setHeight(bm.getHeight());
                        bm.recycle();
                    } catch (InterruptedException | ExecutionException e) {
                        welfare.setWidth(500);
                        welfare.setHeight(500);
                    }
                    return welfare;
                })
                .toList();
    }
    /**
     * 获取 bitmap 对象
     */
    private Bitmap getBitmap(Fragment fragment, Welfare t) throws InterruptedException, ExecutionException {
        return Glide.with(fragment)
                .load(t.getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }
}
