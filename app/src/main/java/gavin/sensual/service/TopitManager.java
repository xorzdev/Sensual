package gavin.sensual.service;

import java.util.List;

import gavin.sensual.app.capture.Capture;
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
    public Observable<List<Capture>> getList() {
        return getApi().getTopitAlbumList();
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
