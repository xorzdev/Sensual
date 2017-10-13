package gavin.sensual.service.base;

import android.support.v4.app.Fragment;

import java.util.List;

import gavin.sensual.app.capture.Capture;
import gavin.sensual.app.common.Image;
import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import gavin.sensual.app.setting.License;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

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
    private JiandanService mJiandanService;
    private MaijiaxiuService mMaijiaxiuService;
    private SettingService mSettingService;
    private TopitService mTopitService;

    public DataLayer(DailyService dailyService,
                     GankService gankService,
                     DoubanService doubanService,
                     ZhihuPicService zhihuPicService,
                     MzituService mzituService,
                     MeizituService meizituService,
                     JiandanService jiandanService,
                     MaijiaxiuService maijiaxiuService,
                     SettingService settingService,
                     TopitService topitService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mDoubanService = doubanService;
        mZhihuPicService = zhihuPicService;
        mMzituService = mzituService;
        mMeizituService = meizituService;
        mJiandanService = jiandanService;
        mMaijiaxiuService = maijiaxiuService;
        mSettingService = settingService;
        mTopitService = topitService;
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

    public JiandanService getJiandanService() {
        return mJiandanService;
    }

    public MaijiaxiuService getMaijiaxiuService() {
        return mMaijiaxiuService;
    }

    public SettingService getSettingService() {
        return mSettingService;
    }

    public TopitService getTopitService() {
        return mTopitService;
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
        Observable<List<Capture>> getList(int type);

        Observable<Image> getQuestionPic(Fragment fragment, long question, int limit, int offset);

        Observable<Image> getCollectionPic(Fragment fragment, long question, int offset);
    }

    public interface MzituService {
        Observable<Integer> getPageCount();

        Observable<Image> getZipai(Fragment fragment, int offset);

        Observable<Image> getTypeOther(Fragment fragment, String type, int offset);

        Observable<Image> getImageRange(Fragment fragment, String url);

        /**
         * 手机页面
         */
        Observable<Image> getM(Fragment fragment, String type, int offset);
    }

    public interface MeizituService {
        Observable<Image> getPic(Fragment fragment, String type, int offset);
    }

    public interface JiandanService {
        Observable<Integer> getPageCount();

        Observable<Image> getPic(Fragment fragment, int offset);

        Observable<String[]> getTop(int type);
    }

    public interface MaijiaxiuService {
        Observable<Image> getPic2(Fragment fragment);
    }

    public interface TopitService {
        Observable<List<Capture>> getList();

        Observable<Image> getAlbum(long id, int offset);
    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);

        Observable<List<License>> getLicense();
    }

}
