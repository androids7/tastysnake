package com.example.stevennl.tastysnake.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stevennl.tastysnake.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     * @param listener A {@link ResultListener}
     */
    public void getAvgW(@Nullable final ResultListener<Integer> listener) {
        get(Config.URL_GET_AVG_W, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getAvgW() response: " + response);
                int avgW = 0;
                try {
                    avgW = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "getAvgW() error:", e);
                }
                if (listener != null) {
                    listener.onGotResult(avgW);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getAvgW(): " + error.toString());
                if (listener != null) {
                    listener.onError(error);
                }
            }
        });
    }

    /**
     * Insert a W value to remote database.
     *
     * @param w The W value to be inserted
     * @param listener A {@link ResultListener}
     */
    public void insertW(int w, @Nullable final ResultListener<String> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("id", Config.DEVICE_ID);
        params.put("w", String.valueOf(w));
        post(Config.URL_INSERT_W, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "insertW() response: " + response);
                if (listener != null) {
                    listener.onGotResult(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "insertW(): " + error.toString());
                if (listener != null) {
                    listener.onError(error);
                }
            }
        });
    }

    /**
     * Get all W values in remote database.
     *
     * @param listener A {@link ResultListener}
     */
    public void getAllW(@Nullable final ResultListener<ArrayList<String>> listener) {
        get(Config.URL_GET_ALL_W, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getAllW() response: " + response);
                String[] vals = response.split(";");
                if (listener != null) {
                    listener.onGotResult(new ArrayList<>(Arrays.asList(vals)));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getAllW(): " + error.toString());
                if (listener != null) {
                    listener.onError(error);
                }
            }
        });
    }

    /**
     * Remove all W values in remote database.
     *
     * @param listener A {@link ResultListener}
     */
    public void removeAllW(@Nullable final ResultListener<String> listener) {
        get(Config.URL_REMOVE_ALL_W, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "removeAllW() response: " + response);
                if (listener != null) {
                    listener.onGotResult(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getAllW(): " + error.toString());
                if (listener != null) {
                    listener.onError(error);
                }
            }
        });
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

        /**
         * Called when error occurs.
         *
         * @param err {@link VolleyError}
         */
        void onError(VolleyError err);
    }
}
