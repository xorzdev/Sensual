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
    // 猜测： max-age 为同一请求复用时间  max-stale 缓存过期时间 复用请求不会延迟过期
    // 单位均为秒
    // 缓存有限期为 60 * 60 * 24
    // 请求复用时间 60 * 1
    @Headers("Cache-Control: public, max-age=60, max-stale=86400")
    @GET("news/latest")
    Observable<Daily> getDaily();

    /**
     * 获取新闻
     *
     * @param newsId long
     * @return News
     */
    @Headers("Cache-Control: public, max-age=3600, max-stale=86400")
    @GET("news/{newsId}")
    Observable<News> getNews(@Path("newsId") long newsId);
}
