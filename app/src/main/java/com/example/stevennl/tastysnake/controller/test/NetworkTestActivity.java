package com.example.stevennl.tastysnake.controller.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.network.NetworkUtil;

import java.util.ArrayList;

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
                infoTxt.append("Insert w: " + w + "\n");
                networkUtil.insertW(w, new NetworkUtil.ResultListener<String>() {
                    @Override
                    public void onGotResult(String result) {
                        infoTxt.append(result + "\n");
                    }

                    @Override
                    public void onError(VolleyError err) {
                        infoTxt.append(err.toString() + "\n");
                    }
                });
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

                    @Override
                    public void onError(VolleyError err) {
                        infoTxt.append(err.toString() + "\n");
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
                networkUtil.getAllW(new NetworkUtil.ResultListener<ArrayList<String>>() {
                    @Override
                    public void onGotResult(ArrayList<String> result) {
                        if (result.isEmpty()) {
                            infoTxt.append("W in remote DB: nothing\n");
                        } else {
                            infoTxt.append("W in remote DB:\n");
                            for (String w : result) {
                                infoTxt.append(w);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError err) {
                        infoTxt.append(err.toString() + "\n");
                    }
                });
            }
        });
    }
}
