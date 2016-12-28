package edu.buaa.bwc.buaa_check.Utils;

import edu.buaa.bwc.buaa_check.Api.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XJX on 2016/12/28.
 */

public class RetrofitWrapper {
    private static RetrofitWrapper instance;
    private Retrofit retrofit;

    private RetrofitWrapper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
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
