package skcc.com.fashiononeapp.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 05526 on 2017-06-20.
 */

public class JsonDataSet extends JSONObject {

    private static String TRANSACTION = "transaction";
    private static String DATASET = "dataSet";
    private static String FIELDS = "fields";
    private static String RECORDSETS = "recordSets";
    private static String NC_LIST = "nc_list";
    private static String MESSAGE = "message";

    private JSONObject joDataSet;

    public JsonDataSet()  {
        super();
        joDataSet = new JSONObject();
        try {
            this.put(DATASET, joDataSet);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
    }
    public JsonDataSet(JSONObject response) {
        super();
        joDataSet = new JSONObject();
        try {
            if (!this.isNull(TRANSACTION)) {
                JSONObject joTransaction = response.getJSONObject(TRANSACTION);
                this.put(TRANSACTION, joTransaction);
            }
            joDataSet = response.getJSONObject(DATASET);
        } catch(JSONException e) {
            Log.e("ERROR", "JsonDataSet", e);
        }
    }

    public void setTranId(String tranId) {
        try {
            if (this.isNull(TRANSACTION)) {
                JSONObject joTransaction = new JSONObject();
                this.put(TRANSACTION, joTransaction);
            }
            JSONObject joTransaction = this.getJSONObject(TRANSACTION);
            joTransaction.put("id", tranId);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
    }

    public String getTranId()  {
        String ret = null;
        try {
            if (this.isNull(TRANSACTION)) {
                JSONObject joTransaction = new JSONObject();
                this.put(TRANSACTION, joTransaction);
            }
            JSONObject joTransaction = this.getJSONObject(TRANSACTION);
            ret = joTransaction.getString("id");
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return ret;
    }


    public void putField(String key, String value)  {
        try {
            if (joDataSet.isNull(FIELDS)) {
                JSONObject joField = new JSONObject();
                joDataSet.put(FIELDS, joField);
            }
            JSONObject joField = joDataSet.getJSONObject(FIELDS);
            joField.put(key, value);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
    }

    public String getField(String key) {
        String ret = null;
        try {
            if (joDataSet.isNull(FIELDS)) {
                return null;
            }
            JSONObject joField = joDataSet.getJSONObject(FIELDS);
            ret = joField.getString(key);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return ret;
    }

    public void putRecordSet(String rsNM, JSONArray ncList) {
        try {
            if (joDataSet.isNull(RECORDSETS)) {
                JSONObject joRSs = new JSONObject();
                joDataSet.put(RECORDSETS, joRSs);
            }
            JSONObject joRSs = joDataSet.getJSONObject(RECORDSETS);
            JSONObject rs = new JSONObject();
            rs.put(NC_LIST, ncList);
            joRSs.put(rsNM, rs);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
    }

    public JSONArray getRecordSet(String rsNM) {
        try {
            if (joDataSet.isNull(RECORDSETS)) {
                return null;
            }
            JSONObject joRSs = joDataSet.getJSONObject(RECORDSETS);
            JSONObject rs = joRSs.getJSONObject(rsNM);
            return rs == null?null:rs.getJSONArray(NC_LIST);
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return null;
    }

    public JSONObject getRecordSetObject(String rsNM) {
        try {
            if (joDataSet.isNull(RECORDSETS)) {
                return null;
            }
            JSONObject joRSs = joDataSet.getJSONObject(RECORDSETS);
            JSONObject rs = joRSs.getJSONObject(rsNM);
            return rs;
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return null;
    }

    public String getResult() {
        String ret = null;
        try {
            if (joDataSet.isNull(MESSAGE)) {
                return null;
            }
            JSONObject joMessage = joDataSet.getJSONObject(MESSAGE);
            ret = joMessage.getString("result");
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return ret;
    }

    public String getMessageName() {
        String ret = null;
        try {
            if (joDataSet.isNull(MESSAGE)) {
                return null;
            }
            JSONObject joMessage = joDataSet.getJSONObject(MESSAGE);
            ret = joMessage.getString("messageName");
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return ret;
    }

    public JSONObject getMessage() {
        try {
            if (joDataSet.isNull(MESSAGE)) {
                return null;
            }
            JSONObject joMessage = joDataSet.getJSONObject(MESSAGE);
            return joMessage;
        } catch(JSONException e) {
            Log.e("ERROR","JsonDataSet",e);
        }
        return null;
    }
}
