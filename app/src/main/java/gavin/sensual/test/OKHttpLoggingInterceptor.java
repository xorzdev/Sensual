/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gavin.sensual.test;

import java.io.IOException;
import java.nio.charset.Charset;

import gavin.sensual.util.L;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * 自定义 OkHttp 日志拦截器
 *
 * @author gavin.xiong 2017/4/28
 */
public final class OKHttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        L.v("--> " + request.method() + ' ' + request.url());

        Response response;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            throw e;
        }

        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            L.d("<-- " + request.url() + "\n" + source.buffer().clone().readString(Charset.forName("UTF-8")));
        } catch (Exception e) {
            L.e(e.getLocalizedMessage());
        }
        return response;
    }
}
