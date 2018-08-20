package cc.aoeiuv020.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class GoogleMapHelper extends MapHelper {
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
                PlaceLikelihoodBufferResponse placeLikelihoods = task.getResult();
                if (!task.isSuccessful() || placeLikelihoods == null) {
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
}
