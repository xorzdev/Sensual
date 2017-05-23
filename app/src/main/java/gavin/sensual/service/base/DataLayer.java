package gavin.sensual.service.base;

import android.support.v4.app.Fragment;

import gavin.sensual.app.common.Image;
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
    private GankService mGankService;
    private DoubanService mDoubanService;
    private ZhihuPicService mZhihuPicService;
    private MeiziPicService mMeiziPicService;
    private MeizituService mMeizituService;

    public DataLayer(DailyService dailyService,
                     GankService gankService,
                     DoubanService doubanService,
                     ZhihuPicService zhihuPicService,
                     MeiziPicService meiziPicService,
                     MeizituService meizituService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mDoubanService = doubanService;
        mZhihuPicService = zhihuPicService;
        mMeiziPicService = meiziPicService;
        mMeizituService = meizituService;
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

    public MeiziPicService getMeiziPicService() {
        return mMeiziPicService;
    }

    public MeizituService getMeizituService() {
        return mMeizituService;
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
        Observable<Image> getImage(Fragment fragment, int limit, int no);
    }

    public interface DoubanService {
        Observable<Image> getRank(Fragment fragment, int page);

        Observable<Image> getShow(Fragment fragment, String type, int page);
    }

    public interface ZhihuPicService {
        Observable<Image> getPic(Fragment fragment, long question, int limit, int offset);
    }

    public interface MeiziPicService {
        Observable<Image> getPic(Fragment fragment, String type, int offset);

        Observable<Image> getPic2(Fragment fragment, String url);
    }

    public interface MeizituService {
        Observable<Image> getPic(Fragment fragment, String type, int offset);
    }
}
