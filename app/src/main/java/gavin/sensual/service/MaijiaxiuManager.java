package gavin.sensual.service;

import android.support.v4.app.Fragment;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.app.capture.maijiaxiu.Maijiaxiu;
import gavin.sensual.app.common.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import gavin.sensual.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * MaijiaxiuManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MaijiaxiuManager extends BaseManager implements DataLayer.MaijiaxiuService {

    @Override
    public Observable<Image> getPic2(Fragment fragment) {
        return getApi().getMaijiaxiu()
                .map(ResponseBody::string)
                .map(s -> {
                    List<String> ss = new ArrayList<>();
                    ss.add(s);
                    ss.add(s.substring(s.indexOf("<script>window.metaData={"), s.indexOf("}</script>")));
                    return ss;
                })
                .flatMap(Observable::fromIterable)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String str) throws Exception {
                        if (!str.startsWith("<script>window.metaData={")) {
                            return Observable.just(str)
                                    .map(Jsoup::parse)
                                    .map(document -> document.select("div[class=section-main] div[class=main] li[class=item] img"))
                                    .flatMap(Observable::fromIterable)
                                    .map(element -> element.attr("data-original"))
                                    .filter(s -> s.length() > 6)
                                    .map(s -> s.substring(0, s.lastIndexOf("_")));
                        } else {
                            return Observable.just(str)
                                    .map(s -> s.substring(s.indexOf("{")))
                                    .map(s -> s + "}")
                                    .map(s -> JsonUtil.toObj(s, Maijiaxiu.class))
                                    .map(Maijiaxiu::getList)
                                    .flatMap(Observable::fromIterable)
                                    .map(Maijiaxiu.Model::getSrc)
                                    .filter(s -> s.length() > 6);
                        }
                    }
                })
                .map(s -> "https:" + s)
                .map(s -> Image.newImage(fragment, s));
    }
}
