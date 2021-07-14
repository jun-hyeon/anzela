package com.example.angela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;

public class ListTimelineActivity extends AppCompatActivity {

  //  @BindView(R.id.leftArrow)
    ImageView leftArrow;
   // @BindView(R.id.timelineWrite)
    TextView timelineWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_timeline);
//
       leftArrow = (ImageView) findViewById(R.id.leftArrow);
       timelineWrite = (TextView) findViewById(R.id.timelineWrite);


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