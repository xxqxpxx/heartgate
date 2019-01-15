package dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface;


import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelGeneric;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUserRequests;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {

    @Headers("Content-Type: application/json")
    @POST("make-request")
    Call<ResultModelGeneric> create_request(@Body Map<String, String> headers);


    @GET("user-requests/{id}")
    Call<ResultModelUserRequests> get_all_user_received_requests(@Path(value = "id", encoded = true) String id);


    @GET("user/{id}/requests")
    Call<ResultModelUserRequests> get_all_sent_requests(@Path(value = "id", encoded = true) String id);


    @POST("request/{id}/confirm")
        // id for request , user_id for the user who will approve/decline the request
    Call<ResultModelGeneric> Confirm_Request(@Path(value = "id", encoded = true) String id, @Body Map<String, String> headers);

    @POST("request/{id}/reject")
    Call<ResultModelGeneric> Reject_Request(@Path(value = "id", encoded = true) String id, @Body Map<String, String> headers);

}
