package dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface;


import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelFiterbyResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VillageApi {

    @GET("resources/{resource}/{userid}")
    Call<ResultModelFiterbyResource> getUsersByResource(@Path(value = "resource", encoded = true) String id,
                                                        @Path(value = "userid", encoded = true) String userid);


    @GET("resources")
    Call<Object> resourceLookup();

}
