package edu.buaa.bwc.buaa_check.Utils;

import java.net.CookieManager;
import java.net.CookiePolicy;

import edu.buaa.bwc.buaa_check.Api.Constants;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XJX on 2016/12/28.
 */

public class RetrofitWrapper {
    private static RetrofitWrapper instance;
    private Retrofit retrofit;
    private OkHttpClient mOkHttpClient;

    private RetrofitWrapper() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieManager cookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieHandler);
        mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitWrapper getInstance() {
        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                if (instance == null) {
                    instance = new RetrofitWrapper();
                }
            }
        }
        return instance;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
