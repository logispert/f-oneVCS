package skcc.com.fashiononeapp.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 05526 on 2017-08-14.
 */

public class DatePickerUtil implements View.OnTouchListener {
    Context context;

    public DatePickerUtil(Context context) {
        this.context = context;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == event.ACTION_DOWN) {
            showDialog(context, (EditText) v);
        }
        return true;
    }

    public void showDialog(final Context context, final EditText et){
        String[] dt = et.getText().toString().split("-");

        int cyear = Integer.parseInt(dt[0]);
        int cmonth = Integer.parseInt(dt[1])-1;
        int cday = Integer.parseInt(dt[2]);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et.setText(setDateString(year, monthOfYear, dayOfMonth));
                    }
                };
        DatePickerDialog alert = new DatePickerDialog(context,  mDateSetListener,
                cyear, cmonth, cday);
        alert.show();
    }

    public static String getToday() {
        return getDate("yyyy-MM-dd", 0);
    }

    public static String getToday(String format) {
        return getDate(format, 0);
    }
    public static String getDate(int diffDay) {
        return getDate("yyyy-MM-dd", diffDay);
    }

    public static String getDate(String format, int diffDay) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c1 = Calendar.getInstance();
        if (diffDay != 0) {
            c1.add(c1.DATE, diffDay);
        }
        return sdf.format(c1.getTime());
    }

    public static String setDateString(int year, int monthOfYear, int dayOfMonth) {
        String mon = (monthOfYear+1)>10?(monthOfYear+1)+"":"0"+(monthOfYear+1);
        String day = (dayOfMonth)>10?(dayOfMonth)+"":"0"+dayOfMonth;

        return year+"-"+mon+"-"+day;
    }
}
