package gavin.sensual.app.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.android.databinding.library.baseAdapters.BR;

import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragGankBinding;
import gavin.sensual.util.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/6
 */
public class GankFragment extends BindingFragment<FragGankBinding> {

    public static GankFragment newInstance() {
        return new GankFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_gank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        aaa();
    }

    private void init() {
        binding.toolbar.setTitle("干货集中营");
    }

    private void aaa() {
        getDataLayer().getGankService().getWelfare(15, 1)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {

                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> {

                })
                .doOnComplete(() -> {

                })
                .doOnError(throwable -> {

                })
                .subscribe(result -> {
                    binding.recycler.setAdapter(new BindingAdapter<>(_mActivity, result.getResults(), R.layout.item_welfare, BR.item));
                });
    }
}
