package dev.cat.mahmoudelbaz.heartgate.videos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import android.widget.VideoView;
import android.widget.MediaController;


import dev.cat.mahmoudelbaz.heartgate.R;

import static dev.cat.mahmoudelbaz.heartgate.webServices.Services.MAIN_VIDEOS_URL;

public class VideoDetailsActivity extends AppCompatActivity {

    private Button btnonce, btncontinuously, btnstop, btnplay;
    private VideoView vv;
    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;
    private ProgressBar progressBar;
    String uriPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        String getVideo_url = getIntent().getStringExtra("getVideo_url");
          uriPath = MAIN_VIDEOS_URL + getVideo_url ;
        uri = Uri.parse(uriPath);


        startService(new Intent(this, MediaPlayerService.class));
        findViewById(R.id.btnChangeTrack).setOnClickListener(clickListener);
        findViewById(R.id.btnStartMediaPlayer).setOnClickListener(clickListener);
        findViewById(R.id.btnStopMediaPlayer).setOnClickListener(clickListener);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.togglePauseResume);
        toggleButton.setOnCheckedChangeListener(checkedChangeListener);
        /*
         * To get url which is passing from the previous activity listitem click.
         * If url which is pass from listitem click is not empty it will start player
         * */
        String url = getIntent().getStringExtra(uriPath);
        if (!TextUtils.isEmpty(url))
            startMediaPlayer(url);

    }

    private ToggleButton.OnCheckedChangeListener checkedChangeListener = new ToggleButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                Intent intent = new Intent();
                intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.PAUSE_MEDIA_PLAYER);
                sendBroadcast(intent);
            } else {
                Intent intent = new Intent();
                intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.RESUME_MEDIA_PLAYER);
                sendBroadcast(intent);
            }
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btnChangeTrack:
                    intent = new Intent();
                    intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                    intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.CHANGE_PLAYER_TRACK);
                    intent.putExtra(MediaPlayerService.PLAYER_TRACK_URL, uriPath);
                    sendBroadcast(intent);
                    break;
                case R.id.btnStartMediaPlayer:
                    startMediaPlayer(uriPath);
//startMediaPlayer("http://108.163.197.114:8071/listen.pls");
                    break;
                case R.id.btnStopMediaPlayer:
                    intent = new Intent();
                    intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
                    intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.STOP_MEDIA_PLAYER);
                    sendBroadcast(intent);
                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiverFromservice, new IntentFilter(MediaPlayerService.SERVICE_TO_ACTIVITY));
    }

    private String currentPlayerStatus = "N/A";
    private BroadcastReceiver receiverFromservice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MediaPlayerService.SERVICE_TO_ACTIVITY.equalsIgnoreCase(action)) {
                /*
                 * To get current status of player
                 * */
                currentPlayerStatus = intent.getStringExtra(MediaPlayerService.PLAYER_STATUS_KEY);
                Log.e("Player Mode", "" + currentPlayerStatus);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverFromservice);
    }

    /**
     * TO start media player.It will send broadcast to Service & from service player will start
     *
     * @param url
     */
    public void startMediaPlayer(String url) {
        Intent intent = new Intent();
        intent.setAction(MediaPlayerService.BROADCAST_TO_SERVICE);
        intent.putExtra(MediaPlayerService.PLAYER_FUNCTION_TYPE, MediaPlayerService.PLAY_MEDIA_PLAYER);
        intent.putExtra(MediaPlayerService.PLAYER_TRACK_URL, url);
        sendBroadcast(intent);
    }
}