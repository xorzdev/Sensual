package gavin.sensual.service;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

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
public class MeizituManager extends BaseManager implements DataLayer.MeizituService {

    @Override
    public Observable<Image> getPic(Fragment fragment, String type, int offset) {
        return TextUtils.isEmpty(type)
                ? getHome(fragment)
                : getType(fragment, type, offset);
    }

    private Observable<Image> getHome(Fragment fragment) {
        return getMeizituAPI().getHome()
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[id=maincontent] div[class=postContent] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }

    private Observable<Image> getType(Fragment fragment, String type, int offset) {
        return getMeizituAPI().get(type, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[id=maincontent] li[class=wp-item] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }

}
