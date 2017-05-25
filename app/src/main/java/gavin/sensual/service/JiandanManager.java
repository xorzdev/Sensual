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
public class JiandanManager extends BaseManager implements DataLayer.JiandanService {

    @Override
    public Observable<Image> getPic(Fragment fragment, int offset) {
        return getJiandanAPI().get(offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("ol[class=commentlist] div[class=row] a[class=view_img_link]"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("href"))
                .map(s -> "http:" + s)
                .map(s -> Image.newImage(fragment, s));
    }

}
