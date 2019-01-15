package dev.cat.mahmoudelbaz.heartgate.game.Model.ApiInterface;


import java.util.Map;

import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuyItem;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUpgradeInfo;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUpgradeRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MarketApi {


    @POST("upgrade-info")
    Call<ResultModelUpgradeInfo> get_building_info(@Body Map<String, String> headers);

    @POST("upgrade")
    Call<ResultModelUpgradeRequest> upgrade_item(@Body Map<String, String> headers);


    @POST("item-sell")
    Call<ResultModelBuyItem> sell_item(@Body Map<String, String> headers);

    @POST("item-buy")
    Call<ResultModelBuyItem> buy_item(@Body Map<String, String> headers);


}
