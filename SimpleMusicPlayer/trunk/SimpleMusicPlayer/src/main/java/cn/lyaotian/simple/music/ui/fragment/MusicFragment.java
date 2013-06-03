package cn.lyaotian.simple.music.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.lyaotian.simple.music.R;
import cn.lyaotian.simple.music.data.MediaItem;
import cn.lyaotian.simple.music.data.util.MediaStoreUtil;
import cn.lyaotian.simple.music.service.PlayService;

import java.util.ArrayList;

/**
 * Created by lyaotian on 5/29/13.
 */
public class MusicFragment extends Fragment {
    public static final String TAG = "MusicFragment";

    //view
    private ListView listView;
    private MusicAdapter adapter;

    //data
    private Activity mActivity;
    private MediaStoreUtil mediaStoreUtil;

    public MusicFragment(){
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mediaStoreUtil = new MediaStoreUtil(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mediaStoreUtil = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        registerListener();
        loadAsync();
    }

    private void findViews(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
    }

    private void registerListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaItem click = adapter.getItem(position);
                play(click);
            }
        });
    }

    private void play(MediaItem click) {
        Log.d(TAG, click + "");
        if(click != null){
            Intent playIntent = PlayService.getPlayIntent(click);
            mActivity.startService(playIntent);
        }
    }

    private void loadAsync(){
        ArrayList<MediaItem> mediaItems = mediaStoreUtil.getMediaItems();
        if(adapter == null){
            adapter = new MusicAdapter(mediaItems);
            listView.setAdapter(adapter);
        }
    }

    private class MusicAdapter extends BaseAdapter{

        public ArrayList<MediaItem> dataList;

        public MusicAdapter(ArrayList<MediaItem> dataList){
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return this.dataList.size();
        }

        @Override
        public MediaItem getItem(int position) {
            return this.dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            if(item == null){
                item = mActivity.getLayoutInflater().inflate(R.layout.item_music_list, null);
            }

            MediaItem data = getItem(position);
            TextView tvTitle = (TextView) item.findViewById(R.id.tvTitle);
            tvTitle.setText(data.title);

            return item;
        }
    }
}
