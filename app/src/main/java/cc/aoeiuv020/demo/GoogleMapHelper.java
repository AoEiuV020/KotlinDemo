package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GoogleMapHelper extends MapHelper {
    private static final String TAG = "GoogleMapHelper";
    @SuppressLint("StaticFieldLeak")
    private static GoogleMapHelper INSTANCE;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMapHelper(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static GoogleMapHelper getInstance(Context context) {
        // 单例用懒加载，为了传入context,
        if (INSTANCE == null) {
            synchronized (GoogleMapHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GoogleMapHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getStaticImage(LatLng latLng) {
        return "http://maps.googleapis.com/maps/api/staticmap?" +
                "center=" + latLng.getLatitude() + "," + latLng.getLongitude() +
                "&size=640x480&markers=color:blue%7Clabel:S%7C62.107733,-145.541936" +
                "&zoom=15";
    }

    @Override
    public void requestLatLng(final OnSuccessListener<LatLng> onSuccessListener, final OnErrorListener onErrorListener) {
        final Task<Location> lastLocation;
        try {
            lastLocation = fusedLocationProviderClient.getLastLocation();
        } catch (SecurityException e) {
            if (onErrorListener != null) {
                onErrorListener.onError(new RuntimeException("没有位置权限,", e));
            }
            return;
        }
        lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                // 设备上的google service出意外可能导致这个task成功但是location为空，
                if (!task.isSuccessful() || location == null) {
                    if (onErrorListener != null) {
                        onErrorListener.onError(new RuntimeException("定位失败,", task.getException()));
                    }
                    return;
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess(latLng);
                }
            }
        });
    }

    @Override
    public void requestPlaceList(LatLng latLng, final OnSuccessListener<List<Place>> onSuccessListener, final OnErrorListener onErrorListener) {
        PlaceDetectionClient placeDetectionClient = Places.getPlaceDetectionClient(context);
        // TODO: 这是获取了当前位置的周边信息，没用到参数的经纬度，
        @SuppressLint("MissingPermission") Task<PlaceLikelihoodBufferResponse> currentPlace = placeDetectionClient.getCurrentPlace(null);
        currentPlace.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                if (!task.isSuccessful()) {
                    if (onErrorListener != null) {
                        onErrorListener.onError(new RuntimeException("获取周边位置失败,", task.getException()));
                    }
                    return;
                }
                PlaceLikelihoodBufferResponse placeLikelihoods = task.getResult();
                if (placeLikelihoods == null) {
                    if (onErrorListener != null) {
                        onErrorListener.onError(new RuntimeException("获取周边位置失败,", task.getException()));
                    }
                    return;
                }
                List<Place> placeList = new ArrayList<>(placeLikelihoods.getCount());
                for (int i = 0; i < placeLikelihoods.getCount(); i++) {
                    com.google.android.gms.location.places.Place gPlace = placeLikelihoods.get(i).getPlace();
                    String name = gPlace.getName().toString();
                    // 以防万一避免空指针，
                    String address = "" + gPlace.getAddress();
                    LatLng placeLatLng = new LatLng(gPlace.getLatLng().latitude, gPlace.getLatLng().longitude);
                    Place place = new Place(name, address, placeLatLng);
                    placeList.add(place);
                }
                if (onSuccessListener != null) {
                    onSuccessListener.onSuccess(placeList);
                }
            }
        });
    }

    @Override
    public void requestCityName(LatLng latLng, final OnSuccessListener<String> onSuccessListener, final OnErrorListener onErrorListener) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1);
        } catch (IOException e) {
            if (onErrorListener != null) {
                onErrorListener.onError(new RuntimeException("获取位置失败,", e));
            }
            return;
        }
        if (addressList == null || addressList.isEmpty()) {
            if (onErrorListener != null) {
                onErrorListener.onError(new RuntimeException("获取位置失败,"));
            }
            return;
        }
        /*
            Address[addressLines=[0:"中国广东省深圳市宝安区五和大道 邮政编码: 518000"],
            feature=五和大道,admin=广东省,sub-admin=null,locality=深圳市,thoroughfare=五和大道,
            postalCode=518000,countryCode=CN,countryName=中国,hasLatitude=true,latitude=22.6032427,
            hasLongitude=true,longitude=114.05754379999999,phone=null,url=null,extras=null]
         */
        Address address = addressList.get(0);
        if (address == null || address.getLocality() == null) {
            if (onErrorListener != null) {
                onErrorListener.onError(new RuntimeException("获取位置失败,"));
            }
            return;
        }
        if (onSuccessListener != null) {
            onSuccessListener.onSuccess(address.getLocality());
        }
    }

    @Override
    public Picker getPicker(Context context) {
        return new GoogleMapPicker(context);
    }


    private class GoogleMapPicker extends Picker implements OnMapReadyCallback {
        private MapView mapView;
        private Context context;
        private GoogleMap googleMap;
        private OnMapReadyListener onMapReadyListener;

        private GoogleMapPicker(Context context) {
            this.context = context;
        }

        private void createMapView() {
            if (mapView == null) {
                mapView = new MapView(context);
                mapView.setClickable(true);
                mapView.setFocusable(true);
            }
        }

        @Override
        public void attack(FrameLayout container, OnMapReadyListener listener) {
            Log.d(TAG, "attack: ");
            this.onMapReadyListener = listener;
            createMapView();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(mapView, params);
            mapView.getMapAsync(this);
        }

        @Override
        public void moveMap(LatLng latLng, boolean anim) {
            if (googleMap == null) {
                Log.e(TAG, "moveMap: 移动地图失败，", new RuntimeException("谷歌地图还没准备好，"));
                return;
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude()),
                    15f);
            if (anim) {
                googleMap.animateCamera(cameraUpdate);
            } else {
                googleMap.moveCamera(cameraUpdate);
            }
        }

        @Override
        public void onMapReady(final GoogleMap googleMap) {
            Log.d(TAG, "onMapReady() called with: googleMap = [" + googleMap + "]");
            this.googleMap = googleMap;
            googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int reason) {
                    if (REASON_GESTURE != reason) {
                        // 不是用户拖出来的事件无视，
                        Log.d(TAG, "onCameraMoveStarted() called with: i = [" + reason + "]");
                        return;
                    }
                    MapStatus mapStatus = new MapStatus();
                    com.google.android.gms.maps.model.LatLng target = googleMap.getCameraPosition().target;
                    mapStatus.target = new LatLng(target.latitude, target.longitude);
                    onMapStatusChangeListener.onMapStatusChangeStart(mapStatus);
                }
            });
            googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    MapStatus mapStatus = new MapStatus();
                    com.google.android.gms.maps.model.LatLng target = googleMap.getCameraPosition().target;
                    mapStatus.target = new LatLng(target.latitude, target.longitude);
                    onMapStatusChangeListener.onMapStatusChange(mapStatus);
                }
            });
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    MapStatus mapStatus = new MapStatus();
                    com.google.android.gms.maps.model.LatLng target = googleMap.getCameraPosition().target;
                    mapStatus.target = new LatLng(target.latitude, target.longitude);
                    onMapStatusChangeListener.onMapStatusChangeFinish(mapStatus);

                }
            });
/*
            googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                @Override
                public void onCameraMoveCanceled() {
                    MapStatus mapStatus = new MapStatus();
                    com.google.android.gms.maps.model.LatLng target = googleMap.getCameraPosition().target;
                    mapStatus.target = new LatLng(target.latitude, target.longitude);
                    onMapStatusChangeListener.onMapStatusChangeFinish(mapStatus);
                }
            });
*/
            if (onMapReadyListener != null) {
                onMapReadyListener.ready();
            }
        }

        @Override
        void create() {
            super.create();
            createMapView();
            mapView.onCreate(null);
        }

        @Override
        void start() {
            super.start();
            mapView.onStart();
        }

        @Override
        void resume() {
            super.resume();
            mapView.onResume();
        }

        @Override
        void pause() {
            super.pause();
            mapView.onPause();
        }

        @Override
        void stop() {
            super.stop();
            mapView.onStop();
        }

        @Override
        void destroy() {
            super.destroy();
            mapView.onDestroy();
        }
    }

}
