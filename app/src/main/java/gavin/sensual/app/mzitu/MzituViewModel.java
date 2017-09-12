package gavin.sensual.app.mzitu;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.app.main.StartFragmentEvent;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.RxBus;
import io.reactivex.Observable;

/**
 * 豆瓣
 *
 * @author gavin.xiong 2017/8/15
 */
class MzituViewModel extends ImageViewModel {

    private String cid;

    private Integer pageCount;

    MzituViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, String cid) {
        super(context, fragment, binding);
        this.cid = cid;
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        if (!"zipai".equals(cid)) {
            adapter.setListener(i ->
                    RxBus.get().post(new StartFragmentEvent(MzituRangFragment.newInstance(mList.get(i).getUrl()))));
        }
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        if (!"zipai".equals(cid))
            return getDataLayer().getMeiziPicService().getTypeOther(mFragment.get(), cid, isMore ? pagingOffset + 1 : 1);

        if (pageCount != null)
            return getDataLayer().getMeiziPicService().getZipai(mFragment.get(), isMore ? pageCount - pagingOffset : pageCount);

        return getDataLayer().getMeiziPicService().getPageCount()
                .flatMap(integer -> {
                    pageCount = integer;
                    return getDataSrc(isMore);
                });
    }
}
