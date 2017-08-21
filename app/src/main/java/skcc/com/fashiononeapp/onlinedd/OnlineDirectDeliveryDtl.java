package skcc.com.fashiononeapp.onlinedd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import skcc.com.fashiononeapp.R;
import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.JsonDataSet;


/**
 * Created by 07237 on 2017-07-31.
 */

public class OnlineDirectDeliveryDtl extends AppCompatActivity {

    private ArrayList<ListDelivery> listDelivery;
    private JSONObject obj = new JSONObject();
    private JSONObject obj2 = new JSONObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_delivery_dtl);

        listDelivery = (ArrayList<ListDelivery>)getIntent().getSerializableExtra("deliveryList");


        Intent ondtl = getIntent();

        //json오브젝트를 스트링타입으로 받음.
        String clickList = ondtl.getStringExtra("clickList");


        try {

            obj = new JSONObject(clickList);

            obj2.put("ORD_NO", "");
            obj2.put("TM_CD","");
            obj2.put("REASON_NM","");

            TextView tv2ReqDate = (TextView) findViewById(R.id.tv2ReqDate);
            TextView tv2ReqNum = (TextView) findViewById(R.id.tv2ReqNum);
            TextView tv2OnlineNum = (TextView) findViewById(R.id.tv2OnlineNum);
            TextView tv2StyleCd = (TextView) findViewById(R.id.tv2StyleCd);
            TextView tv2ClrCd = (TextView) findViewById(R.id.tv2ClrCd);
            TextView tv2SizeCd = (TextView) findViewById(R.id.tv2SizeCd);
            TextView tv2ReqQty = (TextView) findViewById(R.id.tv2ReqQty);
            TextView tv2ShopQty = (TextView) findViewById(R.id.tv2ShopQty);
            TextView tv2ExpFee = (TextView) findViewById(R.id.tv2ExpFee);
            TextView tv2Status = (TextView) findViewById(R.id.tv2Status);

            tv2ReqNum.setText(obj.getString("SEQ"));
            tv2ReqDate.setText(obj.getString("REQ_DATE"));
            tv2OnlineNum.setText(obj.getString("ORD_NO"));
            tv2StyleCd.setText(obj.getString("STYLE_CD"));
            tv2ClrCd.setText(obj.getString("CLR_CD"));
            tv2SizeCd.setText(obj.getString("SIZE_CD"));
            tv2ReqQty.setText(obj.getString("QTY"));
            tv2ShopQty.setText(obj.getString("SHOP_QTY"));
            tv2ExpFee.setText(obj.getString("EX_AMT"));
            tv2Status.setText(obj.getString("STAT_NM"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //저장 버튼 클릭 이벤트 설정
        Button btnAccept = (Button)findViewById(R.id.btn_accept);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONArray reqLists = new JSONArray(listDelivery);
                JSONArray reqLists = new JSONArray();
                JSONArray reqLists2 = new JSONArray();
                reqLists.put(obj);
                reqLists2.put(obj2);


                JsonDataSet param = new JsonDataSet();
                param.setTranId("sl.slg.SLGBCust#saveSfmDeliOrderList");

                param.putRecordSet("dsOutput", reqLists);
                param.putRecordSet("dsOutputMsg", reqLists2);

                Log.d("REQ",param.toString());
                Log.d("dddd","asdg");

                /*
                Intent intent = new Intent();
                intent.putExtra("key", 1);
                setResult(1, intent);
                */
                //finish();

                //이동테스트
                //parameter 설정 후 서버 전송
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                        //서버 응답시 처리
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    //Log.d("TAG",response.toString());
                                    try {
                                        //response JSON을 DataSet형태로 wrapping
                                        JsonDataSet param = new JsonDataSet(response);
                                        //로그인 성공 시
                                        if ("OK".equals(param.getResult())) {
                                            //JSONArray reqList  = param.getRecordSet("dsOutput");
                                            Toast.makeText(getApplicationContext(), "접수처리되었습니다.", Toast.LENGTH_SHORT).show();
                                            //Intent ondtl = new Intent( getApplicationContext() , MainActivity.class);
                                            Intent intent = new Intent();
                                            intent.putExtra("key", 1);
                                            setResult(1, intent);
                                            //setResult(1);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), param.getMessageName(), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "excep->"+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "onErrRes->"+error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                );

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(jsonReq);

                //이동 테스트*/

            }


        });


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader mImageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        try {
            String styleCd = obj.getString("STYLE_CD");
            String clrCd = obj.getString("CLR_CD");
            NetworkImageView ni = (NetworkImageView) findViewById(R.id.goods_img);
            StringBuffer sb = new StringBuffer();
            sb.append("http://img.hfashionmall.com/images/upload/repo/targetDir/p/")
                    .append(styleCd.substring(0,2)).append("/188x262/")
                    .append(styleCd).append("_").append(clrCd).append("_05.png");
            ni.setImageUrl(sb.toString(), mImageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onClickBack(View view) {
        this.onBackPressed();
    }

}
