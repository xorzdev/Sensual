package gavin.sensual.app.douban;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragGankBinding;
import gavin.sensual.util.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/9
 */
public class DoubanFragment extends BindingFragment<FragGankBinding> {

    public static DoubanFragment newInstance() {
        return new DoubanFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_gank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.toolbar.setTitle("豆瓣");
        binding.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> pop());

        getRank();
    }

    private void getRank() {
        getDataLayer().getDBService().getRank(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(Schedulers.computation())
//                .map(responseBody -> {
//                    List<Image> images = new ArrayList<>();
//                    try {
//                        Document document = Jsoup.parse(responseBody.string());
////                            Elements elements = document.select("div[class=thumbnail] > div[class=img_single] > a > img");
//                        Elements elements = document.select("div[class=thumbnail] div[class=img_single] img");
//                        final int size = elements.size();
//                        for (int i = 0; i < size; i++) {
//                            String src = elements.get(i).attr("src").trim();
//                            if (DataBase.hasImage(null, src)) {
//                                continue;
//                            }
//                            images.add(new Image(src, type));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return images;
//                });
                .subscribe(responseBody -> {

                });
    }
}
