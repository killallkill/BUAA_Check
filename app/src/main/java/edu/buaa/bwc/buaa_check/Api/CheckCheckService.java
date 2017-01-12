package edu.buaa.bwc.buaa_check.Api;

import edu.buaa.bwc.buaa_check.POJOs.CheckCheckDetailHeader;
import edu.buaa.bwc.buaa_check.POJOs.CheckCheckItem;
import edu.buaa.bwc.buaa_check.POJOs.DeleteCheckResponse;
import edu.buaa.bwc.buaa_check.POJOs.ListResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by XJX on 2017/1/3.
 */

public interface CheckCheckService {

    @FormUrlEncoded
    @POST("c_c_list.do")
    Call<ListResponse<CheckCheckItem>> getCheckCheckList(@Field("page") int page, @Field("rows") int rows);

    @FormUrlEncoded
    @POST("c_spot_del.do")
    Call<DeleteCheckResponse> delCheckCheckItem(@Field("id") String id, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("c_spot_view.do")
    Call<CheckCheckDetailHeader> getCheckDetailHeader(@Field("id") String id);

    @FormUrlEncoded
    @POST("c_c_view.do")
    Call<String> getCheckDetail(@Field("id") String id);
}
