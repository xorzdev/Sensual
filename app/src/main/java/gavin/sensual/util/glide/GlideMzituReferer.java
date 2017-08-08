package gavin.sensual.util.glide;

import com.bumptech.glide.load.model.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Glide 自定义请求头 - 反反盗链
 *
 * @author gavin.xiong 2017/8/7
 */
public class GlideMzituReferer implements Headers {

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "http://m.mzitu.com/");
        // header.put("Host", "i.meizitu.net");
        return header;
    }

}
