package skcc.com.fashiononeapp.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by 05526 on 2017-08-10.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, ((int)value)-6);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

        return sdf.format(cal.getTime());
    }
}
