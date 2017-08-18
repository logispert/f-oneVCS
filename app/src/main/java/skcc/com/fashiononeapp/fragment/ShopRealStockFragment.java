package skcc.com.fashiononeapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import skcc.com.fashiononeapp.F1ListViewBaseAdaptor;
import skcc.com.fashiononeapp.R;
import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.DatePickerUtil;
import skcc.com.fashiononeapp.util.JsonDataSet;
import skcc.com.fashiononeapp.util.JsonObjectRequest;

import static skcc.com.fashiononeapp.R.id.silsa_date_act;


public class ShopRealStockFragment extends Fragment {

    int count = 1;
    TextView t_box_no;
    private boolean bBcdStay = false;
    F1ListViewBaseAdaptor adaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop_real_stock, container,false);

        t_box_no = (EditText)rootView.findViewById(R.id.silsa_box_no);
        Button btPlus = (Button)rootView.findViewById(R.id.btn_silsa_plus);
        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                t_box_no.setText(""+count);
            }
        });

        Button btMinus = (Button)rootView.findViewById(R.id.btn_silsa_minus);
        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                t_box_no.setText(""+count);
            }
        });

        EditText silsaDateAct = (EditText) rootView.findViewById(silsa_date_act);

        silsaDateAct.setText(DatePickerUtil.getToday());
        silsaDateAct.setInputType(0);
        silsaDateAct.setOnTouchListener(new DatePickerUtil(getContext()));

        int[] textViewId = {R.id.silsa_col1, R.id.silsa_col2, R.id.silsa_col3, R.id.silsa_col4, R.id.silsa_col5};
        String[] rsKeys =  {"STYLE_CD", "CLR_CD", "SIZE_CD", "BOX_NO", "QTY"};

        adaptor = new F1ListViewBaseAdaptor(
                R.layout.silsa_list,
                textViewId,
                rsKeys);

        ListView listView = (ListView)rootView.findViewById(R.id.silsa_insert_list);
        listView.setAdapter(adaptor);

        EditText tmcd = (EditText)rootView.findViewById(R.id.silsa_tmcd);           //상품코드 입력될 때 체크 후 목록에 표시
        tmcd.setInputType(0);
        tmcd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (bBcdStay) {
                        EditText tet = (EditText) v;
                        tet.setText("");
                        bBcdStay = false;
                    }
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == 66) {

                        final String sBoxNo = ((EditText) rootView.findViewById(R.id.silsa_box_no)).getText().toString();
                        String sStyleCd = ((EditText) rootView.findViewById(R.id.silsa_tmcd)).getText().toString();
                        try {
                            JSONArray refArr = adaptor.getRecordSet();
                            for(int i = 0; i < refArr.length(); i++) {    //상품코드, 박스번호 중복입력 시 수량 증가
                                JSONObject obj = refArr.getJSONObject(i);
                                String sTmCd = obj.getString("STYLE_CD") + obj.getString("CLR_CD") + obj.getString("SIZE_CD");
                                if (sTmCd.equals(sStyleCd) && obj.getString("BOX_NO").equals(sBoxNo)) {

                                    int addQty = obj.getInt("QTY");
                                    addQty++;
                                    refArr.getJSONObject(i).put("QTY", addQty);
                                    adaptor.notifyDataSetChanged();
                                    bBcdStay = true;

                                    return false;
                                }
                            }

                        } catch(JSONException e) {
                            e.printStackTrace();
                            Log.e("ERR", e.getMessage());
                        }

                        JsonDataSet param = new JsonDataSet();
                        param.setTranId("sh.sha.SHASShopCommon#getSCSCdByTmcd");

                        param.putField("tmCd",((EditText) rootView.findViewById(R.id.silsa_tmcd)).getText().toString());

                        Log.d("REQ",param.toString());
                        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response != null) {
                                            Log.d("TAG",response.toString());
                                            try {
                                                JsonDataSet param = new JsonDataSet(response);

                                                JSONArray resArr  = param.getRecordSet("dsOutput");
                                                if (resArr==null || resArr.length() == 0) {
                                                    Toast.makeText(getContext(), "유효하지 않은 바코드 입니다.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    JSONObject obj = resArr.getJSONObject(0);
                                                    obj.put("BOX_NO", sBoxNo);
                                                    obj.put("QTY", "1");
                                                    adaptor.addItem(obj);

                                                    adaptor.notifyDataSetChanged();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), "excep->"+e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "onErrRes->"+error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        );

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(jsonReq);

                        bBcdStay = true;
                    }
                }
                return false;
            }
        });

        Button btnSave = (Button)rootView.findViewById(R.id.btn_silsa_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonDataSet param = new JsonDataSet();
                param.setTranId("sh.sha.SHABSalesMgmt#uploadShopStockInvstgtnApp");

                param.putField("BRD_CD", "T");
                param.putField("SHOP_CD",((EditText) rootView.findViewById(R.id.silsa_shcd_act)).getText().toString());
                param.putField("PRCS_DATE",((EditText) rootView.findViewById(silsa_date_act)).getText().toString());
                param.putField("GB_CD", "J");
                param.putRecordSet("dsMg", adaptor.getRecordSet());

                Log.d("REQ",param.toString());
                /*
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    Log.d("TAG",response.toString());
                                    try {
                                        JsonDataSet param = new JsonDataSet(response);

                                        adaptor.notifyDataSetChanged();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "excep->"+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "onErrRes->"+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                );

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(jsonReq);*/
            }
        });

        Button btDel = (Button)rootView.findViewById(R.id.btn_silsa_del);
        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptor.clearAllItem();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setMessage("삭제 하시겠습니까?");
                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adaptor.removeItem(position);
                    }
                });
                ab.setNegativeButton("Cancel", null);
                ab.show();

                return false;
            }
        });

        TextView tx3 = (TextView)rootView.findViewById(R.id.textView3);
        tx3.setOnClickListener(new tempTmcd());
        TextView tx4 = (TextView)rootView.findViewById(R.id.textView4);
        tx4.setOnClickListener(new tempTmcd());
        TextView tx5 = (TextView)rootView.findViewById(R.id.textView5);
        tx5.setOnClickListener(new tempTmcd());
        TextView tx6 = (TextView)rootView.findViewById(R.id.textView6);
        tx6.setOnClickListener(new tempTmcd());

        return rootView;
    }

    class tempTmcd implements View.OnClickListener {
        public void onClick(View v) {
            String str = "";
            if (v.getId() == R.id.textView3) {
                str = "ABN4VB400WTF";
            } else if (v.getId() == R.id.textView4) {
                str = "AHN2AM280LYF";
            } else if (v.getId() == R.id.textView5) {
                str = "5MN4KC0209090";
            } else {
                str = "2MN4WP3003344";
            }
            EditText tmcd = (EditText)getActivity().findViewById(R.id.silsa_tmcd);
            tmcd.setText(str);
            bBcdStay = false;
            tmcd.requestFocus();
        }
    }

}
