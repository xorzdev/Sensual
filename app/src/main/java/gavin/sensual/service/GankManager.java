package gavin.sensual.service;

import gavin.sensual.app.gank.Result;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class GankManager extends BaseManager implements DataLayer.GankService {

    @Override
    public Observable<Result> getWelfare(int limit, int no) {
        return getGankApi().getWelfare(limit, no);
    }
}
