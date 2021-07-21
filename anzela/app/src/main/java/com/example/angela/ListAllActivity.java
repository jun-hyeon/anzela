package com.example.angela;

import androidx.annotation.NonNull;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import java.util.ArrayList;

public class ListAllActivity extends AppCompatActivity {

    ImageView leftArrow;
    TextView allWrite;
    RecyclerView allRecyclerView;
    LinearLayout noList;
    NestedScrollView allList_scrollView;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    LinearLayoutManager  layoutManager;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_all);

        Server allPosts = new Server();

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        allWrite = (TextView) findViewById(R.id.allWrite);

        allRecyclerView = (RecyclerView) findViewById(R.id.allRecycler);

        noList = (LinearLayout) findViewById(R.id.noList_all);

        allList_scrollView = (NestedScrollView) findViewById(R.id.allList_scrollView);

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
                    ArrayList<Post> allList =  allPosts.getPosts(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PostAdapter allAdapter = new PostAdapter(allList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            allRecyclerView.setLayoutManager(layoutManager);
                            allRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            allRecyclerView.setAdapter(allAdapter);

                            if(allList.size() == 0){
                                allRecyclerView.setVisibility(View.GONE);
                                noList.setVisibility(View.VISIBLE);
                            }else{
                                allRecyclerView.setVisibility(View.VISIBLE);
                                noList.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        allRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if(loading){
                        if((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                            loading = false;
                            Log.e("RECYCLERVIEW","LAST ITEM");
                            page++;
                            loading = true;
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Server allPosts = new Server();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Post> allList =  allPosts.getPosts(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PostAdapter allAdapter = new PostAdapter(allList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            allRecyclerView.setLayoutManager(layoutManager);
                            allRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            allRecyclerView.setAdapter(allAdapter);

                            if(allList.size() == 0){
                                allRecyclerView.setVisibility(View.GONE);
                                noList.setVisibility(View.VISIBLE);
                            }else{
                                allRecyclerView.setVisibility(View.VISIBLE);
                                noList.setVisibility(View.GONE);
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