package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    int count = 1;
    Server allPosts;
    ArrayList<Post> allList;
    PostAdapter allAdapter;
    LinearLayoutManager layoutManager;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_all);

       allPosts = new Server();

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        allWrite = (TextView) findViewById(R.id.allWrite);

        allRecyclerView = (RecyclerView) findViewById(R.id.allRecycler);
        noList = (LinearLayout) findViewById(R.id.noList_all);

        allList_scrollView = (NestedScrollView) findViewById(R.id.allList_scrollView);

        allRecyclerView.setNestedScrollingEnabled(true);



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



        Thread t1 =  new Thread(() -> {
              try {
                  allList =  allPosts.getPosts(1);
                     runOnUiThread(() -> {
                         allAdapter = new PostAdapter(allList);
                         allRecyclerView.setItemAnimator(new DefaultItemAnimator());
                         layoutManager = new LinearLayoutManager(this);
                         allRecyclerView.setLayoutManager(layoutManager);
                         allRecyclerView.setAdapter(allAdapter);

                      if(allList.size() == 0){
                          allRecyclerView.setVisibility(View.GONE);
                          noList.setVisibility(View.VISIBLE);
                      }else{
                          allRecyclerView.setVisibility(View.VISIBLE);
                          noList.setVisibility(View.GONE);
                      }


                  });
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          });
        t1.start();
        Onscroll();
    }


//    public void loadNextDataFromApi(int page) {
//
//        // Send an API request to retrieve appropriate paginated data
//        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
//        //  --> Deserialize and construct new model objects from the API response
//        //  --> Append the new data objects to the existing set of items inside the array of items
//        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
//        Log.e("SCROLL","SCROLLL"+page);
//
//        new Thread(new Runnable() {
//                      @Override
//                      public void run() {
//                          try {
//                              allList = allPosts.getPosts(page);
//                              allList.addAll(allList);
//                              allAdapter.setArrayList(allList);
//
//                              runOnUiThread(new Runnable() {
//                                  @Override
//                                  public void run() {
//
//                                      allAdapter.notifyDataSetChanged();
//
//
//                                  }
//                              });
//                          } catch (JSONException e) {
//                              e.printStackTrace();
//                          }
//                      }
//                  }).start();
//    }


//    public void Onscroll() {
//        allRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                int itemTotalcount = recyclerView.getAdapter().getItemCount();
//
//                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalcount - 1) {
//                    Log.e("SCROLL", "SCROLLLL" + page);
//
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                page++;
//                                allList.addAll(allList);
//                                allList = allPosts.getPosts(page);
//                                allAdapter.setArrayList(allList);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (allAdapter.getItemCount() > 0)
//                                            allAdapter.notifyDataSetChanged();
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//        });
//    }

    public void Onscroll() {
            allList_scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                        Log.e("SCROLL", "SCROLLLLLL" + page);

                        new Thread(() -> {
                            try {
                                page++;
                                allList.addAll(allPosts.getPosts(page));
//                                allAdapter.setArrayList(allList);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            if(allAdapter.arrayList.size()>0)
                                                allAdapter.notifyDataSetChanged();
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

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    allList =  allPosts.getPosts(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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