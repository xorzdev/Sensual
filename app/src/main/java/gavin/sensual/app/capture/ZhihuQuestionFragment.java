package gavin.sensual.app.capture;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingAdapter;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragZhihuQuestionBinding;
import gavin.sensual.service.DoubanManager;
import gavin.sensual.util.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gavin on 2017/5/10.
 */

public class ZhihuQuestionFragment extends BindingFragment<FragZhihuQuestionBinding> {

    public static ZhihuQuestionFragment newInstance() {
        return new ZhihuQuestionFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_zhihu_question;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        DoubanManager m = (DoubanManager)getDataLayer().getDoubanService();
        m.getQuestion(this, "https://www.zhihu.com/question/37787176")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> {
                    L.e(images);
                    binding.recycler.setAdapter(new CaptureAdapter(_mActivity, images));
                }, L::e);
    }
}
