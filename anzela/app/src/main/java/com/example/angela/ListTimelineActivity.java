package com.example.angela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

public class ListTimelineActivity extends AppCompatActivity {

  //  @BindView(R.id.leftArrow)
    ImageView leftArrow;
   // @BindView(R.id.timelineWrite)
    TextView timelineWrite,timeLinedateText;
    RecyclerView timeLineRecyclerView;
    LinearLayout noList_timeline;
    NestedScrollView soonList_scrollView;
    int page = 1;
    PostAdapter postAdapter;
    Server server;
    ArrayList<Post> timelineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_timeline);
//
       leftArrow = (ImageView) findViewById(R.id.leftArrow);

       timelineWrite = (TextView) findViewById(R.id.timelineWrite);
       timeLinedateText = (TextView) findViewById(R.id.timeLine_dateText);

       timeLineRecyclerView = (RecyclerView) findViewById(R.id.timelineRecycler);

       noList_timeline = (LinearLayout) findViewById(R.id.noList_timeline);

       soonList_scrollView = (NestedScrollView) findViewById(R.id.timelineList_scrollView);

        server = new Server();

        DateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date  date = new Date();
        String strDate = dateFormat.format(date);

        timeLinedateText.setText(strDate);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     timelineList = server.getSoon(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             postAdapter = new PostAdapter(timelineList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            timeLineRecyclerView.setLayoutManager(layoutManager);
                            timeLineRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            timeLineRecyclerView.setAdapter(postAdapter);
                            if(timelineList.size() == 0){
                                timeLineRecyclerView.setVisibility(View.GONE);
                                noList_timeline.setVisibility(View.VISIBLE);
                            }else{
                                timeLineRecyclerView.setVisibility(View.VISIBLE);
                                noList_timeline.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timelineWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goWrite = new Intent(getApplicationContext(),WriteInputActivity.class);
                startActivity(goWrite);
            }
        });
    }

    public void Onscroll() {
        soonList_scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    Log.e("SCROLL", "SCROLLLLLL" );

                    new Thread(() -> {
                        try {
                            page++;
                            timelineList.addAll(server.getSoon(page));
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Server server = new Server();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> timelineList = server.getSoon(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(timelineList.size() == 0){
                                timeLineRecyclerView.setVisibility(View.GONE);
                                noList_timeline.setVisibility(View.VISIBLE);
                            }else{
                                timeLineRecyclerView.setVisibility(View.VISIBLE);
                                noList_timeline.setVisibility(View.GONE);
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