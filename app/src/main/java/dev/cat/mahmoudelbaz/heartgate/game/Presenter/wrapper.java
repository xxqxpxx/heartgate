package dev.cat.mahmoudelbaz.heartgate.game.Presenter;

import android.support.v7.app.AppCompatActivity;

import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelBuyItem;
import dev.cat.mahmoudelbaz.heartgate.game.Model.ResultModel.ResultModelUpgradeRequest;

public interface wrapper {
    AppCompatActivity getActivity();

    void updateUiCounter(ResultModelBuyItem resultModelBuyItem);

    void updateUiBuilding(ResultModelUpgradeRequest body);
}
