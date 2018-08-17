package cc.aoeiuv020.demo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduMap();
    }

    private void initBaiduMap() {
        SDKInitializer.initialize(this.getApplicationContext());
    }
}
