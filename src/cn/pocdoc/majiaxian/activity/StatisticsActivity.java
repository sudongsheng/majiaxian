package cn.pocdoc.majiaxian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseTitleActivity;
import cn.pocdoc.majiaxian.adapter.StatisticsAdapter;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pengwei on 15/1/20.
 */
public class StatisticsActivity extends BaseTitleActivity {

    @ViewInject(R.id.listView)
    private ListView listView;

    @ViewInject(R.id.web_view)
    private WebView webView;

    @ViewInject(R.id.web_pb)
    private ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("活动统计");
        setNavBtn(R.drawable.back, "", 0, "    反馈");
        setTitleBackgroundResource(R.color.base);
        getTitleView().setTextColor(Color.WHITE);

        webView.getSettings().setJavaScriptEnabled(true);

        HttpUtils httpUtils = new HttpUtils();
        final String requestUrl = String.format(Config.STATISTICS_URL, PreferencesUtils.getString(this, "uid"));
        httpUtils.send(HttpRequest.HttpMethod.GET, requestUrl, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String res = responseInfo.result;
                res = res.replace("/keepfit_api/Public/js/","");
                LogUtils.e("***"+res);
//                File file = new File(getFilesDir()+"/js/index.html");
//                try {
//                    FileWriter writer = new FileWriter(file);
//                    writer.write(res);
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //webView.loadData(res, "text/html", "UTF-8");
                //webView.loadUrl("file://"+getFilesDir()+"/js/index.html");
                webView.loadDataWithBaseURL("file://"+getFilesDir()+"/js/",res,"text/html","utf-8","");
            }
            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress < 100){
                    pb.setProgress(newProgress);
                }else{
                    pb.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.loadUrl("file:///android_asset/error.html");
//                webView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View view, MotionEvent motionEvent) {
//                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                            StatisticsActivity.this.finish();
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                });
            }
        });

        /*listView.setAdapter(new StatisticsAdapter(this));

        if(!MainApplication.getInstance().isLogin()){
            startActivity(LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }*/
    }

    @Override
    protected void HandleTitleBarEvent(int buttonId, View v) {
        switch (buttonId) {
            case BaseTitleActivity.LEFT_BTN:
                finish();
                break;
            case BaseTitleActivity.RIGHT_BTN:
                startActivity(FeedBackActivity.class);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @OnClick(R.id.down_360)
    public void down360(View v){
        showToast("正在下载360手机助手");
        Uri uri = Uri.parse("http://openbox.mobilem.360.cn/channel/getUrl?src=cp&app=360box");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }
}