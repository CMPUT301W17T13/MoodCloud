package com.csahmad.moodcloud;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import java.util.Calendar;

public class SelectDateActivity extends AppCompatActivity {

    private final static int DIALOG_DATE_PICKER = 0;

    private int setYear;
    private int setMonth;
    private int setDay;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_date);

        Calendar today = Calendar.getInstance();
        setYear = today.get(Calendar.YEAR);
        setMonth = today.get(Calendar.MONTH);
        setDay = today.get(Calendar.DAY_OF_MONTH);

        showDialog(DIALOG_DATE_PICKER);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DIALOG_DATE_PICKER:

                final Activity context = this;

                DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener, setYear, setMonth, setDay);

                dialog.setOnDismissListener(new DatePickerDialog.OnDismissListener(){

                @Override
                public void onDismiss(final DialogInterface arg0){
                    context.finish();
                }
            });

                return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            setYear = year;
            setMonth = month;
            setDay = day;
            returnDate();
        }
    };




    /*
     * Package up the data and return it back to the calling intent
     */
    private void returnDate(){
        Intent intent = getIntent();    // calling/parent intent //

        Bundle bundle = new Bundle();
        bundle.putInt("year", setYear);
        bundle.putInt("month", setMonth);
        bundle.putInt("day", setDay);
        intent.putExtra("set_date", bundle);

        setResult(RESULT_OK, intent);
        finish();


    }
}
