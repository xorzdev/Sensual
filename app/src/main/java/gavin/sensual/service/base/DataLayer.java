package gavin.sensual.service.base;

import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import gavin.sensual.app.douban.Image;
import gavin.sensual.app.gank.Welfare;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private DailyService mDailyService;
    private GankService mGankService;
    private DoubanService mDoubanService;
    private ZhihuPicService mZhihuPicService;

    public DataLayer(DailyService dailyService, GankService gankService, DoubanService doubanService,ZhihuPicService zhihuPicService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mDoubanService = doubanService;
        mZhihuPicService = zhihuPicService;
    }

    public DailyService getDailyService() {
        return mDailyService;
    }

    public GankService getGankService() {
        return mGankService;
    }

    public DoubanService getDoubanService() {
        return mDoubanService;
    }

    public ZhihuPicService getZhihuPicService() {
        return mZhihuPicService;
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

    public interface GankService {

        /**
         * 获取福利
         *
         * @param limit 分页大小
         * @param no    页码
         * @return Result
         */
        Single<List<Welfare>> getWelfare(Fragment fragment, int limit, int no);
    }

    public interface DoubanService {
        Single<List<Image>> getRank(Fragment fragment, int page);

        Single<List<Image>> getShow(Fragment fragment, String type, int page);
    }

    public interface ZhihuPicService {
        Single<List<Image>> getPic(Fragment fragment, long question, int limit, int offset);
    }
}
