package skcc.com.fashiononeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.JsonDataSet;
import skcc.com.fashiononeapp.util.JsonObjectRequest;
import skcc.com.fashiononeapp.util.StringUtil;
import skcc.com.fashiononeapp.util.UserInfo;

/**
 * Created by 05526 on 2017-06-20.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //login layout xml 설정
        setContentView(R.layout.activity_login);

        //로그인 버튼 클릭 이벤트 설정
        Button loginBtn = (Button)findViewById(R.id.bt_login);
        loginBtn.setOnClickListener(btLoginClick);
    }

    private View.OnClickListener btLoginClick = new View.OnClickListener() {
        @Override
        //로그인 버튼 클릭 시
        public void onClick(View v) {
            EditText etId = (EditText)findViewById(R.id.et_loginId);
            EditText etPwd = (EditText)findViewById(R.id.et_loginPwd);

            if (StringUtil.isNullString(etId.getText().toString())) {
                Toast.makeText(getApplicationContext(), "ID를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtil.isNullString(etPwd.getText().toString())) {
                Toast.makeText(getApplicationContext(), "패스워드를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            JsonDataSet param = new JsonDataSet();
            param.setTranId("CSMA0002");

            param.putField("POS_YN","N");
            param.putField("USER_ID",etId.getText().toString());
            param.putField("USER_PASSWORD",etPwd.getText().toString());

            Log.d("REQ",param.toString());
            //parameter 설정 후 서버 전송
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_LOGIN_URL, param,
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
                                        JSONArray userInfo  = param.getRecordSet("dsUserInfo");
                                        JSONObject user = userInfo.getJSONObject(0);

                                        Toast.makeText(getApplicationContext(), "반갑습니다. "+user.getString("USER_NAME")+" 님.", Toast.LENGTH_SHORT).show();

                                        //사용자 정보를 전역변수로 설정
                                        UserInfo mfUser = UserInfo.getUser();
                                        mfUser.setUserId(user.getString("USER_ID"));
                                        mfUser.setUserNm(user.getString("USER_NAME"));

                                        //Home 화면으로 이동
                                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(homeIntent);

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
        }
    };


    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }
}
