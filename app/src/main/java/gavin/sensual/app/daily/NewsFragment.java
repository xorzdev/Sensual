package gavin.sensual.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bumptech.glide.Glide;

import gavin.sensual.R;
import gavin.sensual.app.main.DrawerToggleEvent;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.base.RxBus;
import gavin.sensual.databinding.FragNewsBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 日报详情
 *
 * @author gavin.xiong 2017/4/28
 */
public class NewsFragment extends BindingFragment<FragNewsBinding> {

    public static NewsFragment newInstance(long newsId) {
        Bundle args = new Bundle();
        args.putLong(BundleKey.NEWS_ID, newsId);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_news;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getNews(getArguments().getLong(BundleKey.NEWS_ID));
    }

    private void init() {
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.setNavigationOnClickListener((v) -> RxBus.get().post(new DrawerToggleEvent(true)));
        binding.collapsingToolbarLayout.setTitleEnabled(false);
        binding.collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getContext(), android.R.color.white));
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
    }

    private void getNews(long newsId) {
        getDataLayer().getDailyService().getNews(newsId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    binding.nestedView.setVisibility(View.GONE);
                    binding.cpbLoading.setVisibility(View.VISIBLE);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> binding.cpbLoading.setVisibility(View.GONE))
                .doOnError(e -> binding.cpbLoading.setVisibility(View.GONE))
                .subscribe(news -> {
                    binding.toolbar.setTitle(news.getTitle());
                    binding.nestedView.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(news.getImage()).into(binding.ivHeader);
                    binding.tvSource.setText(news.getImageSource());
                    String htmlData = HtmlUtil.createHtmlData(news);
                    binding.wvNews.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                }, e -> Snackbar.make(binding.wvNews, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show());
    }
}
