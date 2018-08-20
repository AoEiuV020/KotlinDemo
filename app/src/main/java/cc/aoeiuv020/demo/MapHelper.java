package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.List;

import static cc.aoeiuv020.demo.MapHelper.MapType.BAIDU;

@SuppressWarnings("WeakerAccess")
public abstract class MapHelper {
    private static final String TAG = "MapHelper";
    private static MapType sMapType = BAIDU;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void setMapType(MapType type) {
        MapHelper.sMapType = type;
    }

    public static void initContext(Context context) {
        MapHelper.context = context.getApplicationContext();
    }

    public static MapHelper getInstance() {
        return getInstance(sMapType);
    }

    public static MapHelper getInstance(MapType mapType) {
        MapHelper result = null;
        switch (mapType) {
            case BAIDU:
                result = BaiduMapHelper.getInstance(context);
                break;
            case GOOGLE:
                result = GoogleMapHelper.getInstance(context);
                break;
        }
        return result;
    }

    /**
     * TODO: 经纬度标准没有统一，百度的经纬度给谷歌不准，
     *
     * @param onSuccessListener 成功回调，可空，
     * @param onErrorListener   成功回调，可空，
     * @throws SecurityException 没有位置权限，
     */
    abstract public void requestLatLng(OnSuccessListener<LatLng> onSuccessListener,
                                       OnErrorListener onErrorListener);

    /**
     * 请求周边位置信息，
     *
     * @param onSuccessListener 成功回调，可空，
     * @param onErrorListener   失败回调，可空，
     */
    public final void requestPlaceList(final OnSuccessListener<List<Place>> onSuccessListener,
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

    /**
     * TODO: 城市名称字符串不能保证和百度的一致，语言和是否包含“市”字样都没测试，
     *
     * @param latLng            经纬度，不可空，
     * @param onSuccessListener 成功回调，可空，
     * @param onErrorListener   失败回调，可空，
     */
    abstract public void requestCityName(LatLng latLng,
                                         OnSuccessListener<String> onSuccessListener,
                                         OnErrorListener onErrorListener);

    abstract public Picker getPicker(Context context);

    /**
     * 子类继承封装选择位置视图相关一切，
     */
    public static abstract class Picker implements LifecycleObserver {

        public abstract void attack(FrameLayout container);

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void create() {
            Log.d(TAG, "create: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void start() {
            Log.d(TAG, "start: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void resume() {
            Log.d(TAG, "resume: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void pause() {
            Log.d(TAG, "pause: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void stop() {
            Log.d(TAG, "stop: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void destroy() {
            Log.d(TAG, "destroy: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        void any() {
            Log.d(TAG, "any: ");
        }
    }

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
