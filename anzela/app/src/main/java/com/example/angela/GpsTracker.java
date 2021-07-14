package com.example.angela;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class GpsTracker implements LocationListener {

    private final Context mContext;
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;  //최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;       //최소 GPS 정보 업데이트 시간 밀리세컨드 60분
    protected LocationManager locationManager;  //위치 서비스에 접근 가능하게 함

    public GpsTracker(Context context){  //생성자
        this.mContext = context;
        getLocation();
    }



    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE); //로케이션 서비스를 이용하는

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //gps사용이 가능한가
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //인터넷 사용이 가능한가

            if (!isGPSEnabled && !isNetworkEnabled) {   //gps와 인터넷을 사용할 수 없다면

            } else {                                    //사용할 수 있다면



                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,          //PERMISSION_GRANTED 또는 PERMISSION_DENINED인지 변수에 저장
                        Manifest.permission.ACCESS_FINE_LOCATION);                                   //ACCESS_FINE_LOCATION ACCESS_COARSE_LOCATION보다 정확한 위치 정보 제공
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,        //ACCESS_COARSE_LOCATION 도시 블록 내에 위치 정확성 제공
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {    //둘다 PERMISSION_GRANTED라면


                } else

                    return null;


                if (isNetworkEnabled) {

                    //로케이션 매니저 설정 NETWORK_POVIDER는 근처 기지국이나 와이파이를 받아와 위치를 결정함
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //마지막 장소
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled)
                {
                    if (location == null) //위치값이 없다면 위치 정보 가져오기
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this); //정보 업데이트
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //마지막 위치
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d("@@@", ""+e.toString());
        }

        return location;
    }

    public  double getLatitude(){ // 위도 설정
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude(){  //경도 설정
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public void stopUsingGps(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
