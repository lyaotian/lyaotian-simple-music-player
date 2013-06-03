package cn.lyaotian.simple.music.data.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lyaotian on 6/2/13.
 */
public class MediaStoreUtil {
    public static final String TAG = "MediaStoreUtil";

    private Context mContext;

    public MediaStoreUtil(Context context){
        this.mContext = context;

        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                + Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Music"));
        context.sendBroadcast(scanIntent);
    }

    public ArrayList<MediaItem> getMediaItems(){
        ArrayList<MediaItem> result = new ArrayList<MediaItem>();

        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor queryExternal = null;
        try{
            queryExternal = contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if(queryExternal != null){
                while(queryExternal.moveToNext()){
                    try{
                        result.add(new MediaItem(queryExternal));
                    }catch(Exception e){
                        continue;
                    }
                }
            }

            Log.d(TAG, "getMediaItems result size=" + result.size());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(queryExternal != null){
                queryExternal.close();
            }
        }

        return result;
    }
}
