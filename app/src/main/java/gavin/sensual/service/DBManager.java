package gavin.sensual.service;

import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class DBManager extends BaseManager implements DataLayer.DBService {

    @Override
    public Observable<ResponseBody> getRank(int page) {
        return getDBApi().getRank(page);
    }
}
