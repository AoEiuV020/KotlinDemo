package cc.aoeiuv020.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private TextView mLocationTextView;
    private LocationClient mLocationClient;                 // 声明LocationClient类
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
            double mLongitude = location.getLongitude();
            double mLatitude = location.getLatitude();
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
        mLocationTextView = findViewById(R.id.location_tv);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mMyLocationListener); // 注册监听函数
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_btn:
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);           // 设置定位模式
                option.setCoorType("bd09ll");                                  // 返回的定位结果是百度经纬度,默认值gcj02
                option.setScanSpan(5000);                                      // 设置发起定位请求的间隔时间为10s
                option.setIsNeedAddress(true);
                option.setNeedDeviceDirect(false);
                mLocationClient.setLocOption(option);

                mLocationClient.start();
                break;
            case R.id.map_btn:
                Intent intent = new Intent(this, BaiduMapActivity.class);
                startActivity(intent);
                break;
            case R.id.image_btn:
                break;
        }
    }
}
