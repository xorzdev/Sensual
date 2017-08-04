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
public class DoubanManager extends BaseManager implements DataLayer.DoubanService {

    @Override
    public Observable<Image> getRank(Fragment fragment, int page) {
        return getApi().getDoubanRank(page)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=thumbnail] div[class=img_single] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }

    @Override
    public Observable<Image> getShow(Fragment fragment, String type, int page) {
        return getApi().getDoubanShow(type, page)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=thumbnail] div[class=img_single] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }
}
