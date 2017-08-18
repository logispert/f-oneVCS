package skcc.com.fashiononeapp.onlinedd;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import skcc.com.fashiononeapp.R;

/**
 * Created by 07237 on 2017-07-20.
 */

public class ListDeliveryView extends LinearLayout{
    TextView tv_styleCd;
    TextView tv_reqQty;
    TextView tv_shopQty;
    TextView tv_expFee;

    public ListDeliveryView(Context context) {
        super(context);

        //동적으로 xml을 생성해주는 inflater 선언
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //col2.xml 을 inflate --> 동적 생성
        inflater.inflate(R.layout.ondelivery_list, this);

        //col2에 있는 위젯선언
        tv_styleCd = (TextView)findViewById(R.id.style_cd);
        tv_reqQty = (TextView)findViewById(R.id.req_qty);
        tv_shopQty = (TextView)findViewById(R.id.shop_qty);
        tv_expFee = (TextView)findViewById(R.id.exp_fee);

    }

    // textview 위젯에 텍스트를 입력할 수 있도록 해주는 public 메소드
    public void setStyleCd(String styleCd) { tv_styleCd.setText(styleCd);}
    public void setReqQty(String reqQty) { tv_reqQty.setText(reqQty);}
    public void setShopQty(String shopQty) { tv_shopQty.setText(shopQty);}
    public void setExpFee(String expFee) { tv_expFee.setText(expFee);}
}
