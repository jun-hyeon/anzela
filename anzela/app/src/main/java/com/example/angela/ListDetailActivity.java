package com.example.angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

public class ListDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView leftArrow,message;
    GoogleMap googleMap;
    EditText comment_editText;
    TextView detailTitle;
    TextView rightBtn;
    TextView leftBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        message = (ImageView) findViewById(R.id.message);

        comment_editText = (EditText) findViewById(R.id.comment_editText);

        detailTitle = (TextView) findViewById(R.id.detailTitle);
        rightBtn = (TextView) findViewById(R.id.rightBtn);
        leftBtn = (TextView) findViewById(R.id.leftBtn);

        SpannableString titleBold = new SpannableString(detailTitle.getText());
        titleBold.setSpan(new StyleSpan(Typeface.BOLD),0,titleBold.length(),0);
        detailTitle.setText(titleBold);

        SpannableString rightBold = new SpannableString(rightBtn.getText());
        rightBold.setSpan(new StyleSpan(Typeface.BOLD),0,rightBold.length(),0);
        rightBtn.setText(rightBold);

        SpannableString leftBold = new SpannableString(leftBtn.getText());
        leftBold.setSpan(new StyleSpan(Typeface.BOLD),0,leftBold.length(),0);
        leftBtn.setText(leftBold);



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

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng position = new LatLng(37.56, 126.97);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

    }
}