package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

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
    public Observable<Integer> getPageCount() {
        return getJiandanAPI().get("")
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=comments] div[class=cp-pagenavi] span[class=current-comment-page]"))
                .map(elements -> elements.get(0))
                .map(Element::html)
                .map(s -> s.substring(1, s.length() - 1))
                .map(Integer::parseInt);
    }

    @Override
    public Observable<Image> getPic(Fragment fragment, int offset) {
        return getJiandanAPI().get("page-" + offset + "")
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("ol[class=commentlist] div[class=row] a[class=view_img_link]"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("href"))
                .map(s -> "http:" + s)
                .map(s -> Image.newImage(fragment, s));
    }

}
