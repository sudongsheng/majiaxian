package cn.pocdoc.majiaxian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.helper.MobClickAgentHelper;
import cn.pocdoc.majiaxian.model.UserInfo;
import cn.pocdoc.majiaxian.network.NetWorkRequest;
import cn.pocdoc.majiaxian.network.NetworkFinListener;
import cn.pocdoc.majiaxian.utils.LogUtil;
import cn.pocdoc.majiaxian.utils.Pop;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Xuri on 2015/1/27 0027.
 */
public class LoginActivity extends BaseActivity {
    UMSocialService mController;
    NetWorkRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        request = new NetWorkRequest(this);
    }

    @OnClick({R.id.activity_login, R.id.qq_login, R.id.weibo_login, R.id.weixin_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login:
                finish();
                break;
            case R.id.qq_login:
                MobClickAgentHelper.onEvent("login_qq");
                login(SHARE_MEDIA.QQ);
                break;
            case R.id.weibo_login:
                MobClickAgentHelper.onEvent("login_weibo");
                login(SHARE_MEDIA.SINA);
                break;
            case R.id.weixin_login:
                MobClickAgentHelper.onEvent("login_weixin");
                login(SHARE_MEDIA.WEIXIN);
                break;
        }
    }

    private void login(final SHARE_MEDIA share_media) {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        if (share_media == SHARE_MEDIA.SINA)
            mController.getConfig().setSsoHandler(new SinaSsoHandler());
        if (share_media == SHARE_MEDIA.QQ) {
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this, Config.qq_appID, Config.qq_appKey);
            qqSsoHandler.addToSocialSDK();
        }
        if (share_media == SHARE_MEDIA.WEIXIN) {
            UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, Config.wx_appID, Config.wx_appSecret);
            wxHandler.addToSocialSDK();
        }
        mController.doOauthVerify(LoginActivity.this, share_media, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
            }

            @Override
            public void onComplete(final Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    mController.getPlatformInfo(LoginActivity.this, share_media, new SocializeListeners.UMDataListener() {
                        @Override
                        public void onComplete(int status, Map<String, Object> info) {
                            if (status == 200 && info != null) {
                                StringBuilder sb = new StringBuilder();
                                Set<String> keys = info.keySet();
                                for (String key : keys) {
                                    sb.append(key + "=" + info.get(key).toString() + "\r\n");
                                }
                                String openid = value.getString("openid");
                                //sb.append("openid:"+openid);
                                LogUtil.d("TestData", sb.toString());
                                UserInfo userInfo = new UserInfo();
                                try{
                                    if (share_media == SHARE_MEDIA.SINA) {
                                        userInfo.setOpenId(info.get("uid").toString());
                                        userInfo.setName(info.get("screen_name").toString());
                                        userInfo.setHeadImg(info.get("profile_image_url").toString());
                                        userInfo.setType(UserInfo.SINA);
                                    } else if (share_media == SHARE_MEDIA.QQ) {
                                        userInfo.setOpenId(openid);
                                        userInfo.setName(info.get("screen_name").toString());
                                        userInfo.setHeadImg(info.get("profile_image_url").toString());
                                        userInfo.setType(UserInfo.QQ);
                                    } else if (share_media == SHARE_MEDIA.WEIXIN) {
                                        userInfo.setOpenId(info.get("unionid").toString());
                                        userInfo.setName(info.get("nickname").toString());
                                        userInfo.setHeadImg(info.get("headimgurl").toString());
                                        userInfo.setType(UserInfo.WEIXIN);
                                        PreferencesUtils.putString(getContext(),
                                                "openid110",info.get("openid").toString());
                                    }
                                }catch (NullPointerException e){
                                    showToast("登录出现异常");return;
                                }

                                login(userInfo);
                            } else {
                                LogUtil.d("TestData", "发生错误：" + status);
                            }
                        }

                        @Override
                        public void onStart() {
                            Toast.makeText(LoginActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });

    }

    public void login(final UserInfo userInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("openid", userInfo.getOpenId());
        map.put("name", userInfo.getName());
        map.put("head_url", userInfo.getHeadImg());
        map.put("type", userInfo.getType());
        if(!PreferencesUtils.getBoolean(this, "has_handle")){
            map.put("uid110", PreferencesUtils.getString(this, "real_uid"));
            map.put("openid110", PreferencesUtils.getString(this, "openid110"));
        }
        request.setProgressDialog(false);
        request.postRequest(Config.LOGIN, UserInfo.class, map, new NetworkFinListener(this) {
            @Override
            public void finish(Object o) {
                PreferencesUtils.putBoolean(getContext(), "has_handle", true);
                userInfo.setUid(((UserInfo) o).getUid());
                Pop.popToast(LoginActivity.this, "登录成功");
                MainApplication.getInstance().UserLogin(userInfo);
                LoginActivity.this.finish();
                updateDatabaseUid();
                startUpdate();
            }

            @Override
            public void parseError(int i) {
                Pop.popToast(LoginActivity.this, "登录失败");
            }

            @Override
            public void error(int status, String msg) {
                Pop.popToast(LoginActivity.this, "登录失败");
            }

            @Override
            public void error() {
                Pop.popToast(LoginActivity.this, "登录失败");
            }
        });
    }

    public void updateDatabaseUid() {
        RecordsDB.getInstance(this).updateUid(MainApplication.getInstance().mUser.getUid());
    }

    public void startUpdate() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
