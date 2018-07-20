package cn.pocdoc.majiaxian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.helper.ShareHelper;
import cn.pocdoc.majiaxian.service.RecordService;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class BadgeActivity extends BaseActivity {

    @ViewInject(R.id.no_share_btn)
    private TextView no_share_btn;
    @ViewInject(R.id.badge_img)
    private ImageView badgeImg;
    @ViewInject(R.id.badge_name)
    private TextView badgeName;
    @ViewInject(R.id.cancel_share)
    private TextView cancel_share;

    private int badgeId;

    @ViewInject(R.id.share_layout_btn)
    private LinearLayout share_layout_btn;

    @ViewInject(R.id.share_layout)
    private LinearLayout share_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badgeId = getIntent().getIntExtra("badgeId", 1);
        setContentView(R.layout.activity_badge);

        badgeImg.setImageResource(Config.BADGE_INFOS[badgeId - 1].getBigResourceId());
        badgeName.setText(Config.BADGE_INFOS[badgeId - 1].getName());
        no_share_btn.setText(Html.fromHtml("<u>低调路过</u>"));
        cancel_share.setText(Html.fromHtml("<u>取消</u>"));

        // TODO fix
//        for (int i = 0; i < Config.ITEM_INFOS.length; i++) {
//            if (RecordsDB.getInstance(this).getBadgeCount() >= Config.ITEM_INFOS[i].getLockBadgeNum()) {
//                PreferencesUtils.putBoolean(this, i + "item_status", true);
//                // TODO unlock
//                //Config.LocalCourses[i].setStatus(true);
//            }
//        }
        FontManager.changeFonts((ViewGroup) findViewById(R.id.activity_badge));

        //分享事件绑定
        LinearLayout parent = (LinearLayout)share_layout.getChildAt(0);
        for(int i = 0; i < parent.getChildCount(); ++i){
            View child = parent.getChildAt(i);
            child.setTag(i);
            child.setOnClickListener(shareClick);
        }
    }

    @OnClick({R.id.share_btn, R.id.no_share_btn, R.id.cancel_share})
    public void btn_click(View v) {
        if (v.getId() == R.id.share_btn) {
             share_layout_btn.setVisibility(View.GONE);
             share_layout.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.no_share_btn) {
            finish();
        }else if(v.getId() == R.id.cancel_share){
            finish();
        }
    }

    /**
     * 分享
     */
    private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = getString(R.string.app_name)+"，平坦腹部的最高境界";
            String content = "快来邀请小伙伴与你一起秀出"+getString(R.string.app_name)+"!更有任性徽章、超值大礼等你拿！";
            String url = "http://115.28.35.167/firmabs/hiit/share.html?badge="+badgeId;
            int imgIcon = R.drawable.logo;
            ShareHelper share = ShareHelper.getInstance();
            share.setShareContent(getContext(), imgIcon, url, title, content);
            share.setShareCallBack(new ShareHelper.shareCallBack() {
                @Override
                public void onStart() {
                    //showToast("开始分享");
                }
                @Override
                public void onComplete() {
                    showToast("分享成功");
                    finish();
                }
                @Override
                public void onError() {
                    showToast("分享失败");
                }
            });
            switch ((Integer)view.getTag()){
                case 0:
                    share.shareToWeiBo();
                    break;
                case 1:
                    share.shareToQZone();
                    break;
                case 2:
                    share.shareToWeiXin();
                    break;
                case 3:
                    share.shareToWxCircle();
                    break;
                default:
                    finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = ShareHelper.getInstance().getController().getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
