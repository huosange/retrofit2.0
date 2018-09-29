package com.suhao.retrofit;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MyService {

    @GET("api/destinations.json")
    Call<ResponseBody> getDestinations();

    @GET("api/destinations/{id}.json")
    Call<ResponseBody> getDestinationById(@Path("id") int id);

    @GET("api/trips/featured.json")
    Call<ResponseBody> getTripsByPage(@Query("page") int page);

    @GET("api/trips/featured.json")
    Call<ResponseBody> getTripsByPage(@QueryMap Map<String,Object> map);

    /**
     * post请求必须加上@FormUrlEncoded注解
     */
    @FormUrlEncoded
    @POST("android.v2/ajax/likeStory.ashx")
    Call<ResponseBody> likeStory(@Field("id") int id,@Field("c") int c);

    @FormUrlEncoded
    @POST("android.v2/ajax/likeStory.ashx")
    Call<ResponseBody> likeStory(@FieldMap Map<String,Object> map);

    /**
     * POST请求发送JSON数据
     * 必须给retrofit配置addConverterFactory(GsonConverterFactory.create())
     * @param param
     * @return
     */
    @POST("suhao/servlet01")
    Call<ResponseBody> getMyServlet01(@Body Param param);

    /**
     * 上传一个文件
     * 必须加上@Multipart注解
     * @param file
     * @return
     */
    @Multipart
    @POST("suhao/upload")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);
}