package gavin.sensual.app.capture.topit;

import android.content.Context;
import android.databinding.ViewDataBinding;

import gavin.sensual.R;
import gavin.sensual.app.capture.Capture;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.recycler.BindingHeaderFooterAdapter;
import gavin.sensual.base.recycler.PagingViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 发现
 *
 * @author gavin.xiong 2017/8/14
 */
public class TopitViewModel extends PagingViewModel<Capture, BindingHeaderFooterAdapter<Capture>> {

    TopitViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
        refreshable.set(false);
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_capture);
        adapter.setOnItemClickListener(position ->
                mFragment.get().start(TopitDetailsFragment.newInstance(mList.get(position).getId())));
    }

    @Override
    protected void getData(boolean isMore) {
        getDataLayer().getTopitService().getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    loading.set(true);
                })
                .doOnComplete(() -> loading.set(false))
                .doOnError(throwable -> loading.set(false))
                .subscribe(list -> {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                }, e -> notifyMsg(e.getMessage()));
    }
}
