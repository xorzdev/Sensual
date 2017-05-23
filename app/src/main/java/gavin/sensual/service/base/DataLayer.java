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
    private MzituService mMzituService;
    private MeizituService mMeizituService;

    public DataLayer(DailyService dailyService,
                     GankService gankService,
                     DoubanService doubanService,
                     ZhihuPicService zhihuPicService,
                     MzituService mzituService,
                     MeizituService meizituService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mDoubanService = doubanService;
        mZhihuPicService = zhihuPicService;
        mMzituService = mzituService;
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

    public MzituService getMeiziPicService() {
        return mMzituService;
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
    }

    public interface GankService {
        Observable<Image> getImage(Fragment fragment, int limit, int no);
    }

    public interface DoubanService {
        Observable<Image> getRank(Fragment fragment, int page);

        Observable<Image> getShow(Fragment fragment, String type, int page);
    }

    public interface ZhihuPicService {
        Observable<Image> getQuestionPic(Fragment fragment, long question, int limit, int offset);

        Observable<Image> getCollectionPic(Fragment fragment, long question, int offset);
    }

    public interface MzituService {
        Observable<Image> getPic(Fragment fragment, String type, int offset);

        Observable<Image> getPic2(Fragment fragment, String url);
    }

    public interface MeizituService {
        Observable<Image> getPic(Fragment fragment, String type, int offset);
    }
}
