package gavin.sensual.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gavin.sensual.app.douban.Image;
import gavin.sensual.app.gank.Result;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import gavin.sensual.util.ImageLoader;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class GankManager extends BaseManager implements DataLayer.GankService {

    @Override
    public Single<List<Image>> getImage(Fragment fragment, int limit, int no) {
        return getGankApi().getImage(limit, no)
                .map(Result::getResults)
                .flatMap(Observable::fromIterable)
                .map(image -> {
                    try {
                        Bitmap bm = ImageLoader.getBitmap(fragment, image.getUrl());
                        image.setWidth(bm.getWidth());
                        image.setHeight(bm.getHeight());
                        bm.recycle();
                    } catch (InterruptedException | ExecutionException e) {
                        image.setWidth(500);
                        image.setHeight(500);
                    }
                    return image;
                })
                .toList();
    }
}
