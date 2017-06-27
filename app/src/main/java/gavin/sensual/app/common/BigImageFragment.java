package gavin.sensual.app.common;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragBigImageBinding;
import gavin.sensual.databinding.RighterLoadingBinding;
import gavin.sensual.db.util.DbUtil;
import gavin.sensual.util.ImageLoader;
import gavin.sensual.util.ShareUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/22
 */
public class BigImageFragment extends BindingFragment<FragBigImageBinding> implements Toolbar.OnMenuItemClickListener {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int requestCode;

    private List<Image> imageList;
    private BigImageAdapter adapter;
    private RighterLoadingBinding loadingBinding;

    private LinearLayoutManager linearLayoutManager;

    private String imageUrl;

    private ImageView ivActionLove;

    private AnimatorSet scaleAnim;

    public static BigImageFragment newInstance(@NonNull ArrayList<Image> imageList, int position, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.IMAGE_URL, imageList);
        bundle.putInt(BundleKey.POSITION, position);
        bundle.putInt(BundleKey.PAGE_TYPE, requestCode);
        BigImageFragment fragment = new BigImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_big_image;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        requestCode = getArguments().getInt(BundleKey.PAGE_TYPE);
        imageList = (ArrayList<Image>) getArguments().getSerializable(BundleKey.IMAGE_URL);
        init();
        subscribeEvent();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDownload:
                createFile();
                break;
            case R.id.actionShare:
                shareImage();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
        if (scaleAnim != null) {
            scaleAnim.cancel();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        toPop();
        return true;
    }

    private void toPop() {
        RxBus.get().post(new BigImagePopEvent(requestCode, linearLayoutManager.findFirstVisibleItemPosition()));
        pop();
    }

    private void init() {
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> toPop());
        binding.toolbar.inflateMenu(R.menu.action_image_option);
        binding.toolbar.setOnMenuItemClickListener(this);

        ivActionLove = binding.toolbar.getMenu().findItem(R.id.actionCollect).getActionView().findViewById(R.id.ivActionLove);
        ivActionLove.setOnClickListener(v -> doLoveAnim(imageUrl));

        adapter = new BigImageAdapter(_mActivity, this, imageList);
        loadingBinding = RighterLoadingBinding.inflate(LayoutInflater.from(_mActivity));
        loadingBinding.root.setVisibility(View.VISIBLE);
        adapter.setFooterBinding(loadingBinding);
        binding.recycler.setAdapter(adapter);
        binding.recycler.scrollToPosition(getArguments().getInt(BundleKey.POSITION));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recycler);

        binding.recycler.setOnLoadListener(() -> RxBus.get().post(new LoadMoreEvent(requestCode)));

        binding.recycler.preCount = 3;
        binding.recycler.haveMore = true;
        linearLayoutManager = (LinearLayoutManager) binding.recycler.getLayoutManager();

        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    imageUrl = imageList.get(linearLayoutManager.findFirstVisibleItemPosition()).getUrl();
                    Observable.just(imageUrl)
                            .map(s -> DbUtil.getCollectionService().hasCollected(s))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(ivActionLove::setSelected);
                }
            }
        });

        imageUrl = imageList.get(getArguments().getInt(BundleKey.POSITION)).getUrl();
        Observable.just(imageUrl)
                .map(s -> DbUtil.getCollectionService().hasCollected(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ivActionLove::setSelected);
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(NewImageEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (event.requestCode != requestCode) return;
                    if (linearLayoutManager.findLastVisibleItemPosition() == imageList.size() - 1) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemInserted(imageList.indexOf(event.image));
                    }
                });

        RxBus.get().toObservable(LoadStateEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> {
                    if (event.requestCode != requestCode) return;
                    if (event.state == LoadStateEvent.STATE_NONE) {
                        binding.recycler.loading = false;
                        loadingBinding.progressBar.setVisibility(View.VISIBLE);
                        loadingBinding.textView.setText("加载中...");
                    } else if (event.state == LoadStateEvent.STATE_NO_MORE) {
                        binding.recycler.haveMore = false;
                        loadingBinding.progressBar.setVisibility(View.GONE);
                        loadingBinding.textView.setText("没有更多了");
                    } else if (event.state == LoadStateEvent.STATE_ERROR) {
                        loadingBinding.progressBar.setVisibility(View.GONE);
                        loadingBinding.textView.setText("玩坏了...");
                    }
                });

        RxBus.get().toObservable(BigImageClickEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(event -> toPop());
    }

    /**
     * 喜欢/取消喜欢动画
     */
    private void doLoveAnim(String image) {
        Observable.timer(350, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    compositeDisposable.add(disposable);
                    ivActionLove.setEnabled(false);
                    if (scaleAnim == null) {
                        initAnim();
                    }
                    scaleAnim.start();
                })
                .map(arg0 -> {
                    DbUtil.getCollectionService().toggle(image);
                    ivActionLove.setSelected(!ivActionLove.isSelected());
                    return arg0;
                })
                .delay(350, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(arg0 -> ivActionLove.setEnabled(true));
    }

    /**
     * 初始化属性动画
     */
    private void initAnim() {
        scaleAnim = new AnimatorSet().setDuration(700);
        scaleAnim.setInterpolator(new AnticipateOvershootInterpolator());
        scaleAnim.playTogether(
                ObjectAnimator.ofFloat(ivActionLove, View.SCALE_X, 1f, 0f, 1f),
                ObjectAnimator.ofFloat(ivActionLove, View.SCALE_Y, 1f, 0f, 1f));
    }

    private void shareImage() {
        Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(s -> ShareUtil.shareImage(new WeakReference<>(this).get(), s),
                        e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")) + ".jpg");
        startActivityForResult(intent, 99);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && data != null && data.getData() != null) {
            saveBitmap(data.getData());
        }
    }

    private void saveBitmap(Uri uri) {
        Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(uri1 -> getContext().getContentResolver().openOutputStream(uri1))
                .filter(outputStream -> outputStream != null)
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(outputStream -> {
                    try {
                        Bitmap bitmap = ImageLoader.getBitmap(this, imageUrl);
                        boolean state = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        Snackbar.make(binding.recycler, state ? "保存成功" : "保存失败", Snackbar.LENGTH_LONG).show();
                    } finally {
                        outputStream.close();
                    }
                }, throwable -> Snackbar.make(binding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
