package gavin.sensual.net;

import gavin.sensual.app.daily.News;
import gavin.sensual.app.daily.TodayNews;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {

    /**
     * 获取今日日报新闻列表 ( 最长缓存一天 60 * 60 * 24 )
     *
     * @return TodayNews
     */
    // 如果服务端有做缓存处理的话，我们只需要在Request的Header中添加Cache-Control即可
    // @Headers("Cache-Control: public, max-age=86400")
    @GET("news/latest")
    Observable<TodayNews> getTodayNews();

    /**
     * 获取新闻
     *
     * @param newsId long
     * @return News
     */
    @GET("news/{newsId}")
    Observable<News> getNews(@Path("newsId") long newsId);
}
