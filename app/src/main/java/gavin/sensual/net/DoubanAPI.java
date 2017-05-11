package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 豆瓣 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface DoubanAPI {

    @Headers("Cache-Control: max-stale=1800")
    @GET("rank.htm")
    Observable<ResponseBody> getRank(@Query("pager_offset") int page);

    @Headers("Cache-Control: max-stale=1800")
    @GET("show.htm")
    Observable<ResponseBody> getShow(@Query("cid") String type, @Query("pager_offset") int page);
}
