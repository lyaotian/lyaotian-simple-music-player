package cn.lyaotian.simple.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by lyaotian on 6/2/13.
 */
public class CoreService extends Service {
    public IBinder onBind(Intent intent) {
        return new CoreServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
}
