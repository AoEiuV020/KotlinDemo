package cc.aoeiuv020.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MapPickerActivity extends AppCompatActivity {
    private static final String TAG = "MapPickerActivity";
    private static final String MAP_TYPE_KEY = "mapType";
    private MapHelper mapHelper;
    private MapHelper.Picker picker;

    /**
     * 封装好启动这个activity的方法，必需要mapType,
     *
     * @param context context,
     * @param mapType 必须要有，不可空，
     */
    public static void startActivity(Context context, MapHelper.MapType mapType) {

        Intent intent = new Intent(context, MapPickerActivity.class);
        intent.putExtra(MAP_TYPE_KEY, mapType.name());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_picker);

        String errorMessage = "";
        if (getIntent() != null) {
            String mapTypeName = getIntent().getStringExtra(MAP_TYPE_KEY);
            if (mapTypeName != null) {
                try {
                    MapHelper.MapType mapType = MapHelper.MapType.valueOf(mapTypeName);
                    mapHelper = MapHelper.getInstance(mapType);
                } catch (IllegalArgumentException e) {
                    errorMessage = "地图类型<" + mapTypeName + ">不存在，";
                }
            }
        }
        if (mapHelper == null) {
            new RuntimeException(errorMessage)
                    .printStackTrace();
            finish();
            return;
        }
        initMap();
    }

    private void initMap() {
        picker = mapHelper.getPicker(this);
        getLifecycle().addObserver(picker);
        FrameLayout container = findViewById(R.id.map_view_container);
        picker.attack(container, new MapHelper.OnMapReadyListener() {
            @Override
            public void ready() {
                mapHelper.requestLatLng(new MapHelper.OnSuccessListener<MapHelper.LatLng>() {
                    @Override
                    public void onSuccess(MapHelper.LatLng latLng) {
                        picker.moveMap(latLng);
                    }
                }, new MapHelper.OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(MapPickerActivity.this, "自动定位失败", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
        picker.setOnMapStatusChangeListener(new MapHelper.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapHelper.MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapHelper.MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapHelper.MapStatus mapStatus) {
                Log.d(TAG, "onMapStatusChangeFinish() called with: mapStatus = [" + mapStatus + "]");
            }
        });

    }
}
