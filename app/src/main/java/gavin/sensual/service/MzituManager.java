package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import gavin.sensual.util.L;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MzituManager extends BaseManager implements DataLayer.MzituService {

    @Override
    public Observable<Integer> getPageCount() {
        return getMzituAPI().getZipai("")
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[id=comments] div[class=pagenavi-cm] span[class=page-numbers current]"))
                .map(elements -> elements.get(0))
                .map(Element::html)
                .map(Integer::parseInt);
    }

    @Override
    public Observable<Image> getZipai(Fragment fragment, int offset) {
        return getMzituAPI().getZipai(String.format("comment-page-%s", offset))
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=comment-body] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src"))
                .map(s -> Image.newImage(fragment, s));
    }

    @Override
    public Observable<Image> getTypeOther(Fragment fragment, String type, int offset) {
        return getMzituAPI().getOther(type, offset)
                .map(ResponseBody::string)
                .map(Jsoup::parse)
                .map(document -> document.select("div[class=main-content] div[class=postlist] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("data-original"))
                .map(s -> s
                        .substring(0, s.lastIndexOf("/") + 1).replaceAll("thumbs/", "")
                        .concat(s.substring(s.indexOf("_") + 1, s.lastIndexOf("_")))
                        .concat(s.substring(s.lastIndexOf("."))))
                .map(s -> {
                    L.e(s);
                    return s;
                })
                .map(s -> Image.newImage(fragment, s));
    }

    private boolean flag = false;

    @Override
    public Observable<Image> getImageRange(Fragment fragment, String url) {
        return Observable.just(url)
                .map(s -> s.substring(0, s.lastIndexOf(".") - 2).concat("%02d").concat(s.substring(s.lastIndexOf("."))))
                .flatMap(s -> Observable.range(1, 99).map(i -> String.format(s, i)))
                .map(s -> Image.newImage(fragment, s))
                .takeUntil(image -> {
                    // 允许图片连续不存在的最大间隔为1
                    if (image.isError()) {
                        flag = !flag;
                        return !flag;
                    } else {
                        flag = false;
                    }
                    return false;
                })
                .filter(image -> !image.isError());
    }
}
