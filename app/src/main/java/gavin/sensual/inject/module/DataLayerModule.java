package gavin.sensual.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gavin.sensual.service.DailyManager;
import gavin.sensual.service.base.DataLayer;

/**
 * @author lsxiao
 * @date 2015-11-04 00:44
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    public DailyManager provideDailyManager() {
        return new DailyManager();
    }


    @Singleton
    @Provides
    public DataLayer provideDataLayer(DailyManager dailyManager) {
        return new DataLayer(dailyManager);
    }
}
