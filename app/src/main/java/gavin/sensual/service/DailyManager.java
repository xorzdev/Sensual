package gavin.sensual.service;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
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
    public Observable<Daily> getDaily() {
        return getApi().getDaily();
    }

    @Override
    public Observable<News> getNews(long newsId) {
        return getApi().getNews(newsId);
    }

    @Override
    public void cacheDaily(final Daily daily) {
        SPUtil.saveString(BundleKey.LATEST_DATE, daily.getDate());
        SPUtil.saveString(daily.getDate(), getGson().toJson(daily));
    }
}
