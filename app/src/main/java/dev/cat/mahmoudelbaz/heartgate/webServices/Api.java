package dev.cat.mahmoudelbaz.heartgate.webServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.concor.CornrPriceModel;
import dev.cat.mahmoudelbaz.heartgate.heartPress.CardioUpdatesResponseModel;
import dev.cat.mahmoudelbaz.heartgate.heartPress.onlineLibraryResponseModel;
import dev.cat.mahmoudelbaz.heartgate.advisoryBoard.Questions_item;
import dev.cat.mahmoudelbaz.heartgate.myAccount.oldChat.AllMessagesResponse;
import dev.cat.mahmoudelbaz.heartgate.poll.SurveryResponseModel;
import dev.cat.mahmoudelbaz.heartgate.videos.VideoResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @POST("users/reset/{emailaddress}")
    Call<ResponseBody> resetPassword(@Path(value = "emailaddress", encoded = true) String emailaddress);

    @GET("allfavorites")
    Call<ResponseBody> getAllFavorites();


    @GET("price")
    Call<CornrPriceModel> CornorPrice();

    @GET("news/currentnewsinlibrary/{userid}")
    Call<List<onlineLibraryResponseModel>  > getCurrentNewsLibrary(@Path(value = "userid", encoded = true) String id);

    @GET("news/all/{userid}")
    Call<List<CardioUpdatesResponseModel>  > getCardioUpdates(@Path(value = "userid", encoded = true) String id);

    @GET("news/currentnews/{userid}/{newsid}")
    Call<Object  > markAsRead(@Path(value = "userid", encoded = true) String id, @Path(value = "newsid", encoded = true) String newsid);

    @GET("videos/all")
    Call<List<VideoResponseModel>> getAllVideos();

    @GET("survey/all/{userid}")
    Call<List<SurveryResponseModel>  > getSurvey(@Path(value = "userid", encoded = true) String id);

    @POST("users/update_imageprofile_ios/{id}")
    Call<Object> changeImage(@Path(value = "id", encoded = true ) String id, @Body Map<String, String> map );

    @GET("questions/myquestions/{userId}")
    Call<ArrayList<Questions_item>> getQuestions(@Path("userId") String userId);


    @POST("questions/addnewquestion/{userId}")
    @FormUrlEncoded
    Call<ResponseBody> addQuestion(@Path("userId") String userId, @FieldMap Map<String, String> map);


    @GET("questions/answers/{questionId}")
    Call<ResponseBody> getAnswers(@Path("questionId") int questionId);


    @POST("questions/addanswer/{questionId}/{userId}")
    @FormUrlEncoded
    Call<ResponseBody> addAnswer(@Path("questionId") int questionId, @Path("userId") String userId, @FieldMap Map<String, String> map);



    @POST("users/update/{id}")
    Call<Object> updateUser(@Path(value = "id", encoded = true ) String id, @Body Map<String, Object> map );

    @POST("questions/deletequestion/{question_id}/{user_id}")
    Call<Object> delete_post(@Path(value = "question_id", encoded = true) String question_id , @Path(value = "user_id", encoded = true) String user_id);


    @GET("messages/{question_id}/{user_id}")
    Call<ArrayList<AllMessagesResponse>> getAllMessagesOld(@Path(value = "question_id", encoded = true) String question_id , @Path(value = "user_id", encoded = true) String user_id);



    @POST("messages/add")
    Call<Object> sendNewMessage(@Body Map<String, String> headers);


}
