package cc.aoeiuv020.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Switch mChinaCheckBox;
    private TextView mLocationTextView;
    private MapHelper.LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.latlng_btn).setOnClickListener(this);
        findViewById(R.id.map_btn).setOnClickListener(this);
        findViewById(R.id.image_btn).setOnClickListener(this);
        findViewById(R.id.poi_btn).setOnClickListener(this);
        findViewById(R.id.city_btn).setOnClickListener(this);
        mChinaCheckBox = findViewById(R.id.china_cb);
        MapHelper.MapType type;
        if (mChinaCheckBox.isChecked()) {
            type = MapHelper.MapType.BAIDU;
        } else {
            type = MapHelper.MapType.GOOGLE;
        }
        MapHelper.setType(type);
        mChinaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChina) {
                MapHelper.MapType type;
                if (isChina) {
                    type = MapHelper.MapType.BAIDU;
                } else {
                    type = MapHelper.MapType.GOOGLE;
                }
                MapHelper.setType(type);
            }
        });
        mLocationTextView = findViewById(R.id.location_tv);
    }

    @Override
    public void onClick(View view) {
        boolean isChina = mChinaCheckBox.isChecked();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "google place: 没有权限");
            return;
        }
        switch (view.getId()) {
            case R.id.latlng_btn:
                MapHelper.getInstance().requestLatLng(new MapHelper.OnSuccessListener<MapHelper.LatLng>() {
                    @Override
                    public void onSuccess(MapHelper.LatLng latLng) {
                        mLatLng = latLng;
                        String location = "定位信息  mLongitude:" + mLatLng.getLongitude() + "  mLatitude:" + mLatLng.getLatitude();
                        mLocationTextView.setText(location);
                        Log.d(TAG, location);
                    }
                }, new MapHelper.OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case R.id.map_btn:
                Class clazz;
                if (isChina) {
                    clazz = BaiduMapActivity.class;
                } else {
                    clazz = MapsActivityCurrentPlace.class;
                }
                Intent intent = new Intent(this, clazz);
                startActivity(intent);
                break;
            case R.id.image_btn:
                break;
            case R.id.poi_btn:
                MapHelper.getInstance().requestPlaceList(mLatLng, new MapHelper.OnSuccessListener<List<MapHelper.Place>>() {
                    @Override
                    public void onSuccess(List<MapHelper.Place> places) {
                        List<String> addList = new ArrayList<>(places.size());
                        for (MapHelper.Place place :
                                places) {
                            addList.add(String.format("%s\n%s", place.getName(), place.getAddress()));
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("周边")
                                .setItems(addList.toArray(new String[0]), null)
                                .show();
                    }
                }, new MapHelper.OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                });


                break;
            case R.id.city_btn:
                break;
        }
    }
}
