package mobi.vhly.xutilsdemo.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import mobi.vhly.xutilsdemo.R;
import mobi.vhly.xutilsdemo.model.VideoInfo;

/**
 * Created by vhly[FR].
 * <p>
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2016/10/21
 */

public class VideoListAdapter extends BaseAdapter implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private Context mContext;
    private List<VideoInfo> mInfos;

    private static MediaPlayer sMediaPlayer;
    private static int sPlayingPosition;

    public VideoListAdapter(Context context, List<VideoInfo> infos) {
        mContext = context;
        mInfos = infos;

        if (sMediaPlayer == null) {
            sMediaPlayer = new MediaPlayer();
        } else {
            sMediaPlayer.release();
        }
        sPlayingPosition = -1;
        sMediaPlayer.setOnCompletionListener(this);
        sMediaPlayer.setOnPreparedListener(this);

    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mInfos != null) {
            ret = mInfos.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView != null) {
            ret = convertView;
        } else {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ret = inflater.inflate(R.layout.item_video, parent, false);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }

        VideoInfo info = mInfos.get(position);
        Log.d("VLA", "getView: " + position);
        holder.setItemPosition(position);
        holder.setInfo(info);

        if(sMediaPlayer.isPlaying()) {
            if (position == sPlayingPosition) {
                sMediaPlayer.setDisplay(holder.mSurfaceView.getHolder());
                holder.mCover.setVisibility(View.INVISIBLE);
            }else{
                holder.mCover.setVisibility(View.VISIBLE);
            }
        }

        return ret;
    }

    public static int getPlayingPosition() {
        return sPlayingPosition;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // next
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void switchDisplay(SurfaceView floatingSurfaceView) {
        if (sMediaPlayer != null) {
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.setDisplay(floatingSurfaceView.getHolder());
            }
        }
    }

    public void destroy() {
        if (sMediaPlayer != null) {
            sMediaPlayer.stop();
            sMediaPlayer.reset();
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }

    private static class ViewHolder implements SurfaceHolder.Callback, View.OnClickListener {
        private SurfaceView mSurfaceView;
        private Button mButton;
        private int itemPosition;
        private VideoInfo mInfo;
        private ImageView mCover;

        public ViewHolder(View itemView) {
            mSurfaceView = (SurfaceView) itemView.findViewById(R.id.video_surface_view);
            mButton = (Button) itemView.findViewById(R.id.video_play_btn);
            mCover = (ImageView)itemView.findViewById(R.id.video_cover);
            mSurfaceView.getHolder().addCallback(this);
            mButton.setOnClickListener(this);
        }

        public void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        public void setInfo(VideoInfo info) {
            mInfo = info;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
//            if(sMediaPlayer != null) {
//                if (sPlayingPosition == itemPosition) {
//                    sMediaPlayer.stop();
//                }
//            }
        }

        @Override
        public void onClick(View v) {
            // TODO: 点击播放视频
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.stop();
            }
            mCover.setVisibility(View.INVISIBLE);
            sMediaPlayer.reset();

            // 1. 切换显示的SurfaceView
            sMediaPlayer.setDisplay(mSurfaceView.getHolder());

            // 2. 设置视频地址，开始加载
            try {
                String url = mInfo.getFirstUrl();
                if (url != null) {
                    sMediaPlayer.setDataSource(url);
                    sMediaPlayer.prepareAsync();
                    sPlayingPosition = itemPosition;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
