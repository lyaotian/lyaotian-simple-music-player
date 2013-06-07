package cn.lyaotian.simple.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import cn.lyaotian.simple.music.R;
import cn.lyaotian.simple.music.data.MediaItem;
import cn.lyaotian.simple.music.data.PlayList;
import de.greenrobot.event.EventBus;

import java.lang.ref.WeakReference;

/**
 * Created by lyaotian on 6/2/13.
 */
public class PlayService extends Service {
    public static final String TAG = "PlayService";
    private static final String EXTRA_PLAY_LIST = "PlayList";
    public static final String EXTRA_FUNCTION = "function";
    public static final String FUNCTION_PLAY = "function_play";
    public static final String FUNCTION_PAUSE = "function_pause";
    public static final String FUNCTION_STOP = "function_stop";
    public static final String ACTION = "cn.lyaotian.simple.music.service.PlayService";

    private MediaPlayer mediaPlayer;
    private WeakReference<EventBus> eventBusWeak;
    private MediaItem playing;
    private PlayList playList;
    private boolean isPause;

    public IBinder onBind(Intent intent) {
        return new PlayServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String function = intent.getStringExtra(EXTRA_FUNCTION);
            if(FUNCTION_PLAY.equals(function)){
                play(intent);
            }else if(FUNCTION_STOP.equals(function)){
                stop(intent);
            }else if(FUNCTION_PAUSE.equals(function)){
//                pause(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isPlaying(){
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    private void play(Intent intent) {

        if(isPlaying()){
            stop(null);
        }

        //get playlist
        PlayList playListData = (PlayList) intent.getSerializableExtra(EXTRA_PLAY_LIST);
        if(playListData != null && ! playListData.list.isEmpty()){
            playList = playListData;
            playing = playList.list.get(0);
        }

        if(playing == null){
            return;
        }

        //play ...
        setup();
        try{
            mediaPlayer.setDataSource(playing.filePath);
            sendEvent(PlayerEvent.EVENT_FLAG_PREPARING, getString(R.string.play_status_preparing));
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(Exception e){
            showStatus(e.getMessage());
            e.printStackTrace();
        }
    }

    private void showStatus(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void stop(Intent intent) {
        isPause = false;
        if(mediaPlayer == null){
            return;
        }
        try{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            sendEvent(PlayerEvent.EVENT_FLAG_STOP, getString(R.string.play_status_stop));
        }catch(Exception e){
            showStatus(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setup();
    }

    private void setup() {
        mediaPlayer = new MediaPlayer();
        registerListener();
    }

    private void registerListener() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "media player onPrepared");
                sendEvent(PlayerEvent.EVENT_FLAG_PLAY, getString(R.string.play_status_start));
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

                sendEvent(PlayerEvent.EVENT_FLAG_ERROR, getString(R.string.play_status_error, strb.toString()));
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "media player onCompletion");
                if (playList != null) {
                    playNext();
                } else {
                    playing = null;
                    sendEvent(PlayerEvent.EVENT_FLAG_STOP, getString(R.string.play_status_stop));
                }
            }
        });
    }

    private void playNext(){
        int currentIndex = playList.list.indexOf(playing);
        int playListSize = playList.list.size();
        if(currentIndex + 1 >= playListSize){
            sendEvent(PlayerEvent.EVENT_FLAG_STOP, getString(R.string.play_status_stop));
            return;
        }

        playing = playList.list.get(currentIndex + 1);
        if(mediaPlayer == null){
            return;
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playing.filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setEventBus(EventBus eventBus){
        Log.e(TAG, eventBus + "");
        this.eventBusWeak = new WeakReference<EventBus>(eventBus);
    }

    private void sendEvent(int flag, String status){
        Log.d(TAG, "send event.. flag=" + flag + "; status=" + status);
        EventBus eventBus = eventBusWeak.get();
        if(eventBus == null){
            return;
        }

        PlayerEvent event = new PlayerEvent();
        event.flag = flag;
        event.data = playing;
        event.status = status;

        eventBus.post(event);
    }

    public static Intent getPauseIntent(){
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_FUNCTION, FUNCTION_PAUSE);
        return intent;
    }

    public static Intent getPlayIntent(PlayList playList){
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_FUNCTION, FUNCTION_PLAY);
        intent.putExtra(EXTRA_PLAY_LIST, playList);
        return intent;
    }

    public static Intent getStopIntent(){
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_FUNCTION, FUNCTION_STOP);
        return intent;
    }

    public class PlayServiceBinder extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }

    public static class PlayerEvent{
        public static final int EVENT_FLAG_INVALID = -1;
        public static final int EVENT_FLAG_PREPARING = 0;
        public static final int EVENT_FLAG_PLAY = 1;
        public static final int EVENT_FLAG_PAUSE = 2;
        public static final int EVENT_FLAG_STOP = 3;
        public static final int EVENT_FLAG_ERROR = 4;

        public int flag = EVENT_FLAG_INVALID;
        public MediaItem data;
        public String status;

        @Override
        public String toString() {
            return "PlayerEvent{" +
                    "flag=" + flag +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
