package gavin.sensual.net;

import gavin.sensual.app.gank.Result;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface GankAPI {

    /**
     * 获取福利
     *
     * @param limit 分页大小
     * @param no    页码
     * @return Result
     */
    @Headers("Cache-Control: max-stale=1800")
    @GET("data/福利/{limit}/{no}")
    Observable<Result> getWelfare(@Path("limit") int limit, @Path("no") int no);
}
