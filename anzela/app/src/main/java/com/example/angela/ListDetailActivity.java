package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    TextView detailTitle,content,cruCnt,startDate,startPoint,endPoint,cmtCnt,regDate,deleteBtn;
    CircleImageView userProfile;
    TextView userId;
    TextView rightBtn,leftBtn;
    Post detailPost;
    double startLat, startLng, endLat, endLng;
    String profileUrl;
    int detailId;
    Intent intent;
    ArrayList<Comment> comments;
    RecyclerView commentsRecyclerView;
    LinearLayout addComments;
    CommentsAdapter commentsAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        Server server = new Server();
        intent = getIntent();
        detailId = intent.getIntExtra("id",0);
        Log.e("DETAILID",""+detailId);


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
        deleteBtn = (TextView) findViewById(R.id.deleteBtn);

        userProfile = (CircleImageView) findViewById(R.id.userProfile);

        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);

        addComments = (LinearLayout) findViewById(R.id.addComments);

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

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    detailPost = server.getDetail(detailId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detailTitle.setText(detailPost.getTitle());
                            userId.setText(detailPost.getUser().getUid());
                            content.setText(detailPost.getContent());

                            if(detailPost.getCurCnt() < 0){
                                cruCnt.setText("제한없음");
                            }else{
                                cruCnt.setText("최대 "+detailPost.getCurCnt()+"명");
                            }

                            profileUrl = detailPost.getUser().getProfileUrl();
                            startDate.setText(detailPost.getStartDate().substring(0,detailPost.getStartDate().indexOf(" ")));
                            startPoint.setText(detailPost.getStartPoint());
                            if(detailPost.getEndPoint().equals("정해지지 않았습니다.")){
                            endPoint.setTextColor(ContextCompat.getColor(ListDetailActivity.this,R.color.very_light_pink));
                            }
                            endPoint.setText(detailPost.getEndPoint());

                            startLat = detailPost.getStartLat();
                            startLng = detailPost.getStartLng();
                            endLat = detailPost.getEndLat();
                            endLng = detailPost.getEndLng();
                            regDate.setText(detailPost.getRegDate());
                            cmtCnt.setText("댓글 "+detailPost.getCmtCnt()+"개");


                            comments = detailPost.getComments();
                            commentsAdapter = new CommentsAdapter(comments);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            commentsRecyclerView.setLayoutManager(layoutManager);
                            commentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            commentsRecyclerView.setAdapter(commentsAdapter);

                            if (comments.size() == 0){
                                addComments.setVisibility(View.VISIBLE);
                                commentsRecyclerView.setVisibility(View.GONE);
                            }else{
                                commentsRecyclerView.setVisibility(View.VISIBLE);
                                addComments.setVisibility(View.GONE);
                            }


                            Glide.with(ListDetailActivity.this).load(profileUrl).into(userProfile);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modify = new Intent(ListDetailActivity.this,EditInputActivity.class);
                modify.putExtra("id",detailId);
                startActivity(modify);
                Log.e("DETAILACTIVITY","id" + detailId);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       server.postDelete(detailId);
                   }
               }).start();

                finish();
            }
        });


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String content = comment_editText.getText().toString();
                            server.postComment(detailId,content);


                            detailPost = server.getDetail(detailId);
                            comments = detailPost.getComments();
                            commentsAdapter.setArrayList(comments);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commentsAdapter.notifyDataSetChanged();
                                    comment_editText.setText(" ");
                                    if (comments.size() == 0){
                                        addComments.setVisibility(View.VISIBLE);
                                        commentsRecyclerView.setVisibility(View.GONE);
                                    }else{
                                        commentsRecyclerView.setVisibility(View.VISIBLE);
                                        addComments.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng position = new LatLng(37.56, 126.97);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

    }
}