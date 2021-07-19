package com.example.angela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;

public class ListTimelineActivity extends AppCompatActivity {

  //  @BindView(R.id.leftArrow)
    ImageView leftArrow;
   // @BindView(R.id.timelineWrite)
    TextView timelineWrite;
    RecyclerView timeLineRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_timeline);
//
       leftArrow = (ImageView) findViewById(R.id.leftArrow);
       timelineWrite = (TextView) findViewById(R.id.timelineWrite);

       timeLineRecyclerView = (RecyclerView) findViewById(R.id.timelineRecycler);

       Server server = new Server();



        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> timelineList = server.getSoon();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PostAdapter postAdapter = new PostAdapter(timelineList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            timeLineRecyclerView.setLayoutManager(layoutManager);
                            timeLineRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            timeLineRecyclerView.setAdapter(postAdapter);
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
}