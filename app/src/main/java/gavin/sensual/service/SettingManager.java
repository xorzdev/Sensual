package gavin.sensual.service;

import java.util.List;

import gavin.sensual.app.setting.License;
import gavin.sensual.service.base.BaseManager;
import gavin.sensual.service.base.DataLayer;
import io.reactivex.Observable;

/**
 * SettingManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class SettingManager extends BaseManager implements DataLayer.SettingService {

    @Override
    public Observable<List<License>> getLicense() {
        return getApi().getLicense();
    }
}
