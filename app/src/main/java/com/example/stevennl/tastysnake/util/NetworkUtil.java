package com.example.stevennl.tastysnake.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevennl.tastysnake.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Manage HTTP request. Implemented as a singleton.
 * Author: LCY
 */
public class NetworkUtil {
    private static final String TAG = "NetworkUtil";
    private static final String REQUEST_TAG = "request";
    private static NetworkUtil instance = null;
    private RequestQueue queue;

    /**
     * Return the only instance.
     */
    public static NetworkUtil getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkUtil(context);
        }
        return instance;
    }

    /**
     * Initialize.
     *
     * @param context The context
     */
    private NetworkUtil(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Cancel all requests in the queue.
     */
    public void cancelAll() {
        queue.cancelAll(REQUEST_TAG);
    }

    /**
     * Return true if current network is available.
     *
     * @param context The context
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    /**
     * Get average W value from remote server.
     *
     * @param resListener Called when the result is got
     */
    public void getAvgW(final ResultListener<Integer> resListener) {
        get(Config.URL_GET_AVG_W, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getAvgW() Response: " + response);
                // TODO NETWORK Get average W value from response
                int avgW = 999;
                resListener.onGotResult(avgW);
            }
        }, null);
    }

    /**
     * Insert a W value to remote database.
     *
     * @param W The W value to be inserted
     */
    public void insertW(int W) {
        Map<String, String> params = new HashMap<>();
        params.put("w", String.valueOf(W));
        post(Config.URL_INSERT_W, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "insertW() Response: " + response);
            }
        }, null);
    }

    /**
     * Get all W values in remote database.
     *
     * @param resListener Called when the result is got
     * @param errListener Called when error occurs
     */
    public void getAllW(final ResultListener<ArrayList<Integer>> resListener,
                        Response.ErrorListener errListener) {
        get(Config.URL_GET_ALL_W, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getAllW() Response: " + response);
                // TODO NETWORK Generate W values list from response
                ArrayList<Integer> wValues = new ArrayList<>();
                wValues.add(1);
                wValues.add(2);
                wValues.add(3);
                resListener.onGotResult(wValues);
            }
        }, errListener);
    }

    /**
     * Build a GET url.
     *
     * @param url The server url
     * @param params GET parameters
     */
    public static String buildUrl(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Object[] keys = params.keySet().toArray();
        for (Object obj : keys) {
            String key = (String)obj;
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    /**
     * Send a GET request.
     *
     * @param url The server url
     * @param params GET parameters
     * @param resListener Called when receiving response
     * @param errListener Called when error occurs
     */
    public void get(String url, Map<String, String> params,
                    Response.Listener<String> resListener, Response.ErrorListener errListener) {
        String getUrl = buildUrl(url, params);
        StringRequest req = new StringRequest(getUrl, resListener, errListener);
        req.setRetryPolicy(new DefaultRetryPolicy(Config.REQ_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(REQUEST_TAG);
        queue.add(req);
    }

    /**
     * Send a POST request.
     *
     * @param url The server url
     * @param params POST parameters
     * @param resListener Called when receiving response
     * @param errListener Called when error occurs
     */
    public void post(String url, final Map<String, String> params,
                     Response.Listener<String> resListener, Response.ErrorListener errListener) {
        StringRequest req = new StringRequest(Request.Method.POST, url, resListener, errListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(Config.REQ_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(REQUEST_TAG);
        queue.add(req);
    }

    /**
     * Listener for network result.
     *
     * @param <T> The type of result
     */
    public interface ResultListener<T> {
        /**
         * Called when the result is got
         *
         * @param result The result got
         */
        void onGotResult(T result);
    }
}
