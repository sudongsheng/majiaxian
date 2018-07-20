package cn.pocdoc.majiaxian.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.helper.MobClickAgentHelper;
import cn.pocdoc.majiaxian.model.BadgeInfo;
import cn.pocdoc.majiaxian.utils.FontManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by pengwei on 15/1/20.
 */
public class AchievementDetailActivity extends BaseActivity {

    @ViewInject(R.id.achievement_detail_name)
    private TextView name;
    @ViewInject(R.id.achievement_detail_rule)
    private TextView rule;

    private int badgeId;
    private boolean show;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badgeId = getIntent().getIntExtra("badgeId", 0);
        show = getIntent().getBooleanExtra("show",false);
        setContentView(R.layout.activity_achievement_detail);

        if (badgeId == -1) {
            name.setText("徽章兑换");
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f);
            //rule.setText("最先集齐100个徽章的前100名用户每人可获得精美瑜伽垫一个！\n\n1.关注微信账号\"马甲线（majialine）\"\n2.发送\"徽章兑换+手机号\"\n3.工作人员与您联系,确认您的数据\n4.等待奖品上门");
            rule.setText(getString(R.string.prize_rule));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rule.getLayoutParams();
            params.setMargins(80, 35, 80, 0);
            rule.setGravity(Gravity.LEFT);
            rule.setLayoutParams(params);
            MobClickAgentHelper.onEvent("badge_conversion_click");
        } else {
            BadgeInfo info = Config.BADGE_INFOS[badgeId - 1];
            name.setText(info.getName());
            //if (RecordsDB.getInstance(this).getBadgeNum().get(badgeId) == null)
            if (!show)
                name.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.badge_unknow), null, null);
            else
                name.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(info.getBigResourceId()), null, null);
            rule.setText(info.getRule());
            MobClickAgentHelper.onEvent("badge_description_click");
        }

        findViewById(R.id.activity_achievement_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AchievementDetailActivity.this.finish();
            }
        });

        FontManager.changeFonts((ViewGroup) findViewById(R.id.activity_achievement_detail));
    }

    @OnClick(R.id.achievement_detail_btn_close)
    public void close(View v) {
        finish();
    }
}