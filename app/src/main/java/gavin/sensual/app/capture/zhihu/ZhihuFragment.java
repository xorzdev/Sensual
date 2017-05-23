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
import gavin.sensual.databinding.LayoutToolbarRecyclerBinding;

/**
 * 知乎看图
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private List<ZhihuQuestion> targetList;

    public static ZhihuFragment newInstance() {
        return new ZhihuFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.includeToolbar.toolbar.setTitle("知乎看图");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        binding.includeToolbar.toolbar.inflateMenu(R.menu.action_search);
        binding.refreshLayout.setEnabled(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(binding.includeToolbar.toolbar.getMenu().findItem(R.id.actionSearch));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("请输入问题 id");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
//                    start(ZhihuQuestionFragment.newInstance(Long.parseLong(query)));
                    start(ZhihuCollectionFragment.newInstance(Long.parseLong(query)));
                } catch (NumberFormatException e) {
                    Snackbar.make(binding.recycler, "请输入正确问题id", Snackbar.LENGTH_LONG).show();
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
        targetList.add(new ZhihuQuestion(47291204L, 0, "47291204 - 灵魂画师是一种怎样的体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(26830927L, 0, "26830927 - 国内风景最美的地方？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(37787176L, 0, "37787176 - 当一个颜值很高的程序员是怎样一番体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(20843119L, 0, "20843119 - 拍照的时候怎么让表情自然？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(22212644L, 0, "22212644 - 胸大怎么搭配衣服？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(38285230L, 0, "38285230 - 有一群漂亮的朋友是什么体验？", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(123354652L, 1, "123354652 - 看知乎姑娘这里就够了", "https://img3.doubanio.com/lpic/s28586695.jpg"));
        targetList.add(new ZhihuQuestion(72114548L, 1, "72114548 - 欲罢不能的大美妞", "https://img3.doubanio.com/lpic/s28586695.jpg"));

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
