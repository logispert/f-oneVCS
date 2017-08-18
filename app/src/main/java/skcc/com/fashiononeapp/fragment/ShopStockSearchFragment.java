package skcc.com.fashiononeapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONObject;

import skcc.com.fashiononeapp.F1ListViewBaseAdaptor;
import skcc.com.fashiononeapp.R;
import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.DatePickerUtil;
import skcc.com.fashiononeapp.util.JsonDataSet;
import skcc.com.fashiononeapp.util.JsonObjectRequest;


public class ShopStockSearchFragment extends Fragment {

    private ListView listView ;
    F1ListViewBaseAdaptor adaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //나중에 유저인포 공통에서 가져올 정보들 여기부터
        final String brandCode ="T";
        final String subBrandCode="U";
        final String subBrandGroupYn="N";
        final String shopCode="TJH13";
        //나중에 유저인포 공통에서 가져올 정보들 여기까지

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop_stock_search, container, false);
        Button btn  = (Button) rootView.findViewById(R.id.btStockSearch);

        //샵코드 에디트 텍스트를 컨트롤 하기 위함
        EditText etShopCd= (EditText) rootView.findViewById(R.id.etShopCd);
        //지금은 임의의 코드를 세팅해줌.
        etShopCd.setText(shopCode);


        int[] textViewId = {R.id.color, R.id.size, R.id.dataStock, R.id.realStock, R.id.inAmt, R.id.stockAmt};
        String[] rsKeys =  {"CLR_CD", "SIZE_CD", "STOCK_QTY", "RSTOCK_QTY_TXT","IN_AMT","STOCK_AMT"};

        adaptor = new F1ListViewBaseAdaptor(
                R.layout.list_6col,
                textViewId,
                rsKeys);

        ListView listView = (ListView)rootView.findViewById(R.id.lv_sss);
        listView.setAdapter(adaptor);

        EditText etStyleCd= (EditText) rootView.findViewById(R.id.etStyleCd);
        etStyleCd.setInputType(0);

        TextView tx3 = (TextView)rootView.findViewById(R.id.styleCd);
        tx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etStyleCd= (EditText) rootView.findViewById(R.id.etStyleCd);
                etStyleCd.setText( "TUMR1KOE51D0");
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                JsonDataSet param = new JsonDataSet();
                EditText etStyleCd= (EditText) rootView.findViewById(R.id.etStyleCd);
                EditText etYearCd= (EditText) rootView.findViewById(R.id.etYearCd);
                EditText etFromSeasonCd= (EditText) rootView.findViewById(R.id.etFromSeasonCd);
                EditText etToSeasonCd= (EditText) rootView.findViewById(R.id.etToSeasonCd);
                param.setTranId("sh.sha.SHABSalesMgmt#selectShopStockCndtn");

                //입력값을 없을 경우
                if (etYearCd.getText().toString().equals("")
                        || etStyleCd.getText().toString().equals("")
                        || etFromSeasonCd.getText().toString().equals("")
                        || etToSeasonCd.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(),"조회 필수값을 입력하세요.", Toast.LENGTH_LONG).show();
                    return ;
                }


                param.putField("brdCd",brandCode);
                param.putField("sbrdCd",subBrandCode);
                param.putField("sbrdGrpYn" ,subBrandGroupYn);
                param.putField("shopCd",shopCode);
                param.putField("sDate", DatePickerUtil.getToday("yyyyMMdd"));
                //param.putField("fromYear","R");
                //param.putField("fromSeason1","1");
                //param.putField("fromSeason2","1");
                //param.putField("styleCd","TMMR1AFE21A0");
                param.putField("fromYear",etYearCd.getText().toString().toUpperCase());
                param.putField("fromSeason1",etFromSeasonCd.getText().toString());
                param.putField("fromSeason2",etToSeasonCd.getText().toString());
                param.putField("styleCd",etStyleCd.getText().toString().toUpperCase());



                //param.putRecordSet();

                Log.d("REQ",param.toString());
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    Log.d("TAG",response.toString());
                                    try {
                                        JsonDataSet param = new JsonDataSet(response);

                                        if ("OK".equals(param.getResult())) {
                                            JSONArray resList  = param.getRecordSet("dsOutput1");

                                            if (resList == null || resList.length() == 0) {
                                                Toast.makeText(getContext(), "조회된 자료가 없습니다.", Toast.LENGTH_SHORT).show();
                                                adaptor.setRecordSet(new JSONArray());
                                            } else {
                                                adaptor.setRecordSet(resList);
                                            }

                                        } else {
                                            Toast.makeText(getContext(), param.getMessageName(), Toast.LENGTH_SHORT).show();
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

                etStyleCd.requestFocus();
            }
        });

        return rootView;
    }

}
