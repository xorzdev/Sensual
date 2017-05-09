package gavin.sensual.app.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;

/**
 * 轮播(如果图片每张点击结果不一样，最好自定义ImageView)
 *
 * @author gavin.xiong 2016/9/13
 */
// TODO: 2017/5/9 改用 dataBinding
public class BannerView extends FrameLayout {

    private ViewPager viewPager;
    private LinearLayout linear;
    private List<ImageView> banner_tip_list;
    private int pageCount;

    private final int REFRESH_BANNER = 0x250;
    private final int refresh_time = 1000 * 5;

    private List<String> urlList;
    private List<String> titleList;

    private OnItemClickListener onItemClickListener;

    public BannerView(Context context) {
        super(context);
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint("InflateParams")
    public void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.widget_banner_view, null);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        linear = (LinearLayout) v.findViewById(R.id.linear);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(v, lp);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setUrlList(List<String> urlList, List<String> titleList) {
        if (titleList != null && titleList.size() != urlList.size()) {
            throw new IllegalArgumentException("The length of the different");
        }
        this.urlList = urlList;
        this.titleList = titleList;
        if (urlList != null && !urlList.isEmpty()) {
            initBanner();
            showAllPage();
            mHandler.sendEmptyMessageDelayed(REFRESH_BANNER, refresh_time);
            invalidate();
        }
    }

    public void initBanner() {
        pageCount = urlList.size();
        linear.removeAllViews();
        List<View> banner_list = new ArrayList<>();
        for (int i = -1; i <= pageCount; i++) {
            final int index = (i + pageCount) % pageCount;
            FrameLayout frameLayout = new FrameLayout(getContext());

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LayoutParams(-1, -1));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext()).load(urlList.get(index)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(imageView);
            imageView.setOnClickListener(view -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(index);
            });

            frameLayout.addView(imageView);

            if (titleList != null) {
                TextView textView = new TextView(getContext());
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                textView.setText(titleList.get(index));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textView.setLines(2);
                LayoutParams params = new LayoutParams(-1, -2, Gravity.BOTTOM);
                params.setMargins(dp2px(16), 0, dp2px(16), dp2px(20));
                textView.setLayoutParams(params);

                frameLayout.addView(textView);
            }

            banner_list.add(frameLayout);
        }
        initViewPager(banner_list);
    }

    private void initViewPager(List<View> banner_list) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (0 == positionOffsetPixels) {
                    if (position == pageCount + 1) {
                        viewPager.setCurrentItem(1, false);
                    } else if (0 == position) {
                        viewPager.setCurrentItem(pageCount, false);
                    }
                    refreshTip(position);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mHandler.removeMessages(REFRESH_BANNER);
                if (state == 0) {
                    mHandler.sendEmptyMessageDelayed(REFRESH_BANNER, refresh_time);
                }
            }
        });

        PageAdapter pageAdapter = new PageAdapter(banner_list);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(1);
    }

    /**
     * 创建通知栏下面的页码小图标*
     */
    private void showAllPage() {
        if (pageCount < 2) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(5), dp2px(5));
        params.setMargins(5, 0, 5, 0);
        banner_tip_list = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            ImageView img = new ImageView(getContext());
            img.setLayoutParams(params);
            banner_tip_list.add(img);
            linear.addView(img);
            img.setImageResource(R.drawable.sn_banner_tip);
            img.setEnabled(false);
        }
    }

    private void refreshTip(int position) {
        if (pageCount < 2) {
            return;
        }
        for (ImageView img : banner_tip_list) {
            img.setEnabled(false);
        }
        banner_tip_list.get((position + pageCount - 1) % pageCount).setEnabled(true);
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case REFRESH_BANNER:
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (pageCount > 1) {
            mHandler.removeMessages(REFRESH_BANNER);
            if (visibility == VISIBLE) {
                mHandler.sendEmptyMessageDelayed(REFRESH_BANNER, refresh_time);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(REFRESH_BANNER);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private int dp2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
