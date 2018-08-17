package cc.aoeiuv020.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.MapView;

public class BaiduMapActivity extends AppCompatActivity {
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);

        mMapView = findViewById(R.id.bmapView);
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMapView.onResume();
    }
}
