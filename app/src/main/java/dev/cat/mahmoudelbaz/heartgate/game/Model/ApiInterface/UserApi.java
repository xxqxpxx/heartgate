package dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface;


import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuildingsResponse;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelInventory;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelLogin;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelResources;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelSignUp;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelTopPlayer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("login")
    retrofit2.Call<ResultModelLogin> login(@Body Map<String, String> headers);  // Login

    @FormUrlEncoded
    @POST("register")
    retrofit2.Call<ResultModelSignUp> signUp(@Field("name") String name,
                                             @Field("username") String username,
                                             @Field("email") String email,
                                             @Field("password") String password,
                                             @Field("password_confirmation") String password_confirmation
    );  // SignUp


    @GET("user/{id}/inventory")
    Call<ResultModelInventory> getInventory(@Path(value = "id", encoded = true) String id);

    @GET("user/{id}/buildings")
    Call<ResultModelBuildingsResponse> getBuilding(@Path(value = "id", encoded = true) String id);


    @GET("user/{id}/resources")
    Call<ResultModelResources> getResources(@Path(value = "id", encoded = true) String id);


    @GET("user/{id}")
    Call<ResultModelLogin> getUserInfo(@Path(value = "id", encoded = true) String id);


    @GET("users")
    Call<Object> getAllUsers();

    @GET("top-users")
    Call<ResultModelTopPlayer> getTopPLayers();


    @POST("player_id")
    Call<ResponseBody> updatePlayerId(@Body Map<String, String> headers);


    @POST("user-flag")
    Call<Object> updateNotificationStatus(@Body Map<String, String> headers);


} // Interface of LoginAPI

