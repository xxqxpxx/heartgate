package dev.cat.mahmoudelbaz.heartgate.webServices;

import dev.cat.mahmoudelbaz.heartgate.concor.CornrPriceModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

   /* @Headers("Content-Type: application/json")
    @POST("register")
    Call<RegisterResponseModel> Register(@Body Map<String, String> headers);

    @POST("update-user")
    Call<RegisterResponseModel> update_user(@Body Map<String, String> headers);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> Login(@Body Map<String, String> headers);

    @POST("reset-password")
    Call<RegisterResponseModel> Reset_Password(@Body Map<String, String> headers);


    @POST("update-user-image")
    Call<Object> update_user_image(@Body Map<String, String> headers);


    @POST("change-program")
    Call<LoginResponse> change_program(@Body Map<String, String> headers);


    @POST("get-user-program")
    Call<LoginResponse> get_user_program(@HeaderMap Map<String, String> headers);


    @POST("get-user-progress")
    Call<LoginResponse> get_user_progress(@HeaderMap Map<String, String> headers);


    @POST("confirm-schedule")
    Call<LoginResponse> confirm_schedule(@HeaderMap Map<String, String> headers);


    @POST("get-current-day")
    Call<CurrentDayResponseModel> get_current_day(@Body Map<String, String> headers);


    @POST("change-password")
    Call<LoginResponse> change_password(@HeaderMap Map<String, String> headers);


    @POST("update-player-id")
    Call<Object> update_player_id(@HeaderMap Map<String, String> headers);


    @POST("user-info")
    Call<LoginResponse> user_info(@HeaderMap Map<String, String> headers);


    @POST("user-track")
    Call<LoginResponse> user_track(@HeaderMap Map<String, String> headers);


    @POST("healthyTips")
    Call<LoginResponse> healthyTips(@HeaderMap Map<String, String> headers);


    @GET("healthyTips?page=1")
    Call<LoginResponse> healthyTipsPage(@HeaderMap Map<String, String> headers);


    @GET("healthyTips/{id}")
    Call<LoginResponse> healthyTipsByID(@HeaderMap Map<String, String> headers);


    @GET("notifications")
    Call<LoginResponse> notifications(@HeaderMap Map<String, String> headers);


    @GET("offers")
    Call<OffersResponseModel> offers();


    @GET("events")
    Call<OffersResponseModel> events();


    @GET("programs")
    Call<programsResponseModel> programs();


    @GET("schedules")
    Call<LoginResponse> schedules(@HeaderMap Map<String, String> headers);


    @GET("show-posts/{postid}")
    Call<PostsResponseModel> getposts(@Path(value = "postid", encoded = true) String id);

    @POST("posts")
    Call<AddPostResponseModel> AddPosts(@Body Map<String, String> headers);

    @GET("update-post/{post}")
    Call<LoginResponse> update_post(@HeaderMap Map<String, String> headers);


    @POST("delete-post/{post}")
    Call<LoginResponse> delete_post(@HeaderMap Map<String, String> headers);


    @GET("user/{user}/posts")
    Call<LoginResponse> user_Posts(@HeaderMap Map<String, String> headers);


    @POST("post/{postid}/comments")
    Call<AddCommentResponseModel> addComment(@Path(value = "postid", encoded = true) String id, @Body Map<String, String> headers);


    @POST("comment/{comment}/update")
    Call<LoginResponse> editComment(@HeaderMap Map<String, String> headers);


    @POST("comment/{comment}/delete")
    Call<LoginResponse> deleteComment(@HeaderMap Map<String, String> headers);


    @GET("post/{post}/comments")
    Call<AddCommentResponseModel> postComments(@Path(value = "post", encoded = true) String id);


    @POST("post/{post}/likes")
    Call<AddCommentResponseModel> addLike(@Path(value = "post", encoded = true) String id, @Body Map<String, String> headers);


    @POST("like/{like}/delete")
    Call<LoginResponse> deleteLike(@HeaderMap Map<String, String> headers);


    @GET("post/{post}/likes M")
    Call<LoginResponse> postsLikes(@HeaderMap Map<String, String> headers);


    @GET("user-meals/{userid}")
    Call<FoodResonseModel> getMeals(@Path(value = "userid", encoded = true) String id);

    @GET("user-exercises/{userid}")
    Call<ExerciseResponseModel> getExercise(@Path(value = "userid", encoded = true) String id);

    @POST("get-user-progress")
    Call<FoodResonseModel> getMeals(@HeaderMap Map<String, String> headers);


    @GET("get-program/{programid}/days")
    Call<DayProgramResponse> getDayProgram(@Path(value = "programid", encoded = true) String id);

    @POST("get-current-meal")
    Call<CurrentDayResponseModel> getMealsbyDay(@Body Map<String, String> headers);

    @POST("get-current-exercise")
    Call<CurrentDayResponseModel> getExercisebyDay(@Body Map<String, String> headers);

    @POST("confirm-meals")
    Call<ConfirmationResponse> confirmMeal(@Body Map<String, String> headers);

    @POST("confirm-exercises")
    Call<ConfirmationResponse> confirmExercise(@Body Map<String, String> headers);


    @POST("player_id")
    Call<ResponseBody> updatePlayerId(@Body Map<String, String> headers);


    @GET("notifications")
    Call<Notifications_item> getNotifications();*/


    @POST("users/reset/{emailaddress}")
    Call<ResponseBody> resetPassword(@Path(value = "emailaddress", encoded = true) String emailaddress);

    @GET("allfavorites")
    Call<ResponseBody> getAllFavorites();


    @GET("price")
    Call<CornrPriceModel> CornorPrice();




}
