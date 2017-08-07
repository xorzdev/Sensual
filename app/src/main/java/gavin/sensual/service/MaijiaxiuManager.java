package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;

import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MaijiaxiuManager extends BaseManager implements DataLayer.MaijiaxiuService {

    @Override
    public Observable<Image> getPic(Fragment fragment, int offset) {
        return getApi().getMaijiaxiu()
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=section-main] div[class=main] li[class=item] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-original"))
                .filter(s -> s.length() > 6)
                .map(s -> s.substring(0, s.lastIndexOf("_")))
                .map(s -> "https:" + s)
                .map(s -> Image.newImage(fragment, s));
    }
}
