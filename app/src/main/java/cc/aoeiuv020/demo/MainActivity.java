package cc.aoeiuv020.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Switch mChinaCheckBox;
    private TextView mLocationTextView;
    private LocationClient mLocationClient;                 // 声明LocationClient类
    private double mLongitude;
    private double mLatitude;
    private BDAbstractLocationListener mMyLocationListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int resultCode = 0;
            if (location != null) {
                resultCode = location.getLocType();
            }
            // 百度定位失败
            if (resultCode != BDLocation.TypeGpsLocation && resultCode != BDLocation.TypeCacheLocation
                    && resultCode != BDLocation.TypeOffLineLocation && resultCode != BDLocation.TypeNetWorkLocation) {
                Log.d(TAG, "百度定位失败");
                mLocationClient.stop();
                return;
            }

            // 百度定位成功
            mLocationClient.stop();
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();
            String mAddress = location.getAddrStr();
            String mProvinceName = location.getProvince();
            String mCityName = location.getCity();
            String mDistrictName = location.getDistrict();
            mLocationTextView.setText(mCityName);

            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                Log.d(TAG, "百度定位信息  Address:" + mAddress);
                Log.d(TAG, "百度定位信息  City:" + mCityName + "  CityCode:" + location.getCityCode() + "  区：" + location.getDistrict());
            }
            Log.d(TAG, "百度定位信息  mLongitude:" + mLongitude + "  mLatitude:" + mLatitude);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.select_btn).setOnClickListener(this);
        findViewById(R.id.map_btn).setOnClickListener(this);
        findViewById(R.id.image_btn).setOnClickListener(this);
        findViewById(R.id.poi_btn).setOnClickListener(this);
        mChinaCheckBox = findViewById(R.id.china_cb);
        mLocationTextView = findViewById(R.id.location_tv);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mMyLocationListener); // 注册监听函数
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
                if (isChina) {
                    LocationClientOption option = new LocationClientOption();
                    option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);           // 设置定位模式
                    option.setCoorType("bd09ll");                                  // 返回的定位结果是百度经纬度,默认值gcj02
                    option.setScanSpan(5000);                                      // 设置发起定位请求的间隔时间为10s
                    option.setIsNeedAddress(true);
                    option.setNeedDeviceDirect(false);
                    mLocationClient.setLocOption(option);

                    mLocationClient.start();
                } else {
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
                    lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mLongitude = location.getLongitude();
                            mLatitude = location.getLatitude();
                            Log.d(TAG, "谷歌定位信息  mLongitude:" + mLongitude + "  mLatitude:" + mLatitude);
                        }
                    });
                }
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
                    currentPlace.addOnSuccessListener(this, new OnSuccessListener<PlaceLikelihoodBufferResponse>() {
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
