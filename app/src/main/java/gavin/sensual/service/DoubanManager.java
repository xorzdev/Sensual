package gavin.sensual.service;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import gavin.sensual.app.douban.Image;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class DoubanManager extends BaseManager implements DataLayer.DoubanService {

    @Override
    public Single<List<Image>> getRank(Fragment fragment, int page) {
        return getDoubanApi().getRank(page)
                .map(responseBody -> extractImg(responseBody.string()))
                .flatMap(Observable::fromIterable)
                .map(Image::new)
                .map(image -> {
                    try {
                        Bitmap bm = getBitmap(fragment, image);
                        image.setWidth(bm.getWidth());
                        image.setHeight(bm.getHeight());
                    } catch (InterruptedException | ExecutionException e) {
                        image.setWidth(500);
                        image.setHeight(500);
                    }
                    return image;
                })
                .toList();
    }

    public Single<List<Image>> getQuestion(Fragment fragment, String url) {
        return Observable.just(url)
                .map(connection -> Jsoup.connect(url).get())
                .map(document -> document.select("div[class=List-item] div[class=ContentItem AnswerItem] div[class=RichContent RichContent--unescapable] img"))
                .flatMap(Observable::fromIterable)
                .map(element -> element.attr("src").trim())
                .filter(s -> !s.startsWith("//"))
                .map(Image::new)
                .map(image -> {
                    try {
                        Bitmap bm = getBitmap(fragment, image);
                        image.setWidth(bm.getWidth());
                        image.setHeight(bm.getHeight());
                    } catch (InterruptedException | ExecutionException e) {
                        image.setWidth(500);
                        image.setHeight(500);
                    }
                    return image;
                })
                .toList();
    }

    @Override
    public Single<List<Image>> getShow(Fragment fragment, String type, int page) {
        return getDoubanApi().getShow(type, page)
                .map(responseBody -> extractImg(responseBody.string()))
                .flatMap(Observable::fromIterable)
                .map(Image::new)
                .map(image -> {
                    try {
                        Bitmap bm = getBitmap(fragment, image);
                        image.setWidth(bm.getWidth());
                        image.setHeight(bm.getHeight());
                    } catch (InterruptedException | ExecutionException e) {
                        image.setWidth(500);
                        image.setHeight(500);
                    }
                    return image;
                })
                .toList();
    }

    /**
     * 提取图片地址
     */
    private List<String> extractImg(String htmlString) throws Exception {
        List<String> images = new ArrayList<>();
        Document document = Jsoup.parse(htmlString);
        Elements elements = document.select("div[class=thumbnail] div[class=img_single] img");
        for (Element element : elements) {
            images.add(element.attr("src").trim());
        }
        return images;
    }

    /**
     * 获取 bitmap 对象
     */
    private Bitmap getBitmap(Fragment fragment, Image t) throws InterruptedException, ExecutionException {
        return Glide.with(fragment)
                .load(t.getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

}
