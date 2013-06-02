package cn.lyaotian.simple.music.data.media;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by lyaotian on 6/2/13.
 */
public class MediaItem {

    public String title;
    public String artist;
    public long duration;
    public long size;
    public String mimeType;

    public MediaItem(Cursor cursor){
        size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
    }
}
