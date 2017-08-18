package skcc.com.fashiononeapp.util;

/**
 * Created by 05526 on 2017-06-20.
 */

public class StringUtil {

    public static boolean isNullString(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

}
