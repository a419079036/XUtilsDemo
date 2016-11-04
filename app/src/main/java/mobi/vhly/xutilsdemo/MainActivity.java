package mobi.vhly.xutilsdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.image_view)
    private ImageView mImageView;

    @ViewInject(R.id.txt_view)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        ListView listView = (ListView) findViewById(R.id.list_view);
        if (listView != null) {
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                urls.add("https://www.baidu.com/img/bd_logo1.png");
            }
            ImageAdapter adapter = new ImageAdapter(this, urls);
            listView.setAdapter(adapter);
        }
    }

    @Event(value = {R.id.btn_show})
    private void btnShowOnClick(View v) {
        // TODO: 加载图片
//        x.image().bind(
//                mImageView,
//                "https://www.baidu.com/img/bd_logo1.png"
//        );
        // 1. 下载图片并且指定图像参数的方式，圆形、圆角矩形、
        ImageOptions.Builder builder = new ImageOptions.Builder();

        ImageOptions options =
                builder.setSquare(true)
                        .setRadius(20)
                        .setFadeIn(true)
                        .build();
        x.image().bind(
                mImageView,
                "https://www.baidu.com/img/bd_logo1.png",
                options
        );
    }

    @Event(value = {R.id.chb_save},
            type = CompoundButton.OnCheckedChangeListener.class)
    private void chbOnCheckedChange(CompoundButton checkBox, boolean isChecked) {
        mTextView.setText("Checked = " + isChecked);
    }
}
