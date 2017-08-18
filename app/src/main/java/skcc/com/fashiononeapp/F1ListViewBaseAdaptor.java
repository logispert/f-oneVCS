package skcc.com.fashiononeapp;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 05526 on 2017-07-27.
 */

public class F1ListViewBaseAdaptor extends BaseAdapter {
    int layoutId;
    int[] textViewId;
    String[] rsKeys;
    JSONArray rs;

    public F1ListViewBaseAdaptor(int layoutId, int[] textViewId, String[] rsKeys) {
        this(layoutId, textViewId, rsKeys, new JSONArray());
    }

    public F1ListViewBaseAdaptor(int layoutId, int[] textViewId, String[] rsKeys, JSONArray rs) {
        this.layoutId   = layoutId;
        this.textViewId = textViewId;
        this.rsKeys     = rsKeys;
        this.rs         = rs;
    }

    public JSONArray getRecordSet() {
        return this.rs;
    }
    public void setRecordSet(JSONArray rs) {
        this.rs = rs;
        notifyDataSetChanged();
    }

    public void clearAllItem() {
        this.rs = new JSONArray();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position>getCount()) {
            return;
        }

        this.rs.remove(position);
        notifyDataSetChanged();
    }


    public void addItem(JSONObject obj) {
        this.rs.put(obj);
    }

    @Override
    public int getCount() {
        return this.rs==null ? 0 : this.rs.length();
    }

    @Override
    public JSONObject getItem(int position) {

        JSONObject record = null;
        try {
            record = this.rs.getJSONObject(position);
        } catch(JSONException e) {
            Log.e("ERROR", "JsonDataSet", e);
        }

        return record;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosion(JSONObject compareObj, String key) {
        return getPosion(compareObj, new String[] {key});
    }

    public int getPosion(JSONObject compareObj, String[] keys) {
        if (keys == null || keys.length ==0) return -1;

        try {
            for (int i = 0; i < getCount(); i++) {
                JSONObject obj = this.rs.getJSONObject(i);
                int inc=0;
                for (int j=0; j<keys.length; j++) {
                    if (obj.getString(keys[j]).equals(compareObj.getString(keys[j]))) {
                        inc++;
                    }
                }
                if (inc == keys.length) {
                    return i;
                }
            }
        } catch(JSONException e) {
            Log.e("ERROR", "JsonDataSet", e);
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        F1ListViewLayout layout = new F1ListViewLayout(parent.getContext().getApplicationContext());

        JSONObject record = getItem(position);
        layout.setData(record, this.textViewId, this.rsKeys);

        return layout;
    }

    class F1ListViewLayout extends LinearLayout {
        public F1ListViewLayout(Context context) {
            super(context);

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layoutId, this);
        }

        public void setData(JSONObject record, int[] textViewId, String[] rsKeys) {

            for (int i=0; i<textViewId.length; i++) {
                TextView tx = (TextView)findViewById(textViewId[i]);
                try {
                    Object object = record.get(rsKeys[i]);
                    Float result = toFloat(object);
                    if (result == null) {
                        tx.setText(object.toString());
                    } else {
                        tx.setText(String.format("%,d", result.intValue()));
                    }
                } catch(JSONException e) {
                    Log.e("ERROR", "getView", e);
                }
            }
        }

        Float toFloat(Object value) {
            if (value instanceof Float) {
                return (Float) value;
            } else if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else if (value instanceof String) {
                try {
                    return Float.valueOf((String) value);
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        }
    }
}
