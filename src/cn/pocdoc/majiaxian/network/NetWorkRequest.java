package cn.pocdoc.majiaxian.network;

import android.content.Context;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.utils.CustomDialog;
import cn.pocdoc.majiaxian.utils.LogUtil;
import cn.pocdoc.majiaxian.utils.Pop;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Xuri on 2014/11/12.
 */
public class NetWorkRequest {

    private final String TAG = "NetWorkRequest";
    private Context context;
    private RequestQueue mQueue;
    private StringRequest stringRequest;

    private CustomDialog mProgressDialog;
    private boolean toShow = true;

    public NetWorkRequest(Context context) {
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    public void setProgressDialog(boolean toShow) {
        this.toShow = toShow;
    }

    public void getRequest(String url, final Class classOfT, final NetworkFinListener listener) {
        showProgress();
        stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelProgress();
                        LogUtil.d(TAG, "getNetWorkData:" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if ("1".equals(status)) {
                                listener.finish(new Gson().fromJson(object.getString("data"), classOfT));
                            } else {
                                listener.error(Integer.parseInt(status), object.getString("msg"));
                            }
                        } catch (JSONException e) {
                            listener.parseError(1);
                        } catch (Exception e) {
                            listener.parseError(2);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgress();
                listener.error();
            }
        });
        mQueue.add(stringRequest);
    }

    public void getRequest(String url, final Type typeOfT, final NetworkFinListener listener) {
        showProgress();
        stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelProgress();
                        LogUtil.d(TAG, "getNetWorkData:" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("code");
                            if ("0".equals(status)) {
                                listener.finish(object.getString("data"), object.getString("msg"));
                                listener.finish(new Gson().fromJson(object.getString("data"), typeOfT));
                            } else {
                                listener.error(Integer.parseInt(status), object.getString("msg"));
                            }
                        } catch (JSONException e) {
                            listener.parseError(1);
                        } catch (Exception e) {
                            listener.parseError(2);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
                cancelProgress();
            }
        });
        mQueue.add(stringRequest);
    }

    public void postRequest(String url, final Class classOfT, final Map map, final NetworkFinListener listener) {
        LogUtil.d("URL", "url:" + url);
        showProgress();
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelProgress();
                        LogUtil.d(TAG, "getNetWorkData:" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("code");
                            String data = object.getString("data");
                            String msg = object.getString("msg");
                            if ("0".equals(status)) {
                                LogUtil.d(TAG, data);
                                listener.finish(data, msg);
                                if (data == null || data.length() == 0 || data.equals("[]") || data.equals("{}"))
                                    listener.finish(msg);
                                else{
                                    listener.finish(new Gson().fromJson(data, classOfT));
                                }
                            } else {
                                listener.error(Integer.parseInt(status), object.getString("msg"));
                            }
                        } catch (JSONException e) {
                            listener.parseError(1);
                        } catch (Exception e) {
                            listener.parseError(2);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgress();
                listener.error();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    public void postRequest(String url, final Type typeOfT, final Map map, final NetworkFinListener listener) {
        showProgress();
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelProgress();
                        LogUtil.d(TAG, "getNetWorkData:" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("code");
                            String data = object.getString("data");
                            String msg = object.getString("msg");
                            if ("0".equals(status)) {
                                LogUtil.d(TAG, data);
                                listener.finish(data, msg);
                                if (data == null || data.length() == 0 || data.equals("[]"))
                                    listener.finish(msg);
                                else
                                    listener.finish(new Gson().fromJson(data, typeOfT));
                            } else {
                                listener.error(Integer.parseInt(status), object.getString("msg"));
                            }
                        } catch (JSONException e) {
                            listener.parseError(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.d("test", e.toString());
                            listener.parseError(2);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgress();
                listener.error();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    private void showProgress() {
        if (toShow) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                return;
            mProgressDialog = Pop.popDialog(context, R.layout.window_layout, R.style.customDialog);
        }
    }

    private void cancelProgress() {
        if (toShow && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
