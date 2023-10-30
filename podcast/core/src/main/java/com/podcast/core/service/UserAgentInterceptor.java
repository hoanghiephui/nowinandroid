package com.podcast.core.service;

import android.util.Log;
import com.podcast.core.ClientConfig;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class UserAgentInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("UserAgentInterceptor", ClientConfig.USER_AGENT);
        return chain.proceed(chain.request().newBuilder()
                .header("User-Agent", ClientConfig.USER_AGENT)
                .build());
    }
}
