package gavin.sensual.net;

import gavin.sensual.app.daily.Daily;
import gavin.sensual.app.daily.News;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
     * @return Daily
     */
    // 指定返回复用时间为 60s
    @Headers("Cache-Control: max-stale=60")
    @GET("news/latest")
    Observable<Daily> getDaily();

    /**
     * 获取新闻
     *
     * @param newsId long
     * @return News
     */
    @Headers("Cache-Control: max-stale=3600")
    @GET("news/{newsId}")
    Observable<News> getNews(@Path("newsId") long newsId);
}
