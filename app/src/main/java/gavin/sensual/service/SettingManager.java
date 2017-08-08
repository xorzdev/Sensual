package gavin.sensual.service;

import gavin.sensual.app.setting.Version;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class SettingManager extends BaseManager implements DataLayer.SettingService {

    @Override
    public Observable<Version> getVersion() {
        return getApi().getVersion();
    }

}
