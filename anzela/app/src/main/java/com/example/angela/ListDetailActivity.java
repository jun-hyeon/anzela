package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView leftArrow,message;
    GoogleMap googleMap;
    EditText comment_editText;
    TextView detailTitle,content,cruCnt,startDate,startPoint,endPoint,cmtCnt,regDate;
    CircleImageView userProfile;
    TextView userId;
    TextView cmId,cmContent,depth,cmRegDate,cmUserId;
    TextView rightBtn,leftBtn;
    DetailPost detailPost;
    double startLat, startLng, endLat, endLng;
    String profileUrl, cmProfileUrl;
    int detailId;
    Thread t1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        Server server = new Server();
        intent = getIntent();
        detailId = intent.getIntExtra("id",-1);



        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        message = (ImageView) findViewById(R.id.message);

        comment_editText = (EditText) findViewById(R.id.comment_editText);

        detailTitle = (TextView) findViewById(R.id.detailTitle);
        rightBtn = (TextView) findViewById(R.id.rightBtn);
        leftBtn = (TextView) findViewById(R.id.leftBtn);
        content = (TextView) findViewById(R.id.content);
        userId = (TextView) findViewById(R.id.userId);
        cruCnt = (TextView) findViewById(R.id.cruCnt);
        startDate = (TextView) findViewById(R.id.startDate);
        startPoint = (TextView) findViewById(R.id.startPoint);
        endPoint = (TextView) findViewById(R.id.endPoint);
        cmtCnt = (TextView) findViewById(R.id.cmtCnt);
        regDate = (TextView) findViewById(R.id.regDate);
        userProfile = (CircleImageView) findViewById(R.id.userProfile);


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        comment_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    message.setBackground(ContextCompat.getDrawable(ListDetailActivity.this,R.drawable.circle_black));
                    message.setImageResource(R.drawable.ic_comment_mint);
                }else{
                    message.setBackground(ContextCompat.getDrawable(ListDetailActivity.this,R.drawable.circle_aqua_marine));
                    message.setImageResource(R.drawable.ic_comment_white);
                }
            }
        });

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    detailPost = server.getDetail(detailId);
                    detailTitle.setText(detailPost.getTitle());
                    userId.setText(detailPost.getUid());
                    content.setText(detailPost.getContent());
                    cruCnt.setText("최대 "+detailPost.getCruCnt()+"명");
                    profileUrl = detailPost.getProfileUrl();

                    startDate.setText(detailPost.getStartDate().substring(0,detailPost.getStartDate().indexOf(" ")));
                    startPoint.setText(detailPost.getStartPoint());
                    endPoint.setText(detailPost.getEndPoint());
                    if(detailPost.getEndPoint().equals(null)){
                        endPoint.setText("정해지지 않았습니다.");
                    }
                    startLat = detailPost.getStartLat();
                    startLng = detailPost.getStartLng();
                    endLat = detailPost.getEndLat();
                    endLng = detailPost.getEndLng();
                    cmtCnt.setText("댓글 "+detailPost.getCmtCnt()+"개");
                    regDate.setText(detailPost.getRegDate());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(ListDetailActivity.this).load(profileUrl).into(userProfile);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng position = new LatLng(37.56, 126.97);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

    }
}