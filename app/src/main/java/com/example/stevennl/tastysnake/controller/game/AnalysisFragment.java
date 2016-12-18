package com.example.stevennl.tastysnake.controller.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.AnalysisData;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.NetworkUtil;

/**
 * Data analysis page.
 * Author: LCY
 */
public class AnalysisFragment extends Fragment {
    private static final String TAG = "AnalysisFragment";
    private GameActivity act;
    private Handler handler;
    private NetworkUtil networkUtil;

    private TextView infoTxt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        networkUtil = NetworkUtil.getInstance(act);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analysis, container, false);
        initInfoTxt(v);
        analyzeRemoteData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    CommonUtil.showViewPretty(infoTxt);
                }
            }
        }, Config.DELAY_ANALYSIS_FRAG);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkUtil.cancelAll();
    }

    private void initInfoTxt(View v) {
        infoTxt = (TextView) v.findViewById(R.id.analysis_infoTxt);
        infoTxt.setVisibility(View.GONE);
        String info;
        AnalysisData data = AnalysisData.create(act);
        if (data != null) {
            info = getString(R.string.analysis_local, data.N, data.X, data.A,
                    data.B, data.Y, data.C, data.D, data.T, data.L1, data.L2, data.W, data.P);
        } else {
            info = getString(R.string.analysis_no_data);
        }
        infoTxt.setText(info);
    }

    /**
     * Analyze remote data and show the result.
     */
    private void analyzeRemoteData() {
        if (NetworkUtil.isNetworkAvailable(act)) {
            networkUtil.getAvgW(new NetworkUtil.ResultListener<Integer>() {
                @Override
                public void onGotResult(Integer result) {
                    Log.d(TAG, "Got avg W: " + result);
                    // TODO DATA_ANALYSIS Compute U and show info
//                    infoTxt.append("\n\n" + getString(R.string.analysis_remote_exceed, 10));
//                    infoTxt.append("\n\n" + getString(R.string.analysis_remote_equal));
//                    infoTxt.append("\n\n" + getString(R.string.analysis_remote_below, 20));
                }

                @Override
                public void onError(VolleyError err) {
                    Log.e(TAG, err.toString());
                }
            });
        }
    }
}
