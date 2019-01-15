package dev.cat.mahmoudelbaz.heartgate.game.Presenter.Notification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

import dev.cat.mahmoudelbaz.heartgate.game.Presenter.Helper;

public class NotificationService extends NotificationExtenderService {

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        // Read properties from result.
        Log.d("NotificationProcessing", "onNotificationProcessing: " + receivedResult.toString());
        Log.d("NotificationProcessing", "jsonObject: " + receivedResult.payload.additionalData.toString());

        String type = null;
        JSONObject data = null;
        JSONObject jsonObject = receivedResult.payload.additionalData;
        try {
            data = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (type.equals("inventory") || type.equals("resources")) {
            if (Helper.isAppRunning(getApplicationContext(), "com.cat.ahmed.cowfarm")) {
                // App is running // update UI
                // updateUI
                /*Intent gcm_rec = new Intent("your_action");
                LocalBroadcastManager.getInstance(arg0).sendBroadcast(gcm_rec);*/

                updateUI(data, type);

            } else {
                // App is not running
            }

            return true;
        }

        // Return true to stop the notification from displaying.
        return false;
    }

    private void updateUI(JSONObject data, String type) {


        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        intent.putExtra("data", data.toString());
        intent.putExtra("type", type);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


}
