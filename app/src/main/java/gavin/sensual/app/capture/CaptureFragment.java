package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import gavin.sensual.R;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragCaptureBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class CaptureFragment extends BindingFragment<FragCaptureBinding> {

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

    private void init() {
        List<Target> targetList = new ArrayList<>();
        targetList.add(new Target(
                "当一个颜值很高的程序员是怎样一番体验？",
                "https://www.zhihu.com/question/37787176",
                "https://pic1.zhimg.com/27a1a0c48a799f60b655d8949879949c_b.png"));

        BindingAdapter adapter = new BindingAdapter<>(_mActivity, targetList, R.layout.item_capture, BR.item);
        binding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            start(ZhihuQuestionFragment.newInstance(37787176));
//            start(ZhihuQuestionFragment.newInstance(20843119));
        });
    }
}
