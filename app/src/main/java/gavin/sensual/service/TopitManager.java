package gavin.sensual.service;

import android.support.v4.app.Fragment;

import gavin.sensual.app.capture.topit.Album;
import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;

/**
 * TopitManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class TopitManager extends BaseManager implements DataLayer.TopitService {

    @Override
    public Observable<Image> getList(Fragment fragment) {
        return getApi().getTopitAlbumList()
                .flatMap(Observable::fromIterable)
                .map(capture -> {
                    Image image = Image.newImage(fragment, capture.getImage());
                    image.setId(String.valueOf(capture.getId()));
                    image.haveMore = false;
                    return image;
                });
    }

    @Override
    public Observable<Image> getAlbum(long id, int offset) {
        return getApi().getTopitAlbum("album.get", id, 10, offset * 10)
                .map(Album::getItem)
                .flatMap(Observable::fromIterable)
                .map(Album.Item::getIcon)
                .map(icon -> {
                    Image image = new Image();
                    image.setUrl(icon.getUrl());
                    image.setWidth(icon.getWidth());
                    image.setHeight(icon.getHeight());
                    return image;
                });
    }
}
