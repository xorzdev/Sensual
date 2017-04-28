package gavin.sensual.service.base;

import gavin.sensual.app.daily.News;
import gavin.sensual.app.daily.TodayNews;
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
         * @return TodayNews
         */
        Observable<TodayNews> getTodayNews();

        /**
         * 获取新闻
         *
         * @param newsId long
         * @return News
         */
        Observable<News> getNews(long newsId);

        /**
         * 获取本地新闻
         *
         * @param id string
         * @return News
         */
        Observable<News> getLocalNews(final String id);


        /**
         * 获取本地今日热文
         *
         * @return TodayNews
         */
        Observable<TodayNews> getLatestTodayNews();


        /**
         * 缓存新闻
         *
         * @param news News
         */
        void cacheNews(final News news);


        /**
         * 缓存今日热文列表
         *
         * @param todayNews TodayNews
         */
        void cacheTodayNews(final TodayNews todayNews);
    }
}
