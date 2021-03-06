package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    RecyclerView timelineRinding;
    RecyclerView aroundRiding;
    NavigationView navi;
    DrawerLayout drawerLayout;
    ImageView menu;
    ImageView close;
    ImageView rightArrow, weatherIcon;
    TextView all,around,timeLine,locationText,weatherTemp,weatherCondition,textdo,aroundRidingTitle,ridingScheduleTitle;
    CircleImageView mainUserprofile;
    RelativeLayout weatherLayout;
    LinearLayout allLinear,aroundLinear,timelineLinear;
    int page = 1;
    private  GpsTracker gpsTracker;
    long backBtnTime = 0;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;



    String [] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //퍼미션을 배열로 저장


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Weather weather = new Weather();

        Server server = new Server();



        navi = (NavigationView)findViewById(R.id.navigationView);

        timelineRinding = (RecyclerView) findViewById(R.id.timelineRiding);
        aroundRiding = (RecyclerView) findViewById(R.id.aroundRiding);

        drawerLayout  = (DrawerLayout)findViewById(R.id.drawer);
        weatherLayout = (RelativeLayout)findViewById(R.id.weatherLayout);

        menu = (ImageView) findViewById(R.id.menu);
        close = (ImageView) findViewById(R.id.close);

        all = (TextView) findViewById(R.id.all);
        around = (TextView) findViewById(R.id.around);
        timeLine = (TextView) findViewById(R.id.timeLine);
        locationText = (TextView) findViewById(R.id.locationText);
        weatherCondition = (TextView) findViewById(R.id.weatherCondition);
        weatherTemp = (TextView) findViewById(R.id.weatherTemp);
        textdo = (TextView) findViewById(R.id.textdo);
        ridingScheduleTitle = (TextView) findViewById(R.id.ridingScheduleTitle);
        aroundRidingTitle = (TextView) findViewById(R.id.aroundRidingTitle);

        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        allLinear = (LinearLayout) findViewById(R.id.allLinear);
        aroundLinear = (LinearLayout) findViewById(R.id.aroundLinear);
        timelineLinear = (LinearLayout) findViewById(R.id.timeLineLinear);
        mainUserprofile = (CircleImageView) findViewById(R.id.mainUserprofile);

        if(checkLocationServicesStatus()){
            gpsTracker = new GpsTracker(MainActivity.this);
            double latitude = gpsTracker.getLatitude();                                                 //위도 경도 설정
            double longitude = gpsTracker.getLongitude();

            String address = getCurrentAddress(latitude,longitude);                                     //주소 가져오기
            locationText.setText(address);
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double lat = gpsTracker.getLatitude();
                    double lon = gpsTracker.getLongitude();
                    Log.e("LatLon"," "+lat +" "+lon);
                    ArrayList<Post> postList = server.getAround(lat,lon,page);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            PostAdapter postAdapter = new PostAdapter(postList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            aroundRiding.setLayoutManager(layoutManager);
                            aroundRiding.setItemAnimator(new DefaultItemAnimator());
                            aroundRiding.setAdapter(postAdapter);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    weather.func();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherCondition.setText(weather.getSky_value());
                            weatherTemp.setText(weather.getTemp_value()+"°");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> soonList =  server.getSoon(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(soonList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        timelineRinding.setLayoutManager(layoutManager);
                        timelineRinding.setItemAnimator(new DefaultItemAnimator());
                        timelineRinding.setAdapter(timeLineAdapter);
                        timeLineAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationSetting();

            }
        });



        allLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent goAll = new Intent(getApplicationContext(),ListAllActivity.class);
                startActivity(goAll);

            }
        });

        aroundLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent goAround = new Intent(getApplicationContext(),ListAroundActivity.class);
                startActivity(goAround);

            }
        });

        timelineLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent goTimeline = new Intent(getApplicationContext(),ListTimelineActivity.class);
                startActivity(goTimeline);

            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent goAllList = new Intent(MainActivity.this,ListAllActivity.class);
            startActivity(goAllList);

            }
        });
    }

    void onLocationSetting(){
        if(!checkLocationServicesStatus()){                                                 //위치가 꺼져있으면 위치 킬수 있도록 설정으로 이동
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();                                                        //위치가 켜져있으면 위치 정보 제공 동의
        }
        gpsTracker = new GpsTracker(MainActivity.this);
        double latitude = gpsTracker.getLatitude();                                                 //위도 경도 설정
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude,longitude);                                     //주소 가져오기
        locationText.setText(address);
        //Toast.makeText(MainActivity.this,"위도 " + latitude + "\n경도 "+ longitude,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);  //onRequestpermissionResult 함수를 상속? 불러온다

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                onLocationSetting();
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료 2 가지 경우가 있음

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "위치 정보 제공은 필수입니다.", Toast.LENGTH_LONG).show();
                    showDialogForPermission();                                                                           //위치정보제공을 안할 경우 종료


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                    showDialogForReject();
                }
            }
        }
    }


    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크 checkSelfPermission 앱에 특정 권한을 부여했는지 확인 권한이 있으면 Permission_GRANTED를 반환, 아니면 Permission_DENINED를 반환
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            //   위치 값을 가져올 수 있음

        } else {  // 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);  //퍼미션 요청

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }



    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault()); //위도 경도를 주소로 변환

        List<Address> addresses; //주소를 저장할 arraylist

        try {
            addresses = geocoder.getFromLocation( //위치 정보를 가져옴
                    latitude,
                    longitude,
                    5); //7
        } catch (IOException ioException) {                                                         //에러가 났을 경우 예외처리
            //네트워크 문제
//            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
            //GPS 좌표문제
        } catch (IllegalArgumentException illegalArgumentException) {
//            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
            //주소문제
        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "설정하기";
        }

        Address address = addresses.get(0);
        String shortAdddress = address.getAdminArea()+ " " + address.getSubLocality();  // 어드레스의 시와 구만 가져와 표시하기
        return shortAdddress;

    }


    //비활성화 일 때 Gps활성화를 물어보는 메소드
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);             // Alert Dialog를 띄움
        builder.setTitle("위치 서비스 활성화");                                                           //title 정하기
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"+"위치 설정을 수정하시겠습니까?");         // message 설정
        builder.setCancelable(true);                                                                 //back버튼을 쓸 수 있다.
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent
                        = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
                dialog.dismiss();
            }
        });
        builder.create().show();                                                                        //AlertDialog를 보여주기
    }

    private  void showDialogForPermission(){                                                        //위치정보 제공동의 거부했을 때 뜨는 화면
        AlertDialog.Builder pBuilder= new AlertDialog.Builder(MainActivity.this );           //한 번 더 물어본다.
        pBuilder.setTitle("위치 정보제공");
        pBuilder.setMessage("이 앱을 이용하기 위해서는 위치 정보 제공이 필수입니다.");
        pBuilder.setCancelable(true );
        pBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
                checkRunTimePermission();
            }
        });

        pBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        pBuilder.create().show();
    }

    private  void showDialogForReject(){
        AlertDialog.Builder rBuilder = new AlertDialog.Builder(MainActivity.this);
        rBuilder.setTitle("위치 권한 막힘");
        rBuilder.setCancelable(true);
        rBuilder.setMessage("이 앱을 이용하기 위해서는 위치 권한이 필수입니다. 위치권한을 설정하시겠습니까?");
        rBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goPermission =
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:" + getPackageName()));
                        startActivity(goPermission);
                        finish();
            }
        });

        rBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        rBuilder.create().show();
    }

    //요청코드가 맞을 때 GPS가 활성화 되었는 지
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data); //   액티비티에서 데이터를 가져옴

        switch (requestCode){

            case GPS_ENABLE_REQUEST_CODE:

                    if(checkLocationServicesStatus()){                                              //정보를 가져 올 수 있을 때
                        Log.d("@@@","onActivityResult : GPS 활성화 되었음");
                        checkRunTimePermission();                                                   //위치정보 제공동의 물어보기
                        return;
                    }

                break;
        }
    }

    //위치 정보관련 퍼미션이 허락 확인
    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Weather weather = new Weather();

        Server server = new Server();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    weather.func();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherCondition.setText(weather.getSky_value());
                            weatherTemp.setText(weather.getTemp_value()+"°");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> soonList =  server.getSoon(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TimeLineAdapter timeLineAdapter = new TimeLineAdapter(soonList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            timelineRinding.setLayoutManager(layoutManager);
                            timelineRinding.setItemAnimator(new DefaultItemAnimator());
                            timelineRinding.setAdapter(timeLineAdapter);
                            timeLineAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double lat = gpsTracker.getLatitude();
                    double lon = gpsTracker.getLongitude();
                    Log.e("LatLon"," "+lat +" "+lon);
                    ArrayList<Post> postList = server.getAround(lat,lon,page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            PostAdapter postAdapter = new PostAdapter(postList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            aroundRiding.setLayoutManager(layoutManager);
                            aroundRiding.setItemAnimator(new DefaultItemAnimator());
                            aroundRiding.setAdapter(postAdapter);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

}

