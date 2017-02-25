package edu.buaa.bwc.buaa_check.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;

import edu.buaa.bwc.buaa_check.Api.Constants;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by XJX on 2016/12/28.
 */

public class RetrofitWrapper {
    private static RetrofitWrapper instance;
    private Retrofit retrofit;
    private Retrofit scalarsRetrofit;
    private Retrofit fileRetrofit;
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
        scalarsRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        fileRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(new FileRequestBodyConverterFactory())
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

    public <T> T createScalar(final Class<T> service) {
        return scalarsRetrofit.create(service);
    }

    public <T> T createFile(final Class<T> service) {
        return fileRetrofit.create(service);
    }

    private static class FileRequestBodyConverterFactory extends Converter.Factory {
        @Override
        public Converter<File, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new FileRequestBodyConverter();
        }
    }

    private static class FileRequestBodyConverter implements Converter<File, RequestBody> {
        @Override
        public RequestBody convert(File file) throws IOException {
            return RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        }
    }
}
