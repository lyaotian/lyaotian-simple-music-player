package cn.lyaotian.simple.music.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lyaotian on 6/6/13.
 */
public class PlayList implements Serializable {
    public String name;
    public final ArrayList<MediaItem> list = new ArrayList<MediaItem>();

}
