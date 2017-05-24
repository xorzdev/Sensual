package gavin.sensual.app.capture.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.capture.Capture;
import gavin.sensual.app.setting.AssetsUtils;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;
import gavin.sensual.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎看图
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_COLLECTION = 1;

    private Disposable disposable;

    private int pageType;

    public static ZhihuFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKey.PAGE_TYPE, type);
        ZhihuFragment fragment = new ZhihuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        pageType = getArguments().getInt(BundleKey.PAGE_TYPE);

        binding.includeToolbar.toolbar.setTitle(String.format("知乎看图 - %s", pageType == TYPE_COLLECTION ? "收藏" : "问题"));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.includeToolbar.toolbar.inflateMenu(R.menu.action_search);
        binding.refreshLayout.setEnabled(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(binding.includeToolbar.toolbar.getMenu().findItem(R.id.actionSearch));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint(String.format("请输入%s id", pageType == TYPE_COLLECTION ? "收藏" : "问题"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (pageType == TYPE_COLLECTION) {
                        start(ZhihuCollectionFragment.newInstance(Long.parseLong(query)));
                    } else {
                        start(ZhihuQuestionFragment.newInstance(Long.parseLong(query)));
                    }
                } catch (NumberFormatException e) {
                    Snackbar.make(binding.recycler, "请输入正确 id", Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void getData() {
        Observable.just(pageType)
                .map(type -> type == TYPE_COLLECTION ? "zhihu_collection.json" : "zhihu_question.json")
                .map(s -> AssetsUtils.readText(_mActivity, s))
                .map(s -> JsonUtil.toList(s, new TypeToken<List<Capture>>() { }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> this.disposable = disposable)
                .subscribe(list -> {
                    BindingAdapter adapter = new BindingAdapter<>(_mActivity, list, R.layout.item_capture);
                    adapter.setOnItemClickListener(position -> {
                        if (pageType == TYPE_QUESTION) {
                            start(ZhihuQuestionFragment.newInstance(((Capture) list.get(position)).getId()));
                        } else if (pageType == TYPE_COLLECTION) {
                            start(ZhihuCollectionFragment.newInstance(((Capture) list.get(position)).getId()));
                        }
                    });
                    binding.recycler.setAdapter(adapter);
                }, e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show());
    }
}
