package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListAroundActivity extends AppCompatActivity {

    ImageView leftArrow;
    TextView aroundWrite;
    TextView setting;
    RecyclerView aroundRecyclerview;
    LinearLayout noList_around;
    NestedScrollView aroundList_scrollView;
    Server Around;
    PostAdapter postAdapter;
    ArrayList<Post> AroundList;
    private  GpsTracker gpsTracker;
    double lat, lon;
    int page = 1;
    RecyclerView.LayoutManager layoutManager;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    String [] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}; //퍼미션을 배열로 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_around);

         Around = new Server();

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        aroundWrite = (TextView) findViewById(R.id.aroundwrite);
        setting = (TextView) findViewById(R.id.setting);
        aroundRecyclerview = (RecyclerView) findViewById(R.id.aroundRecyclerView);
        noList_around = (LinearLayout) findViewById(R.id.noList_around);
        aroundList_scrollView = (NestedScrollView) findViewById(R.id.aroundList_scrollView);

        SpannableString settingline = new SpannableString("설정하기");
        settingline.setSpan(new UnderlineSpan(), 0, settingline.length(), 0);

        setting.setText(settingline);


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        aroundWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goWrite = new Intent(getApplicationContext(), WriteInputActivity.class);
                goWrite.putExtra("state",1);
                startActivity(goWrite);

            }
        });



        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationSetting();
            }
        });

        if(checkLocationServicesStatus()){
            gpsTracker = new GpsTracker(ListAroundActivity.this);
            double latitude = gpsTracker.getLatitude();                                                 //위도 경도 설정
            double longitude = gpsTracker.getLongitude();

            String address = getCurrentAddress(latitude,longitude);                                     //주소 가져오기
            setting.setText(address);
            setting.setTextColor(ContextCompat.getColor(ListAroundActivity.this,R.color.brown_grey));
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                     lat = gpsTracker.getLatitude();
                     lon = gpsTracker.getLongitude();
                     AroundList = Around.getAround(lat, lon,page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             postAdapter = new PostAdapter(AroundList);
                             layoutManager = new LinearLayoutManager(getApplicationContext());
                            aroundRecyclerview.setLayoutManager(layoutManager);
                            aroundRecyclerview.setItemAnimator(new DefaultItemAnimator());
                            aroundRecyclerview.setAdapter(postAdapter);
                            if(AroundList.size() == 0){
                                aroundRecyclerview.setVisibility(View.GONE);
                                noList_around.setVisibility(View.VISIBLE);
                            }else{
                                aroundRecyclerview.setVisibility(View.VISIBLE);
                                noList_around.setVisibility(View.GONE);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
    }


    void onLocationSetting(){
        if(!checkLocationServicesStatus()){                                                 //위치가 꺼져있으면 위치 킬수 있도록 설정으로 이동
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();                                                        //위치가 켜져있으면 위치 정보 제공 동의
        }
        gpsTracker = new GpsTracker(ListAroundActivity.this);
        double latitude = gpsTracker.getLatitude();                                                 //위도 경도 설정
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude,longitude);                                     //주소 가져오기

        setting.setText(address);
        setting.setTextColor(ContextCompat.getColor(ListAroundActivity.this,R.color.brown_grey));

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
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료 2 가지 경우가 있음

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(ListAroundActivity.this, "위치 정보 제공은 필수입니다.", Toast.LENGTH_LONG).show();
                    showDialogForPermission();                                                                           //위치정보제공을 안할 경우 종료


                } else {

                    Toast.makeText(ListAroundActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                    showDialogForReject();
                }
            }
        }
    }


    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크 checkSelfPermission 앱에 특정 권한을 부여했는지 확인 권한이 있으면 Permission_GRANTED를 반환, 아니면 Permission_DENINED를 반환
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(ListAroundActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(ListAroundActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            //   위치 값을 가져올 수 있음

        } else {  // 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale( ListAroundActivity.this,REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(ListAroundActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(ListAroundActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);  //퍼미션 요청


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(ListAroundActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    public void Onscroll() {
        aroundList_scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    Log.e("SCROLL", "SCROLLLLLL" );
                    new Thread(() -> {
                        try {
                            page++;
                            AroundList.addAll(Around.getAround(lat,lon,page));
//                                allAdapter.setArrayList(allList);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(postAdapter.arrayList.size()>0)
                                        postAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Onscroll();
    }

    public String getCurrentAddress(double latitude, double longitude) {

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
            return"지오코더 서비스 사용불가";
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ListAroundActivity.this);             // Alert Dialog를 띄움
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
        AlertDialog.Builder pBuilder= new AlertDialog.Builder(ListAroundActivity.this );           //한 번 더 물어본다.
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
        AlertDialog.Builder rBuilder = new AlertDialog.Builder(ListAroundActivity.this);
        rBuilder.setTitle("위치 권한 막힘");
        rBuilder.setCancelable(true);
        rBuilder.setMessage("이 앱을 이용하기 위해서는 위치 권한이 필수입니다. 위치권한을 설정하시겠습니까?");
        rBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goPermission =
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(goPermission);
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
    protected void onRestart() {
        super.onRestart();
        Server Around = new Server();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                   AroundList.clear();
                   AroundList.addAll(Around.getAround(lat, lon,1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(AroundList.size() == 0){
                                aroundRecyclerview.setVisibility(View.GONE);
                                noList_around.setVisibility(View.VISIBLE);
                            }else{
                                aroundRecyclerview.setVisibility(View.VISIBLE);
                                noList_around.setVisibility(View.GONE);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();

    }
}