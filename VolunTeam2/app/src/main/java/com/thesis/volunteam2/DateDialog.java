package com.thesis.volunteam2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Jeysown on 6/29/2016.
 */


@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText editDate;

    public DateDialog(View view){
        editDate=(EditText)view;
    }
    public Dialog onCreateDialog(Bundle saveInstanceState) {
// Use the current date as the default date in the dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get( Calendar.YEAR );
        int month = c.get( Calendar.MONTH );
        int day = c.get( Calendar.DAY_OF_MONTH);
        c.add(Calendar.DATE, -1);
        DatePickerDialog date = new DatePickerDialog( getActivity(), this, year, month, day );
        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return date;

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = sdf.format(c.getTime());
        editDate.setText(formattedDate);
       }
    public static class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        EditText editTime;
        private final static int TIME_PICKER_INTERVAL = 15;

        public TimeDialog(View view){
            editTime = (EditText) view;
        }
        public Dialog onCreateDialog(Bundle savedInstanceState) {

    // Use the current date as the default date in the dialog
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            // Create a new instance of DatePickerDialog and return
          TimePickerDialog tpd = new TimePickerDialog(getActivity(),this,hour,min,false);

            return tpd;
        }
        @Override
        public void onTimeSet(TimePicker view, int hour, int min) {
            String am_pm ="";
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);

            if (c.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (c.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (c.get(Calendar.HOUR) == 0)?"12":c.get(Calendar.HOUR)+"";
            String mintoShow = min < 10 ? "0"+min : ""+min;
           String date =( strHrsToShow+":"+mintoShow+" "+am_pm );
            editTime.setText(date);
        }
    }
}