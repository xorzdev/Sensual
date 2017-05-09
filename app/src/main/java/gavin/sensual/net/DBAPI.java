package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 豆瓣 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface DBAPI {

    @GET("rank.htm")
    Observable<ResponseBody> getRank(@Query("pager_offset") int page);
}
