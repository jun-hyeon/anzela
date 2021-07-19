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

import java.util.ArrayList;

public class ListAllActivity extends AppCompatActivity {

    ImageView leftArrow;
    TextView allWrite;
    RecyclerView allRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_all);

        Server allPosts = new Server();

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        allWrite = (TextView) findViewById(R.id.allWrite);

        allRecyclerView = (RecyclerView) findViewById(R.id.allRecycler);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        allWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goWrite = new Intent(getApplicationContext(),WriteInputActivity.class);
                startActivity(goWrite);
            }
        });

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> allList =  allPosts.getPosts();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PostAdapter allAdapter = new PostAdapter(allList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            allRecyclerView.setLayoutManager(layoutManager);
                            allRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            allRecyclerView.setAdapter(allAdapter);
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