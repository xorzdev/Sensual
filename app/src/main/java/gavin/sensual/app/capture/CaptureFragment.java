package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.app.zhihu.ZhihuFragment;
import gavin.sensual.app.zhihu.ZhihuQuestion;
import gavin.sensual.app.zhihu.ZhihuQuestionFragment;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragCaptureBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class CaptureFragment extends BindingFragment<FragCaptureBinding> implements BindingAdapter.OnItemClickListener {

    private List<ZhihuQuestion> targetList;

    public static CaptureFragment newInstance() {
        return new CaptureFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_capture;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.toolbar.setTitle("抓图");
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> pop());

        init();
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                start(ZhihuFragment.newInstance());
                break;
            default:
                break;
        }
    }

    private void init() {
        targetList = new ArrayList<>();
        targetList.add(new ZhihuQuestion(0L, "知乎看图", "http://static.cfanz.cn/uploads/jpg/2012/11/01/23/df24d55432.jpg"));

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, targetList, R.layout.item_capture);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
}
