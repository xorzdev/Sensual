package gavin.sensual.app.capture.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.BundleKey;
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;

/**
 * 知乎看图
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_COLLECTION = 1;

    private int pageType;

    private List<ZhihuQuestion> targetList;

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

        init();
    }

    private void init() {
        targetList = new ArrayList<>();
        if (pageType == TYPE_COLLECTION) {
            targetList.add(new ZhihuQuestion(123354652L, 1, "123354652 - 看知乎姑娘这里就够了", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(72114548L, 1, "72114548 - 欲罢不能的大美妞", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(69135664L, 1, "69135664 - 攻不可破的大美妞阵线联盟", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(52598162L, 1, "52598162 - 知乎福利精选", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(30822111L, 1, "30822111 - 知乎福利：「只有会员知道的世界」", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(30256531L, 1, "30256531 - 内有爆照", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(38624707L, 1, "38624707 - 知乎妹纸爆照大合集", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(69105016L, 1, "69105016 - 【知乎花式骗图】", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(81105951L, 1, "81105951 - 【绝对领域控】", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        } else {
            targetList.add(new ZhihuQuestion(37787176L, 0, "37787176 - 当一个颜值很高的程序员是怎样一番体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(20843119L, 0, "20843119 - 拍照的时候怎么让表情自然？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(22212644L, 0, "22212644 - 胸大怎么搭配衣服？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(38285230L, 0, "38285230 - 有一群漂亮的朋友是什么体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(29289467L, 0, "29289467 - 生活中你见过的最美女性长什么样？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(39210626L, 0, "39210626 - 胸夹铅笔是胸体下垂了吗？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(39132527L, 0, "39132527 - 胸大是怎样一种糟糕的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(39346461L, 0, "39346461 - 大胸究竟有多诱人？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(22182464L, 0, "22182464 - 因为胸大而自卑，怎么办？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(37006507L, 0, "37006507 - 有一个漂亮的室友是一种什么样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(36430439L, 0, "36430439 - 美女们，敢请晒波波照吗？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(24715519L, 0, "24715519 - 腿细是怎样的一种体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(26037846L, 0, "26037846 - 身材好是一种怎样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(36536551L, 0, "36536551 - 爆乳是怎样一种体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(37267636L, 0, "37267636 - 如何拍一张色而不淫的照片？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(37756869L, 0, "37756869 - 你在健身房看到的身材最好的女性是怎样的？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(39351146L, 0, "39351146 - 贫乳的魅力在哪些方面？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(27499928L, 0, "27499928 - 和「女神」约会是一种什么样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(30982846L, 0, "30982846 - 女生夏天穿超短裙是一种什么样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(27767614L, 0, "27767614 - 屁股大是怎样一种体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(31079801L, 0, "31079801 - 美腿是一种怎样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(37365624L, 0, "37365624 - 如何评价濑亚美莉？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(37709992L, 0, "37709992 - 长得好看，但没有男朋友是怎样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(40554112L, 0, "40554112 - 胸大的姑娘一般都有哪些担忧？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(38694587L, 0, "38694587 - 屁股翘是一种怎样的感受？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            targetList.add(new ZhihuQuestion(34195468L, 0, "34195468 - 你圈子里长得最美的女生长啥样？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
            // 第六页
        }

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, targetList, R.layout.item_capture);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            if (targetList.get(position).getType() == 0) {
                start(ZhihuQuestionFragment.newInstance(targetList.get(position).getId()));
            } else if (targetList.get(position).getType() == 1) {
                start(ZhihuCollectionFragment.newInstance(targetList.get(position).getId()));
            }
        });
    }
}
