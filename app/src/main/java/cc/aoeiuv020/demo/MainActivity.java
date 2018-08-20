package cc.aoeiuv020.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Switch mChinaCheckBox;
    private TextView mLocationTextView;
    private double mLongitude;
    private double mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.select_btn).setOnClickListener(this);
        findViewById(R.id.map_btn).setOnClickListener(this);
        findViewById(R.id.image_btn).setOnClickListener(this);
        findViewById(R.id.poi_btn).setOnClickListener(this);
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
            case R.id.select_btn:
                MapHelper.getInstance().currentLatLng(new MapHelper.OnSuccessListener<MapHelper.LatLng>() {
                    @Override
                    public void onSuccess(MapHelper.LatLng latLng) {
                        mLongitude = latLng.getLongitude();
                        mLatitude = latLng.getLatitude();
                        String location = "定位信息  mLongitude:" + mLongitude + "  mLatitude:" + mLatitude;
                        mLocationTextView.setText(location);
                        Log.d(TAG, location);
                    }
                }, new MapHelper.OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                });
                Log.d(TAG, "定位信息  mLongitude:" + mLongitude + "  mLatitude:" + mLatitude);
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
                if (isChina) {
                    GeoCoder geoSearch = GeoCoder.newInstance();
                    geoSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                        /**
                         * 地理编码，由地点名称获取经纬度
                         */
                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult result) {
                        }

                        /**
                         * 反地理编码，由地点经纬度获取地点名称
                         */
                        @Override
                        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                            List<PoiInfo> poiList = result.getPoiList();
                            List<String> addList = new ArrayList<>(poiList.size());
                            for (PoiInfo poi :
                                    poiList) {
                                addList.add(String.format("%s\n%s", poi.name, poi.address));
                            }
                            String name = result.getBusinessCircle();
                            if (TextUtils.isEmpty(name)) {
                                name = result.getSematicDescription();
                            }
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(name)
                                    .setItems(addList.toArray(new String[0]), null)
                                    .show();
                        }
                    });
                    /* 加载定位数据 */
                    LatLng latLng = new LatLng(mLatitude, mLongitude);
                    ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                    reverseGeoCodeOption.location(latLng);
                    geoSearch.reverseGeoCode(reverseGeoCodeOption);
                } else {
                    GeoDataClient geoDataClient = Places.getGeoDataClient(this);
                    PlaceDetectionClient placeDetectionClient = Places.getPlaceDetectionClient(this);
                    Task<PlaceLikelihoodBufferResponse> currentPlace = placeDetectionClient.getCurrentPlace(null);
                    currentPlace.addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onSuccess(PlaceLikelihoodBufferResponse placeLikelihoods) {
                            List<Place> placeList = new ArrayList<>(placeLikelihoods.getCount());
                            for (int i = 0; i < placeLikelihoods.getCount(); i++) {
                                placeList.add(placeLikelihoods.get(i).getPlace());
                            }
                            List<String> addList = new ArrayList<>(placeList.size());
                            for (Place place :
                                    placeList) {
                                addList.add(String.format("%s\n%s", place.getName(), place.getAddress()));
                            }
                            String name = "" + placeLikelihoods.getAttributions();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(name)
                                    .setItems(addList.toArray(new String[0]), null)
                                    .show();

                        }
                    });
                }

                break;
        }
    }
}
