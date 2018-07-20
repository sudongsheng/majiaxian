package cn.pocdoc.majiaxian.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.activity.base.BaseTitleActivity;
import cn.pocdoc.majiaxian.config.Config;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pengwei on 15/3/4.
 */
public class WebBrowserActivity extends BaseTitleActivity {

    @ViewInject(R.id.webView)
    private WebView webView;
    @ViewInject(R.id.pb)
    private ProgressBar pb;

    private String url;
    private boolean loadError = false;
    private boolean needTouchToBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        setTitle("成就");
        setTitleBackgroundColor("#eb3312");
        setTitleColor("#ffffff");
        setNavBtn(BaseTitleActivity.BTN_BACK, "", 0, "勋章兑换");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    pb.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //pb.setVisibility(View.VISIBLE);
                String tempUrl = url;
                LogUtils.e(url);
                String prefix = "pocdoc://";
                if (url.startsWith(prefix)) {
                    try {
                        JSONObject json = new JSONObject(url.substring(prefix.length(), url.length()));
                        url = Config.HOST_URL + "keepfit_api" + json.getJSONObject("params").getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LogUtils.e("tempUrl:" + tempUrl);
                WebBrowserActivity.this.url = url;
                webView.loadUrl(url);
                //hideTitle();

                needTouchToBack = true;
                setTitleContentViewVisibility(View.INVISIBLE);

                webView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            onBack(null);
                            needTouchToBack = false;
                            webView.setOnTouchListener(null);
                            setTitleContentViewVisibility(View.VISIBLE);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError = true;
                webView.loadUrl("file:///android_asset/error.html");
                webView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            onBack(null);
                            webView.setOnTouchListener(null);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
        });
        //hideTitle();
        webView.loadUrl(url);
        if (url.endsWith("exchange")){
            needTouchToBack = true;
            setTitleContentViewVisibility(View.INVISIBLE);
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        needTouchToBack = false;
                        setTitleContentViewVisibility(View.VISIBLE);
                        finish();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack(null);
    }

    /**
     * 打开网页
     *
     * @param url
     */
    public static void initUrl(String url) {
        Context context = MainApplication.getInstance();
        Intent it = new Intent(context, WebBrowserActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("url", url);
        context.startActivity(it);
    }

    void onBack(View view) {
        if (!loadError && webView.canGoBack()) {
            webView.goBack();
            //hideTitle(false);
        } else {
            this.finish();
        }
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (needTouchToBack){
                needTouchToBack = false;
                webView.setOnTouchListener(null);
                setTitleContentViewVisibility(View.VISIBLE);
            }
            onBack(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * TitleBar事件处理
     *
     * @param buttonId
     * @param v
     */
    @Override
    protected void HandleTitleBarEvent(int buttonId, View v) {
        if (buttonId == BaseTitleActivity.LEFT_BTN) {
            onBack(null);
        } else if (buttonId == BaseTitleActivity.RIGHT_BTN) {
            WebBrowserActivity.initUrl(Config.HOST_URL + "keepfit_api/Badge/Badge/exchange");
        }
    }

    private void hideTitle(){
        hideTitle(url.endsWith("exchange") || url.indexOf("page?") != -1);
    }

    private void setTitleContentViewVisibility(int visibility){
        setTitleBarLeftImageButtonVisibility(visibility);
        setTitleBarTitleVisibility(visibility);
        setTitleBarRightButtonVisibility(visibility);
    }
}
