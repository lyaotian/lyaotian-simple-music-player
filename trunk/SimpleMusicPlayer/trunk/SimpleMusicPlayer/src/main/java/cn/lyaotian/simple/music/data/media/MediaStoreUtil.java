package cn.lyaotian.simple.music.data.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
    }

    public ArrayList<MediaItem> getMediaItems(){
        ArrayList<MediaItem> result = new ArrayList<MediaItem>();

        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor queryExtrnal = null;
        try{
            queryExtrnal = contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if(queryExtrnal != null){
                while(queryExtrnal.moveToNext()){
                    result.add(new MediaItem(queryExtrnal));
                }
            }

            Log.d(TAG, "result size=" + result.size());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(queryExtrnal != null){
                queryExtrnal.close();
            }
        }

        return result;
    }
}
