package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cc.aoeiuv020.demo.MapHelper.MapType.BAIDU;

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
    abstract public void currentLatLng(OnSuccessListener<LatLng> onSuccessListener,
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

    @SuppressWarnings("WeakerAccess")
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
}
