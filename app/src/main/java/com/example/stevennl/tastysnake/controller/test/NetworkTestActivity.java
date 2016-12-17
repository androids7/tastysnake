package com.example.stevennl.tastysnake.controller.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkTestActivity extends BaseActivity {
    private static final String TAG = "NetworkTestActivity";
    private NetworkUtil networkUtil;
    private TextView infoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);
        initNetworkUtil();
        initInfoTxt();
        initInsertWBtn();
        initGetAvgWBtn();
        initGetAllWBtn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkUtil.cancelAll();
    }

    private void initNetworkUtil() {
        networkUtil = NetworkUtil.getInstance(this);
        CommonUtil.showToast(this, NetworkUtil.isNetworkAvailable(this)
                ? "Network available" : "Network unavailable");
    }

    private void initInfoTxt() {
        infoTxt = (TextView) findViewById(R.id.req_test_infoTxt);
    }

    private void initInsertWBtn() {
        Button insertWBtn = (Button) findViewById(R.id.req_test_insertWBtn);
        insertWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w = CommonUtil.randInt(9000);
                networkUtil.insertW(CommonUtil.randInt(w));
                infoTxt.append("Insert w: " + w + "\n");
            }
        });
    }

    private void initGetAvgWBtn() {
        Button getAvgWBtn = (Button) findViewById(R.id.req_test_getAvgWBtn);
        getAvgWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtil.getAvgW(new NetworkUtil.ResultListener<Integer>() {
                    @Override
                    public void onGotResult(Integer result) {
                        infoTxt.append("Avg W: " + result + "\n");
                    }
                });
            }
        });
    }

    private void initGetAllWBtn() {
        Button getAllWBtn = (Button) findViewById(R.id.req_test_getAllWBtn);
        getAllWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtil.getAllW(new NetworkUtil.ResultListener<ArrayList<Integer>>() {
                    @Override
                    public void onGotResult(ArrayList<Integer> result) {
                        if (result.isEmpty()) {
                            infoTxt.append("W in remote DB: nothing\n");
                        } else {
                            infoTxt.append("W in remote DB:\n");
                            for (Integer w : result) {
                                infoTxt.append(w + "\n");
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        infoTxt.append(error.toString() + "\n");
                    }
                });
            }
        });
    }
}
