package mobi.vhly.xutilsdemo;

import android.app.Application;

import org.xutils.x;

/**
 * Created by vhly[FR].
 * <p>
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2016/10/21
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化  xUtils 3
        x.Ext.init(this); // 初始化之后， x.app() 才有效，上下文

    }
}
