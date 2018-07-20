package cn.pocdoc.majiaxian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseTitleActivity;
import cn.pocdoc.majiaxian.adapter.AchievementAdapter;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.utils.LogUtil;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengwei on 15/1/20.
 */
public class AchievementActivity extends BaseTitleActivity {

    @ViewInject(R.id.gridView)
    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        setTitle("成就1");
        setTitleBackgroundResource(R.color.achievement_bg);
        setNavBtn(R.drawable.back, "", 0, "徽章兑换");
        getTitleView().setTextColor(Color.WHITE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AchievementActivity.this, AchievementDetailActivity.class);
                intent.putExtra("badgeId", i + 1);
                TextView num = (TextView) view.findViewById(R.id.achievement_number);
                if(num != null && !TextUtils.isEmpty(num.getText())){
                    intent.putExtra("show", true);
                }
                startActivity(intent);
            }
        });

        if(!MainApplication.getInstance().isLogin()){
            startActivity(LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    private void errorHandle() {
        Map<Integer, Integer> badgeNum = new HashMap<Integer, Integer>();
        String cacheBadge = PreferencesUtils.getString(getContext(), "badge_cache");
        if (!MainApplication.isLogin() || TextUtils.isEmpty(cacheBadge)) {
            gridView.setAdapter(new AchievementAdapter(getContext(), badgeNum));
        } else {
            JSONArray array = null;
            try {
                array = new JSONArray(cacheBadge);
                for (int i = 0; i < array.length(); i++) {
                    badgeNum.put(
                            Integer.parseInt(array.getJSONObject(i).getString("badge_id")),
                            Integer.parseInt(array.getJSONObject(i).getString("badgecount"))
                    );
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            gridView.setAdapter(new AchievementAdapter(getContext(), badgeNum));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                String.format(Config.SYNC_BADGE, PreferencesUtils.getString(this, "uid")), null, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            LogUtil.d("record", responseInfo.result);
                            JSONObject result = new JSONObject(responseInfo.result);
                            if (result.getString("code").equals("0")) {
                                Map<Integer, Integer> badgeNum = new HashMap<Integer, Integer>();
                                PreferencesUtils.putString(getContext(), "badge_cache", result.getString("data"));
                                JSONArray array = result.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    badgeNum.put(
                                            Integer.parseInt(array.getJSONObject(i).getString("badge_id")),
                                            Integer.parseInt(array.getJSONObject(i).getString("badgecount"))
                                    );
                                }
                                gridView.setAdapter(new AchievementAdapter(getContext(), badgeNum));
                            } else {
                                errorHandle();
                            }
                        } catch (JSONException e) {
                            errorHandle();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        errorHandle();
                    }
                });
    }


    @Override
    protected void HandleTitleBarEvent(int buttonId, View v) {
        switch (buttonId) {
            case BaseTitleActivity.LEFT_BTN:
                finish();
                break;
            case BaseTitleActivity.RIGHT_BTN:
                Intent intent = new Intent(AchievementActivity.this, AchievementDetailActivity.class);
                intent.putExtra("badgeId", -1);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}