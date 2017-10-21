package gavin.sensual.service;

import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.app.capture.Capture;
import gavin.sensual.app.capture.topit.Album;
import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import gavin.sensual.util.L;
import io.reactivex.Observable;

/**
 * TopitManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class TopitManager extends BaseManager implements DataLayer.TopitService {

    private int num = 0;
    private List<Long> excludes;

    private Observable<Boolean> getExcludeAlbumList() {
        return getApi().getExcludeAlbumList()
                .flatMap(Observable::fromIterable)
                .map(Capture::getId)
                .toList()
                .toObservable()
                .map(longs -> {
                    excludes = longs;
                    return true;
                });
    }

    @Override
    public Observable<Image> getList(Fragment fragment, int offset) {
        return Observable.just(0)
                .flatMap(arg0 -> {
                    if (excludes == null) {
                        return getExcludeAlbumList();
                    } else {
                        return Observable.just(false);
                    }
                })
                .flatMap(arg0 -> getApi().getTopitAlbum("user.getAlbums", 5497642, 15, offset * 15))
                .map(album -> {
                    num = album.getInfo().getNum();
                    return album.getItem();
                })
                .flatMap(Observable::fromIterable)
                .filter(item -> !excludes.contains(item.getId()))
                .map(item -> {
                    Image image = null;
                    if (item.getIcon().getUrl().endsWith("m.jpg")) {
                        image = Image.newImage(fragment, item.getIcon().getUrl().replace("m.jpg", "l.jpg"));
                    } else if (item.getIcon().getUrl().contains("topitme.com/m")) {
                        image = Image.newImage(fragment, item.getIcon().getUrl().replace("topitme.com/m", "topitme.com/l"));
                    } else {
                        L.e("topitme封面规则不匹配 - " + item.getIcon().getUrl());
                        image = new Image();
                        image.setUrl(item.getIcon().getUrl());
                        image.setWidth(item.getIcon().getWidth());
                        image.setHeight(item.getIcon().getHeight());
                    }
                    image.setId(String.valueOf(item.getId()));
                    image.haveMore = offset * 15 + 15 < num;
                    return image;
                });

    }

    @Override
    public Observable<Image> getAlbum(long id, int offset) {
        return getApi().getTopitAlbum("album.get", id, 15, offset * 15)
                .map(album -> {
                    num = album.getInfo().getNum();
                    return album.getItem();
                })
                .flatMap(Observable::fromIterable)
                .map(Album.Item::getIcon)
                .map(icon -> {
                    Image image = new Image();
                    image.setUrl(icon.getUrl());
                    image.setWidth(icon.getWidth());
                    image.setHeight(icon.getHeight());
                    image.haveMore = offset * 15 + 15 < num;
                    return image;
                });
    }
}
