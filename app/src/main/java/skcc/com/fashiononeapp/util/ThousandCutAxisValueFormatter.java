package skcc.com.fashiononeapp.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by 05526 on 2017-08-10.
 */

public class ThousandCutAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String val = "";
        if (value>=10000000f) {
            val = ""+(value/10000000f)+" 천만";
        } else if (value>=1000000f) {
            val = ""+(int)(value/1000000f)+" 백만";
        } else if (value>=100000f) {
            val = ""+(value/100000f)+" 십만";
        } else {
            val = "";
        }

        return val;
    }
}
