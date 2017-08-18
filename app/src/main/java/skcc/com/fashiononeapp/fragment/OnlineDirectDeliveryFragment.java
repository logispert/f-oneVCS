package skcc.com.fashiononeapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import skcc.com.fashiononeapp.F1ListViewBaseAdaptor;
import skcc.com.fashiononeapp.R;
import skcc.com.fashiononeapp.onlinedd.OnlineDirectDeliveryDtl;
import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.DatePickerUtil;
import skcc.com.fashiononeapp.util.JsonDataSet;
import skcc.com.fashiononeapp.util.JsonObjectRequest;
import skcc.com.fashiononeapp.util.StringUtil;
import skcc.com.fashiononeapp.util.UserInfo;

public class OnlineDirectDeliveryFragment extends Fragment {


    ListView lvList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_online_direct_delivery, container, false);

        //검색 버튼 클릭 이벤트 설정
        Button btnDelivery = (Button) rootView.findViewById(R.id.btn_delivery);

        TextView txShopNm = (TextView) rootView.findViewById(R.id.tx_shop_nm);
        txShopNm.setText(UserInfo.getUser().getUserNm());

        EditText etFrDate = (EditText) rootView.findViewById(R.id.et_reqFrDate);
        EditText etToDate = (EditText) rootView.findViewById(R.id.et_reqToDate);

        etFrDate.setText(DatePickerUtil.getDate(-7));
        etToDate.setText(DatePickerUtil.getToday());

        etFrDate.setInputType(0);
        etFrDate.setOnTouchListener(new DatePickerUtil(getContext()));
        etToDate.setInputType(0);
        etToDate.setOnTouchListener(new DatePickerUtil(getContext()));

        lvList = (ListView) rootView.findViewById(R.id.list_ondelivery);

        int[] txId = {R.id.style_cd, R.id.req_qty, R.id.shop_qty, R.id.exp_fee};
        String[] keys = {"STYLE_CD", "QTY", "SHOP_QTY", "EX_AMT"};
        final F1ListViewBaseAdaptor onDeliveryAdaptor = new F1ListViewBaseAdaptor(
                R.layout.ondelivery_list,
                txId, keys
        );

        lvList.setAdapter(onDeliveryAdaptor);
        lvList.setOnItemClickListener(new ListViewItemClickListner());
        lvList.setOnItemLongClickListener(new ListViewItemLongClickListner());

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etFrDate = (EditText) getActivity().findViewById(R.id.et_reqFrDate);
                EditText etToDate = (EditText) getActivity().findViewById(R.id.et_reqToDate);

                if (StringUtil.isNullString(etFrDate.getText().toString()) || StringUtil.isNullString(etToDate.getText().toString())) {
                    Toast.makeText(getContext(), "요청일자를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioGroup rg = (RadioGroup) getActivity().findViewById(R.id.radioGrp);
                RadioButton rd = (RadioButton) getActivity().findViewById(rg.getCheckedRadioButtonId());
                int intRd = rd.getId();
                String strRd = "";

                if (intRd == R.id.radioNoAcc) {
                    strRd = "01";
                } else {
                    strRd = "02";
                }
                JsonDataSet param = new JsonDataSet();
                param.setTranId("sl.slg.SLGBCust#selectSfmDeliOrderList");

                param.putField("SHOP_CD", "TJH13");
                param.putField("REQ_DATE_FR", etFrDate.getText().toString().replaceAll("-",""));
                param.putField("REQ_DATE_TO", etToDate.getText().toString().replaceAll("-",""));
                param.putField("STAT_CD", strRd);

                Log.d("REQ", param.toString());
                //parameter 설정 후 서버 전송
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                        //서버 응답시 처리
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    Log.d("TAG", response.toString());
                                    try {
                                        //response JSON을 DataSet형태로 wrapping
                                        JsonDataSet param = new JsonDataSet(response);

                                        //로그인 성공 시
                                        if ("OK".equals(param.getResult())) {
                                            JSONArray resList = param.getRecordSet("dsOutput");

                                            if (resList == null || resList.length() == 0) {
                                                Toast.makeText(getContext(), "조회된 자료가 없습니다.", Toast.LENGTH_SHORT).show();
                                                onDeliveryAdaptor.setRecordSet(new JSONArray());
                                            } else {
                                                onDeliveryAdaptor.setRecordSet(resList);
                                            }

                                        } else {
                                            Toast.makeText(getContext(), param.getMessageName(), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "excep->" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "onErrRes->" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(jsonReq);
            }
        });

        return rootView;
    }


    private class ListViewItemClickListner implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
            try {
                JSONObject clickList = (JSONObject) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), clickList.getString("STYLE_CD"), Toast.LENGTH_SHORT).show();

                Intent ondtl = new Intent(getActivity(), OnlineDirectDeliveryDtl.class);

                ondtl.putExtra("clickList", clickList.toString());

                startActivityForResult(ondtl, 0);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERR", e.getMessage());
            }
        }
    }


    private class ListViewItemLongClickListner implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getContext(), "bbb", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:

                RadioButton rd = (RadioButton) getActivity().findViewById(R.id.radioAcc);
                rd.setChecked(true);

                Button btnDelivery = (Button) getActivity().findViewById(R.id.btn_delivery);
                btnDelivery.performClick();

                break;
            default:
                break;
        }
    }

    private void selectList() {
    }

}