package cn.lyaotian.simple.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import cn.lyaotian.simple.music.data.media.MediaItem;
import de.greenrobot.event.EventBus;

/**
 * Created by lyaotian on 6/2/13.
 */
public class CoreService extends Service {
    public static final String TAG = "CoreService";
    public static final String EXTRA_MEDIA_ITEM = "MediaItem";
    public static final String EXTRA_FUNCTION = "function";
    public static final String FUNCTION_PLAY = "function_play";
    public static final String FUNCTION_STOP = "function_stop";
    public static final String ACTION = "cn.lyaotian.simple.music.service.CoreService";

    private MediaPlayer mediaPlayer;
    private EventBus eventBus;

    public IBinder onBind(Intent intent) {
        return new CoreServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String function = intent.getStringExtra(EXTRA_FUNCTION);
            if(FUNCTION_PLAY.equals(function)){
                play(intent);
            }else if(FUNCTION_STOP.equals(function)){
                stop(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void play(Intent intent) {
        stop(null);

        MediaItem data = (MediaItem) intent.getSerializableExtra(EXTRA_MEDIA_ITEM);
        if(data != null){
            try{
                if(mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                }else{
                    mediaPlayer.reset();
                }
                Log.d(TAG, "try to play " + data.filePath);
                mediaPlayer.setDataSource(data.filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch(Exception e){
                showStatus(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showStatus(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void stop(Intent intent) {
        if(mediaPlayer == null){
            return;
        }
        try{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }catch(Exception e){
            showStatus(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setup();
        registerListener();
    }

    private void setup() {
        mediaPlayer = new MediaPlayer();
    }

    private void registerListener() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "media player onPrepared");
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                StringBuffer strb = new StringBuffer("what: ");

                switch (what){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        strb.append("MEDIA_ERROR_UNKNOWN");
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        strb.append("MEDIA_ERROR_SERVER_DIED");
                        break;
                }

                strb.append("; extra: ");

                switch (extra){
                    case MediaPlayer.MEDIA_ERROR_IO:
                        strb.append("MEDIA_ERROR_IO");
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        strb.append("MEDIA_ERROR_MALFORMED");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        strb.append("MEDIA_ERROR_UNSUPPORTED");
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        strb.append("MEDIA_ERROR_TIMED_OUT");
                        break;
                }
                showStatus("error: " + strb.toString());

                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "media player onCompletion");

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class CoreServiceBinder extends Binder{
        public CoreService getService(){
            return CoreService.this;
        }
    }

    public static Intent getPlayIntent(MediaItem mediaItem){
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_FUNCTION, FUNCTION_PLAY);
        intent.putExtra(EXTRA_MEDIA_ITEM, mediaItem);
        return intent;
    }

    public static Intent getStopIntent(){
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_FUNCTION, FUNCTION_STOP);
        return intent;
    }
}
