package skcc.com.fashiononeapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import skcc.com.fashiononeapp.R;
import skcc.com.fashiononeapp.util.Constants;
import skcc.com.fashiononeapp.util.DayAxisValueFormatter;
import skcc.com.fashiononeapp.util.JsonDataSet;
import skcc.com.fashiononeapp.util.JsonObjectRequest;
import skcc.com.fashiononeapp.util.MyMarkerView;
import skcc.com.fashiononeapp.util.RateMarkerView;
import skcc.com.fashiononeapp.util.ThousandCutAxisValueFormatter;
import skcc.com.fashiononeapp.util.UserInfo;

public class HomeFragment extends Fragment {
    BarChart mChart;
    PieChart pieChart;
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container,false);

        mChart = new BarChart(getActivity());

        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter();
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextSize(15f);
        IAxisValueFormatter yAxisFormatter = new ThousandCutAxisValueFormatter();
        yAxis.setValueFormatter(yAxisFormatter);

        YAxis ryAxis = mChart.getAxisRight();
        ryAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.NONE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setXOffset(-80f);
        l.setTextSize(15f);
        l.setTextColor(Color.BLACK);

        mChart.animateY(1000);

        mChart.setLayoutParams(lp);
        rootView.addView(mChart);

        pieChart = new PieChart(getActivity());
        pieChart.setLayoutParams(lp);
        rootView.addView(pieChart);

        pieChart.animateY(1000);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.setCenterText("■ 카테고리별 매출비율");
        pieChart.setCenterTextSize(15f);

        RateMarkerView rmv = new RateMarkerView(getActivity(), R.layout.custom_marker_view);
        pieChart.setMarker(rmv);

        Legend pl = pieChart.getLegend();
        pl.setEnabled(false);

        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();

        if (UserInfo.getUser().isLogin()) {
            makeChartS();
        }
    }

    private void makeChartS() {

        JsonDataSet param = new JsonDataSet();
        param.setTranId("CSMA0002");

        param.putField("POS_YN","N");
        param.putField("USER_ID","");
        param.putField("USER_PASSWORD","");

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {

                                ArrayList<BarEntry> entries = new ArrayList<>();
                                try {
                                    String json = "[{day:08.11,sales:1238474}," +
                                            "{day:08.12,sales:1757984}," +
                                            "{day:08.13,sales:2458945}," +
                                            "{day:08.14,sales:1248335}," +
                                            "{day:08.15,sales:3547877}," +
                                            "{day:08.16,sales:4510480}," +
                                            "{day:08.17,sales:234578}]";
                                    JSONArray refArr = new JSONArray(json);
                                    for (int i=0; i<refArr.length(); i++) {
                                        JSONObject day = refArr.getJSONObject(i);
                                        entries.add(new BarEntry(i, day.getInt("sales")));
                                    }

                                } catch (Exception e) {
                                    Log.e("ERR",e.getMessage());
                                }


                                BarDataSet set = new BarDataSet(entries, "■ 최근 1주일 매출");
                                set.setColors(Constants.AWEEK_COLORS);

                                BarData data = new BarData(set);
                                data.setBarWidth(0.8f);
                                data.setValueTextSize(0f);
                                mChart.setData(data);

                                List<PieEntry> pentries = new ArrayList<>();
                                try {
                                    String piejson = "[{sub:'MEN',sales:1238474},{sub:'WOMEN',sales:1757984},{sub:'JEAN',sales:2458945}]";
                                    JSONArray refArr = new JSONArray(piejson);

                                    float sum = 0;
                                    for (int i=0; i<refArr.length(); i++) {
                                        JSONObject day = refArr.getJSONObject(i);
                                        sum += day.getInt("sales");
                                    }
                                    for (int i=0; i<refArr.length(); i++) {
                                        JSONObject day = refArr.getJSONObject(i);
                                        pentries.add(new PieEntry((day.getInt("sales")/sum*100), day.getString("sub")));
                                    }


                                } catch (Exception e) {
                                    Log.e("ERR",e.getMessage());
                                }

                                PieDataSet pset = new PieDataSet(pentries, "Election Results");
                                pset.setColors(ColorTemplate.LIBERTY_COLORS);
                                PieData pdata = new PieData(pset);
                                pdata.setValueTextSize(0f);
                                pieChart.setData(pdata);



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


    }
}
