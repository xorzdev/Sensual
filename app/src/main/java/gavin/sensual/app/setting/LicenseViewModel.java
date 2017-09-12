package gavin.sensual.app.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BaseFragment;
import gavin.sensual.base.recycler.BindingHeaderFooterAdapter;
import gavin.sensual.base.recycler.PagingViewModel;
import gavin.sensual.util.AssetsUtils;
import gavin.sensual.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 开源许可
 *
 * @author gavin.xiong 2017/8/14
 */
public class LicenseViewModel extends PagingViewModel<License, BindingHeaderFooterAdapter<License>> {

    LicenseViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
        refreshable.set(false);
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_license);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse((mList.get(position)).getUrl());
            intent.setData(content_url);
            mFragment.get().startActivity(intent);
        });
    }

    @Override
    protected void getData(boolean isMore) {
        Observable.just("license.json")
                .map(s -> AssetsUtils.readText(mContext.get(), s))
                .map(s -> {
                    List<License> list = JsonUtil.toList(s, new TypeToken<List<License>>() {
                    });
                    return list;
                })
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                }, throwable -> notifyMsg(throwable.getMessage()));
    }
}
