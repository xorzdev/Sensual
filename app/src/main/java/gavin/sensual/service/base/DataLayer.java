package gavin.sensual.service.base;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import io.reactivex.Observable;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private DailyService mDailyService;

    public DataLayer(DailyService dailyService) {
        mDailyService = dailyService;
    }

    public DailyService getDailyService() {
        return mDailyService;
    }

    public interface DailyService {

        /**
         * 获取最新日报新闻列表
         *
         * @return Daily
         */
        Observable<Daily> getDaily(int dayDiff);

        /**
         * 获取新闻
         *
         * @param newsId long
         * @return News
         */
        Observable<News> getNews(long newsId);

        /**
         * 缓存今日热文列表
         *
         * @param daily Daily
         */
        void cacheDaily(final Daily daily);
    }
}
