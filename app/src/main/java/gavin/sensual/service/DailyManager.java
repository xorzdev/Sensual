package gavin.sensual.service;

import gavin.sensual.app.daily.News;
import gavin.sensual.app.daily.TodayNews;
import gavin.sensual.base.BundleKey;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import gavin.sensual.util.SPUtil;
import io.reactivex.Observable;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class DailyManager extends BaseManager implements DataLayer.DailyService {

    @Override
    public Observable<TodayNews> getTodayNews() {
        return getApi().getTodayNews();
    }

    @Override
    public Observable<News> getNews(long newsId) {
        return getApi().getNews(newsId);
    }

    @Override
    public void cacheTodayNews(final TodayNews todayNews) {
        SPUtil.saveString(BundleKey.LATEST_DATE, todayNews.getDate());
        SPUtil.saveString(todayNews.getDate(), getGson().toJson(todayNews));
    }

    @Override
    public void cacheNews(final News news) {
        SPUtil.saveString(String.valueOf(news.getId()), getGson().toJson(news));
    }

    @Override
    public Observable<TodayNews> getLatestTodayNews() {
        return Observable.create(subscriber -> {
//                    subscriber.onStart();
            String latestDate = SPUtil.getString(BundleKey.LATEST_DATE);
            String json = SPUtil.getString(latestDate);
            TodayNews todayNews = getGson().fromJson(json, TodayNews.class);
            subscriber.onNext(todayNews);
            subscriber.onComplete();
        });
    }

    @Override
    public Observable<News> getLocalNews(final String id) {
        return Observable.create(subscriber -> {
//                    subscriber.onStart();
            String json = SPUtil.getString(id);
            News news = getGson().fromJson(json, News.class);
            subscriber.onNext(news);
            subscriber.onComplete();
        });
    }
}
