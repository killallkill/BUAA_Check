package edu.buaa.bwc.buaa_check.Api;

import java.io.File;
import java.util.List;

import edu.buaa.bwc.buaa_check.POJOs.CheckRole;
import edu.buaa.bwc.buaa_check.POJOs.CheckUnit;
import edu.buaa.bwc.buaa_check.POJOs.ListResponse;
import edu.buaa.bwc.buaa_check.POJOs.NormalResponse;
import edu.buaa.bwc.buaa_check.POJOs.OtherCheckPeople;
import edu.buaa.bwc.buaa_check.POJOs.RectifyUser;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckDetailHeader;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Gary on 2017/1/13.
 */

public interface SelfCheckService {
    @FormUrlEncoded
    @POST("c_self_list.do")//获取列表
    Call<ListResponse<SelfCheckItem>> getSelfCheckList(@Field("page") int page, @Field("rows") int rows);

    @FormUrlEncoded
    @POST("c_spot_del.do")//删除抽查管理记录
    Call<NormalResponse> delSelfCheckItem(@Field("id") String id, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("c_spot_view.do")//检查记录详细信息
    Call<SelfCheckDetailHeader> getSelfCheckDetailHeader(@Field("id") String id);

    @FormUrlEncoded
    @POST("c_c_view.do")//检查记录详情
    Call<String> getSelfCheckDetail(@Field("id") String id);

    //*********************
    //检查角色
    @POST("c_c_dept_role_check_list.do")
    Call<List<CheckRole>> getCheckRole();

    //其它检查人员
    @POST("c_c_u_check_list.do")
    Call<List<OtherCheckPeople>> getOtherCheckPeople();

    //获取被检查单位列表
    @FormUrlEncoded
    @POST("c_c_dept_list.do")
    Call<List<CheckUnit>> getCheckUnit(@Field("id") String id);

    //获取三级单位列表
    @FormUrlEncoded
    @POST("c_c_dept_list.do")
    Call<List<CheckUnit>> getThreeClassUnit(@Field("id") String id);

    @Multipart
    @POST("")
    Call<ResponseBody> upload(@Part("") File file);

    @POST("check_check_spotUser.do")
    Call<List<RectifyUser>> getRectifyUsers(@Query("type") String type, @Query("id") String id);

    @FormUrlEncoded
    @POST("rectify_send.do")
    Call<NormalResponse> sendRectify(@Field("pitfallLv") String level,
                                     @Field("id") String id,
                                     @Field("rectifyFinishi") int day,
                                     @Field("userRoleId") String urid,
                                     @Field("proposal") String proposal,
                                     @Field("type") String type);
}
