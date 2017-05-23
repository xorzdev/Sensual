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
public class ZhihuPicManager extends BaseManager implements DataLayer.ZhihuPicService {

    @Override
    public Observable<Image> getQuestionPic(Fragment fragment, long id, int limit, int offset) {
        return getZhihuPicApi().getAnswer(id, "data[*].is_normal,content", limit, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("img[data-actualsrc]"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-actualsrc"))
                .filter(s -> s.length() > 6)
                .map(s -> s.substring(2, s.length() - 2))
                .map(s -> Image.newImage(fragment, s));
    }

    @Override
    public Observable<Image> getCollectionPic(Fragment fragment, long id, int offset) {
        return getZhihuPicApi().getCollection(id, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[data-action=/answer/content] textarea[class=content]"))
                .map(elements -> elements.html().replaceAll("&lt;", "<").replaceAll("&gt;", ">"))
                .map(Jsoup::parse)
                .map(document -> document.select("img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .filter(s -> s.length() > 6)
                .map(s -> Image.newImage(fragment, s));
    }
}
