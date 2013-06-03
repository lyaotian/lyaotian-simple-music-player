package cn.lyaotian.simple.music.data.media;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by lyaotian on 6/2/13.
 */
public class MediaItem implements Serializable{

    public String title;
    public String artist;
    public long duration;
    public long size;
    public String mimeType;
    public String filePath;

    public MediaItem(Cursor cursor) throws Exception{
        size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        byte [] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        filePath = new String(data, 0 , data.length - 1);
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
