package gavin.sensual.app.meizi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.douban.DoubanAdapter;
import gavin.sensual.app.douban.Image;
import gavin.sensual.app.setting.BigImageMultiFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.FragToobleRecyclerBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/15
 */
public class MeiziDetailFragment extends BindingFragment<FragToobleRecyclerBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<Image> imageList;
    private DoubanAdapter adapter;

    public static MeiziDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(BundleKey.PAGE_TYPE, url);
        MeiziDetailFragment fragment = new MeiziDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_tooble_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        String url = getArguments().getString(BundleKey.PAGE_TYPE);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String title = url.substring(url.lastIndexOf("/") + 1, url.length() - 6);
        binding.includeToolbar.toolbar.setTitle(title);
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        binding.refreshLayout.setEnabled(false);

        imageList = new ArrayList<>();
        adapter = new DoubanAdapter(_mActivity, imageList);
        adapter.setOnItemClickListener(position -> {
            ArrayList<String> stringList = new ArrayList<>();
            for (Image image : imageList) {
                stringList.add(image.getUrl());
            }
            start(BigImageMultiFragment.newInstance(stringList, position));
        });
        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recycler.setAdapter(adapter);

        getDataLayer().getMeiziPicService()
                .getPic2(this, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(image -> {
                    imageList.add(image);
                    adapter.notifyItemInserted(imageList.size() - 1);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
