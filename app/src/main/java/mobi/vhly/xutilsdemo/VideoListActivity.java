package mobi.vhly.xutilsdemo;

import android.net.Proxy;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mobi.vhly.xutilsdemo.adapters.VideoListAdapter;
import mobi.vhly.xutilsdemo.model.VideoInfo;

@ContentView(R.layout.activity_video_list)
public class VideoListActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "VLA";

    @ViewInject(R.id.floating_video_view)
    private SurfaceView mFloatingSurfaceView;

    @ViewInject(R.id.video_list)
    private ListView mListView;

    private List<VideoInfo> mVideoInfoList;
    private VideoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        mVideoInfoList = new ArrayList<>();
        mAdapter = new VideoListAdapter(this, mVideoInfoList);
        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(this);


        // 1. HTTP 工具类使用
        RequestParams requestParams =
                new RequestParams("https://www.baidu.com/img/bd_logo1.png");
//                new RequestParams("http://ic.snssdk.com/neihan/stream/mix/v1/?content_type=-104&message_cursor=-1&loc_time=1432654641&latitude=40.0522901291784&longitude=116.23490963616668&city=北京&count=30&screen_width=800&iid=2767929313&device_id=2757969807&ac=wifi&channel=baidu2&aid=7&app_name=joke_essay&version_code=400&device_platform=android&device_type=KFTT&os_api=15&os_version=4.0.3&openudid=b90ca6a3a19a78d6");

        // HTTP的设置
        File cacheDir = getCacheDir();
        if (!cacheDir.exists()){
            cacheDir.mkdir();
        }
        requestParams.setCacheDirName(cacheDir.getAbsolutePath());
        requestParams.setCacheMaxAge(5 * 60 * 1000);

        x.http().get(
                requestParams,
                new Callback.CacheCallback<byte[]>() {
                    @Override
                    public boolean onCache(byte[] result) {
                        return true;
                    }

                    @Override
                    public void onSuccess(byte[] result) {
                        Log.d("VLA", "onSuccess");
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.printStackTrace();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        cex.printStackTrace();
                    }

                    @Override
                    public void onFinished() {
                        Log.d("VLA", "onFinished");
                    }
                }
        );

//        requestVideoList(requestParams);
    }

    private void requestVideoList(RequestParams requestParams) {
        x.http().get(requestParams, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, "onSuccess: " + result);

                mVideoInfoList.clear();

                // 解析视频列表
                try {
                    JSONObject outerData = result.getJSONObject("data");
                    JSONArray innerData = outerData.getJSONArray("data");
                    int len = innerData.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJson = innerData.getJSONObject(i);
                        int type = itemJson.getInt("type");
                        if (type == 1) {
                            JSONObject group = itemJson.getJSONObject("group");
                            JSONObject video = group.getJSONObject("360p_video");
                            VideoInfo info = VideoInfo.createFromJson(video);
                            mVideoInfoList.add(info);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                Log.d(TAG, "onFinished");
            }
        });
    }

    @Override
    protected void onDestroy() {
        mAdapter.destroy();
        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int playingPosition = VideoListAdapter.getPlayingPosition();
        if (playingPosition >= firstVisibleItem && playingPosition < firstVisibleItem + visibleItemCount) {
            mFloatingSurfaceView.setVisibility(View.INVISIBLE);
        } else {
            // position < first || position >= +
            mFloatingSurfaceView.setVisibility(View.VISIBLE);
            mAdapter.switchDisplay(mFloatingSurfaceView);
        }
    }
}
