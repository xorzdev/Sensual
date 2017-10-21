package gavin.sensual.app.capture.topit;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.app.common.Image;
import gavin.sensual.app.common.ImageViewModel;
import gavin.sensual.base.BaseFragment;
import io.reactivex.Observable;

/**
 * TopitDetailsModel
 *
 * @author gavin.xiong 2017/8/11
 */
class TopitmeViewModel extends ImageViewModel {

    TopitmeViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.setListener(i -> mFragment.get().start(
                TopitmeDetailsFragment.newInstance(Long.parseLong(mList.get(i).getId()))));
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getTopitService().getList(mFragment.get(), isMore ? pagingOffset : 0);
    }

}
