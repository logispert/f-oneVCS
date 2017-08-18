package skcc.com.fashiononeapp.util;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 05526 on 2017-06-20.
 */

public class JsonObjectRequest extends com.android.volley.toolbox.JsonObjectRequest {

    public JsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);

        setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        if (headers.containsKey("Set-Cookie")
                && headers.get("Set-Cookie").startsWith("JSESSIONID")) {
            String cookie = headers.get("Set-Cookie");
            String[] splitCookie = cookie.split(";");
            UserInfo.getUser().setSessionId(splitCookie[0]);
        }

        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Cookie",UserInfo.getUser().getSessionId());

        return headers;
    }

}
