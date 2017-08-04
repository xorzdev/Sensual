package gavin.sensual.service;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.jsoup.Jsoup;

import java.util.concurrent.TimeUnit;

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
        return getApi().getMeizituHome()
                .delay(500, TimeUnit.MILLISECONDS)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=listmain] div[class=am-gallery-item] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-original"))
                .map(s -> Image.newImage(fragment, s));
    }

    private Observable<Image> getType(Fragment fragment, String type, int offset) {
        return getApi().getMeizitu(type, offset)
                .delay(500, TimeUnit.MILLISECONDS)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=listmain] div[class=am-gallery-item] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-original"))
                .map(s -> Image.newImage(fragment, s));
    }

}
