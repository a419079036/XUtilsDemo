package mobi.vhly.xutilsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = "DL";

    @ViewInject(R.id.progress)
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        x.view().inject(this);

        // Android 动态申请权限

        // 1. 检查是否已经有了权限
        int p = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if(p == PackageManager.PERMISSION_DENIED){
            // 如果权限是拒绝的，那么申请摸个权限
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    998
            );
        }else{
            startDownload();
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        if(requestCode == 998){

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    int p = grantResults[i];
                    if(p == PackageManager.PERMISSION_GRANTED){
                        startDownload();
                    }
                }
            }

        }

    }

    private void startDownload(){
        RequestParams requestParams =
                new RequestParams("http://10.0.153.80:8080/umeng-share-sdk.zip");
        // 文件下载，断点续传
        requestParams.setAutoResume(true);
        // 下载文件，如果文件已经存在，那么自动重命名
        requestParams.setAutoRename(true);
        String state = Environment.getExternalStorageState();
        File saveDir = null;
        if(state.equals(Environment.MEDIA_MOUNTED)){
            saveDir = Environment.getExternalStorageDirectory();
        }else{
            saveDir = getFilesDir();
        }

        if(!saveDir.exists()){
            saveDir.mkdirs();
        }

        File target = new File(saveDir, "abc.zip");

        // 文件保存在哪
        requestParams.setSaveFilePath(target.getAbsolutePath());

        // 对于文件下载，结果就是一个 File
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                // TODO: 开始下载
                Snackbar.make(mProgressBar, "开始下载", Snackbar.LENGTH_SHORT).show();
            }

            /**
             * 文件下载或者上传时的进度回调
             * @param total
             * @param current
             * @param isDownloading true 代表文件下载 否则上传
             */
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                // TODO: 下载中
                mProgressBar.setMax((int) total);
                mProgressBar.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                Log.d(TAG, "onSuccess: " + result.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
