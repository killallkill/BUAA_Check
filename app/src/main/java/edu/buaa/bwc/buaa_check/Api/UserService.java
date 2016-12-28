package edu.buaa.bwc.buaa_check.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by XJX on 2016/12/28.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("j_spring_security_check")
    Call<ResponseBody> login(@Field("j_username") String username, @Field("j_password") String password);

}
