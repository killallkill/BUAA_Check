package edu.buaa.bwc.buaa_check.Api;

import edu.buaa.bwc.buaa_check.POJOs.DeleteCheckResponse;
import edu.buaa.bwc.buaa_check.POJOs.ListResponse;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckDetailHeader;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckItem;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Gary on 2017/1/13.
 */

public interface SelfCheckService {
    @FormUrlEncoded
    @POST("c_self_list.do")//获取列表
    Call<ListResponse<SelfCheckItem>> getSelfCheckList(@Field("page") int page, @Field("rows") int rows);

    @FormUrlEncoded
    @POST("c_spot_del.do")//删除抽查管理记录
    Call<DeleteCheckResponse> delSelfCheckItem(@Field("id") String id, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("c_spot_view.do")//检查记录详细信息
    Call<SelfCheckDetailHeader> getSelfCheckDetailHeader(@Field("id") String id);

    @FormUrlEncoded
    @POST("c_c_view.do")//检查记录详情
    Call<String> getSelfCheckDetail(@Field("id") String id);
}
