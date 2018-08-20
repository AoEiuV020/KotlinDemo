package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RequiresPermission;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cc.aoeiuv020.demo.MapHelper.MapType.BAIDU;

@SuppressWarnings("WeakerAccess")
public abstract class MapHelper {

    private static MapType type = BAIDU;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void setType(MapType type) {
        MapHelper.type = type;
    }

    public static void initContext(Context context) {
        MapHelper.context = context.getApplicationContext();
    }

    public static MapHelper getInstance() {
        MapHelper result = null;
        switch (type) {
            case BAIDU:
                result = BaiduMapHelper.getInstance(context);
                break;
            case GOOGLE:
                result = GoogleMapHelper.getInstance(context);
                break;
        }
        return result;
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    abstract public void requestLatLng(OnSuccessListener<LatLng> onSuccessListener,
                                       OnErrorListener onErrorListener) throws SecurityException;

    public void requestPlaceList(final OnSuccessListener<List<Place>> onSuccessListener,
                                 final OnErrorListener onErrorListener) throws SecurityException {
        requestLatLng(new OnSuccessListener<LatLng>() {
            @Override
            public void onSuccess(LatLng latLng) {
                requestPlaceList(latLng, onSuccessListener, onErrorListener);
            }
        }, onErrorListener);
    }

    /**
     * 请求周边位置信息，
     *
     * @param latLng            经纬度，不可空，
     * @param onSuccessListener 成功回调，可空，
     * @param onErrorListener   失败回调，可空，
     */
    abstract public void requestPlaceList(LatLng latLng,
                                          OnSuccessListener<List<Place>> onSuccessListener,
                                          OnErrorListener onErrorListener);

    public enum MapType {
        BAIDU, GOOGLE
    }

    public interface OnSuccessListener<T> {
        void onSuccess(T t);
    }

    public interface OnErrorListener {
        void onError(Throwable t);
    }

    public static class LatLng {
        private double latitude;
        private double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static class Place {
        private String name;
        private String address;
        private LatLng latLng;

        public Place(String name, String address, LatLng latLng) {
            this.name = name;
            this.address = address;
            this.latLng = latLng;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public LatLng getLatLng() {
            return latLng;
        }
    }
}
