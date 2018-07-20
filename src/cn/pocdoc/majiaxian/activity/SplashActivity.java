package cn.pocdoc.majiaxian.activity;

import android.os.Bundle;
import android.os.Handler;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.service.RecordService;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;

/**
 * Created by pengwei on 15/1/19.
 */
public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RecordService.startRecordService();
        if(MainApplication.isLogin() && !PreferencesUtils.getBoolean(this, "has_handle")){
            PreferencesUtils.putString(this, "real_uid", PreferencesUtils.getString(this,"uid"));
            MainApplication.getInstance().logout();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.class);
                finish();
            }
        }, 2000);
    }
}