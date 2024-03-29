package cn.lyaotian.simple.music.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.lyaotian.simple.music.R;
import cn.lyaotian.simple.music.service.PlayService;
import de.greenrobot.event.EventBus;

/**
 * Created by lyaotian on 5/29/13.
 */
public class PlayerFragment extends Fragment {
    public static final String TAG = "PlayerFragment";

    //view
    private TextView tvTitle;
    private TextView tvArtist;
    private TextView tvStatus;
    private View btnPrev;
    private View btnNext;
    private View btnPlay;
    private View btnPause;
    private View btnStop;

    //data
    private PlayService playService;
    private Activity mActivity;
    private EventBus eventBus;

    private final ServiceConnection playServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService = ((PlayService.PlayServiceBinder)service).getService();

            eventBus.register(PlayerFragment.this);
            playService.setEventBus(PlayerFragment.this.eventBus);

            Log.d(TAG, "onServiceConnected " + playService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            eventBus.unregister(this);

            playService = null;
        }
    };

    public PlayerFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        eventBus = new EventBus();

        mActivity.startService(new Intent(mActivity, PlayService.class));
        bindPlayService();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbindPlayService();

        mActivity = null;
    }

    private void bindPlayService() {
        Intent coreServiceIntent = new Intent(PlayService.ACTION);
        mActivity.bindService(coreServiceIntent, playServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void unbindPlayService() {
        mActivity.unbindService(playServiceConn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        registerListener();
    }

    private void registerListener() {
        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnNext:
                        break;
                    case R.id.btnPrev:
                        break;
                    case R.id.btnPlay:
                        mActivity.startService(PlayService.getPlayIntent(null));
                        break;
                    case R.id.btnPause:
                        mActivity.startService(PlayService.getPauseIntent());
                        break;
                    case R.id.btnStop:
                        mActivity.startService(PlayService.getStopIntent());
                        break;
                }
            }
        };
        btnNext.setOnClickListener(onClickListener);
        btnPrev.setOnClickListener(onClickListener);
        btnPlay.setOnClickListener(onClickListener);
        btnPause.setOnClickListener(onClickListener);
        btnStop.setOnClickListener(onClickListener);
    }

    private void findViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvArtist = (TextView) view.findViewById(R.id.tvArtist);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPause = view.findViewById(R.id.btnPause);
        btnStop = view.findViewById(R.id.btnStop);
    }

    public void onEventMainThread(PlayService.PlayerEvent event) {
        Log.e(TAG, "onEvent " + event);
        showPlaying(event);
    }

    private void showPlaying(PlayService.PlayerEvent event) {
        if(event.data == null){

            tvTitle.setText("empty");
            tvArtist.setText("empty");
            return;
        }

        tvTitle.setText(event.data.title);
        tvArtist.setText(event.data.artist);
    }
}
