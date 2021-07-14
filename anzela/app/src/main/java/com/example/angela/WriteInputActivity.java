package com.example.angela;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;



public class WriteInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Dialog dialog_write;
    Dialog dialogCount;
    Dialog dialogNecessarry;
    TextView editDate,count,cbText,completeBtn;
    CheckBox cb;
    ImageView close;
    EditText titleWrite,arrivalWrite,startWrite,infoWrite;
    View titleView, arrivalView, startView, infoView;

    boolean isComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_input);



        editDate = (TextView) findViewById(R.id.editDate);
        close = (ImageView) findViewById(R.id.close);
        count = (TextView) findViewById(R.id.count);
        cb = (CheckBox) findViewById(R.id.cb);
        cbText = (TextView) findViewById(R.id.checkboxText);
        titleWrite = (EditText) findViewById(R.id.titleWrite);
        arrivalWrite = (EditText ) findViewById(R.id.arrivalWrite);
        startWrite = (EditText) findViewById(R.id.startWrite);
        infoWrite = (EditText) findViewById(R.id.infoWrite);
        completeBtn = (TextView) findViewById(R.id.completeBtn);


        titleView = (View) findViewById(R.id.titleView);
        arrivalView = (View) findViewById(R.id.arrivalView);
        startView = (View) findViewById(R.id.startView);
        infoView = (View) findViewById(R.id.infoView);


        dialog_write = new Dialog(WriteInputActivity.this);
        dialogCount = new Dialog(WriteInputActivity.this);
        dialogNecessarry = new Dialog(WriteInputActivity.this);


        dialog_write.setContentView(R.layout.dialog_write);
        dialogCount.setContentView(R.layout.dialog_count);
        dialogNecessarry.setContentView(R.layout.dialog_necessarry);


        SpannableString personcount = new SpannableString("총2명");
        personcount.setSpan(new UnderlineSpan(),0,personcount.length(),0);
        count.setText(personcount);

        SpannableString edDate = new SpannableString("2021년 06월 01일");
        edDate.setSpan(new UnderlineSpan(),0,edDate.length(),0);
        editDate.setText(edDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date Picker");
                clearFocus();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showWriteDialog();
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    cbText.setTextColor(getResources().getColor(R.color.white));
                    arrivalView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                    clearFocus();
                }else{
                    cbText.setTextColor(getResources().getColor(R.color.dark_grey));
                    arrivalView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                }
            }
        });

        titleWrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                titleView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                if(!hasFocus){
                    titleView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                }
            }
        });


        startWrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                startView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                if(!hasFocus){
                    startView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                }
            }
        });

        arrivalWrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                arrivalView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                if(!hasFocus){
                    arrivalView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                }
            }
        });


        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountDialog();
                clearFocus();
            }
        });

        infoWrite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                infoView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                if(!hasFocus){
                    infoView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                }
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isComplete){
                   Toast.makeText(WriteInputActivity.this,"완료되었습니다.",Toast.LENGTH_LONG).show();
               }else{
                   showNecessarryDialog();
               }
            }
        });

        titleWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAbled();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        startWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAbled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        arrivalWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAbled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        infoWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             isAbled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void isAbled(){
        if((titleWrite.getText().toString().equals("") || titleWrite.getText().toString() == null) == false
                &(startWrite.getText().toString().equals("") || startWrite.getText().toString() == null) == false
                &((arrivalWrite.getText().toString().equals("") || arrivalWrite.getText().toString() == null) == false || cb.isChecked())
                &(infoWrite.getText().toString().equals("") || infoWrite.getText().toString() == null ) == false){

            completeBtn.setTextColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
            completeBtn.setBackground(ContextCompat.getDrawable(WriteInputActivity.this,R.drawable.rectangle_textwtire));
            isComplete = true;
        }else{
            completeBtn.setTextColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_green));
            completeBtn.setBackground(ContextCompat.getDrawable(WriteInputActivity.this,R.drawable.write_disable));
            isComplete = false;

        }
    }

    public void clearFocus(){
        titleWrite.clearFocus();
        startWrite.clearFocus();
        arrivalWrite.clearFocus();
        infoWrite.clearFocus();
    }


    public void showWriteDialog(){
            dialog_write.show();

            TextView dialogCancel = dialog_write.findViewById(R.id.dialogCancel);
            TextView dialogStop = dialog_write.findViewById(R.id.dialogStop);

            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_write.dismiss();
                }
            });

            dialogStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
    }

    void showCountDialog(){
            dialogCount.show();

            EditText countEdit = dialogCount.findViewById(R.id.countEdit);
            View  personCounterView = dialogCount.findViewById(R.id.personCountView);
            TextView countDialogCancel = dialogCount.findViewById(R.id.countDialogCancel);
            TextView countDialogApply = dialogCount.findViewById(R.id.countDialogApply);

            String ct = countEdit.getText().toString();

            countEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        personCounterView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.dark_grey));
                    }else{
                        personCounterView.setBackgroundColor(ContextCompat.getColor(WriteInputActivity.this,R.color.aqua_marine));
                    }
                }
            });

        countDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCount.dismiss();
            }
        });

        countDialogApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString countText = new SpannableString(countEdit.getText().toString()+"명");
                countText.setSpan(new UnderlineSpan(),0,countText.length(),0);
                count.setText(countText);
                //count.setText(countEdit.getText().toString()+"");
                dialogCount.dismiss();
            }
        });

    }

    void showNecessarryDialog(){
        dialogNecessarry.show();
        TextView okayBtn = dialogNecessarry.findViewById(R.id.okayBtn);

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNecessarry.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c  = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        SpannableString dateUnderline = new SpannableString(currentDateString);
        dateUnderline.setSpan(new UnderlineSpan(),0,dateUnderline.length(),0);
        editDate.setText(dateUnderline);
    }


}