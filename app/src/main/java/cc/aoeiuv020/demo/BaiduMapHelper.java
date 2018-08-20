package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

public class BaiduMapHelper extends MapHelper {
    private static final String TAG = "BaiduMapHelper";
    @SuppressLint("StaticFieldLeak")
    private static BaiduMapHelper INSTANCE;
    private Context context;
    private LocationClient locationClient;
    private BDAbstractLocationListener locationListener;

    private BaiduMapHelper(Context context) {
        this.context = context;
        locationClient = new LocationClient(context);
    }

    public static BaiduMapHelper getInstance(Context context) {
        // 单例用懒加载，为了传入context,
        if (INSTANCE == null) {
            synchronized (BaiduMapHelper.class) {
                if (INSTANCE == null) {
                    SDKInitializer.initialize(context);
                    INSTANCE = new BaiduMapHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void currentLatLng(final OnSuccessListener<LatLng> onSuccessListener, final OnErrorListener onErrorListener) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);           // 设置定位模式
        option.setCoorType("bd09ll");                                  // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);                                      // 设置发起定位请求的间隔时间为10s
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(false);
        locationClient.setLocOption(option);
        locationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // 只定位一次就停止，
                locationClient.unRegisterLocationListener(this);
                locationClient.stop();
                int resultCode;
                if (location == null) {
                    if (onErrorListener != null) {
                        onErrorListener.onError(new RuntimeException("百度定位失败: location is null,"));
                    }
                    return;
                }
                resultCode = location.getLocType();
                // 百度定位失败
                if (resultCode != BDLocation.TypeGpsLocation && resultCode != BDLocation.TypeCacheLocation
                        && resultCode != BDLocation.TypeOffLineLocation && resultCode != BDLocation.TypeNetWorkLocation) {
                    Log.d(TAG, "百度定位失败");
                    if (onErrorListener != null) {
                        onErrorListener.onError(new RuntimeException("百度定位失败: " + location.getLocTypeDescription()));
                    }
                    return;
                }

                // 百度定位成功
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess(latLng);
                }
            }

        };
        locationClient.registerLocationListener(locationListener);
        locationClient.start();
    }
}
