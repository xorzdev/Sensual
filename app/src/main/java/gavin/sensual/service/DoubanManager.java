package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;

import java.util.List;

import gavin.sensual.app.douban.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class DoubanManager extends BaseManager implements DataLayer.DoubanService {

    @Override
    public Single<List<Image>> getRank(Fragment fragment, int page) {
        return getDoubanApi().getRank(page)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document ->  document.select("div[class=thumbnail] div[class=img_single] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s))
                .toList();
    }

    @Override
    public Single<List<Image>> getShow(Fragment fragment, String type, int page) {
        return getDoubanApi().getShow(type, page)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document ->  document.select("div[class=thumbnail] div[class=img_single] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s))
                .toList();
    }
}
