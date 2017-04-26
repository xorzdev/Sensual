package gavin.sensual.net;

import gavin.sensual.app.daily.TodayNews;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * API
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {

    /**
     * 获取今日日报新闻列表
     *
     * @return
     */
    @GET("news/latest")
    Observable<TodayNews> getTodayNews();

//    /**
//     * 获取新闻
//     *
//     * @param newsId long
//     * @return News
//     */
//    @GET("news/{newsId}")
//    Observable<News> getNews(@Path("newsId") long newsId);
}
