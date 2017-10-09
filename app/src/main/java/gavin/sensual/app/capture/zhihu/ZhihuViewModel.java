package gavin.sensual.app.capture.zhihu;

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
public class ZhihuViewModel extends PagingViewModel<Capture, BindingHeaderFooterAdapter<Capture>> {

    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_COLLECTION = 1;

    private final int pageType;

    ZhihuViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, int pageType) {
        super(context, fragment, binding);
        this.pageType = pageType;
        refreshable.set(false);
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_capture);
        adapter.setOnItemClickListener(position ->
                mFragment.get().start(ZhihuDetailsFragment.newInstance(pageType, mList.get(position).getId())));
    }

    @Override
    protected void getData(boolean isMore) {
        getDataLayer().getZhihuPicService().getList(pageType)
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
