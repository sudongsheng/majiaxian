package cn.pocdoc.majiaxian.helper;


import android.app.Activity;
import android.content.Context;
import cn.pocdoc.majiaxian.config.Config;
import com.umeng.socialize.bean.*;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by pengwei on 15/1/22.
 */
public class ShareHelper {

    private final UMSocialService mController;
    public static ShareHelper shareControl;
    private final SocializeConfig socializeConfig;

    private Context context;
    private String title;
    private String content;
    private UMImage img;
    private String shareUrl;

    public ShareHelper() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share",
                RequestType.SOCIAL);
        socializeConfig = mController.getConfig();
        socializeConfig.setSsoHandler(new SinaSsoHandler());
    }

    public static ShareHelper getInstance() {
        if (shareControl == null) {
            shareControl = new ShareHelper();
        }
        return shareControl;
    }

    public UMSocialService getController() {
        return mController;
    }

    public interface  shareCallBack{
        public void onStart();
        public void onComplete();
        public void onError();
    }

    public shareCallBack callBack;
    public void setShareCallBack(shareCallBack callBack){
        if(callBack != null){
            this.callBack = callBack;
        }
    }

    public void setShareContent(final Context context, int imgRes,
                                   String shareUrl, String title, String content) {
        this.context = context;
        this.shareUrl = shareUrl;
        this.title = title;
        this.content = content;
        this.img = new UMImage(context, imgRes);
    }

    public void setShareIcon(String url){
        this.img = new UMImage(context, url);
    }

    public void shareToWeiXin(){
        UMWXHandler wxHandler = new UMWXHandler(context, Config.wx_appID, Config.wx_appSecret);
        wxHandler.addToSocialSDK();
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareImage(img);
        mController.setShareMedia(weixinContent);
        this.postShare(context, SHARE_MEDIA.WEIXIN);
    }

    public void shareToWxCircle(){
        UMWXHandler wxCircleHandler = new UMWXHandler(context, Config.wx_appID, Config.wx_appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(img);
        circleMedia.setTargetUrl(shareUrl);
        mController.setShareMedia(circleMedia);
        this.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    public void shareToWeiBo(){
        SinaShareContent sinaShare = new SinaShareContent();
        sinaShare.setTargetUrl(shareUrl);
        sinaShare.setTitle(title);
        sinaShare.setAppWebSite(shareUrl);
        sinaShare.setShareContent(content + shareUrl);
        sinaShare.setShareImage(img);
        mController.setShareMedia(sinaShare);
        this.postShare(context, SHARE_MEDIA.SINA);
    }

    public void shareToQQ(){
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context, Config.qq_appID,
                Config.qq_appKey);
        qqSsoHandler.addToSocialSDK();
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareImage(img);
        qqShareContent.setTargetUrl(shareUrl);
        mController.setShareMedia(qqShareContent);
        this.postShare(context, SHARE_MEDIA.QQ);
    }

    public void shareToQZone(){
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity)context, Config.qq_appID,
                Config.qq_appKey);
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(shareUrl);
        qzone.setTitle(title);
        qzone.setShareImage(img);
        mController.setShareMedia(qzone);
        this.postShare(context, SHARE_MEDIA.QZONE);
    }


    /**
     * 分享回调函数
     * @param mContext
     * @param media
     */
    private void postShare(final Context mContext, SHARE_MEDIA media){
        mController.postShare(mContext,media,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        if(callBack != null){
                            callBack.onStart();
                        }
                    }
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                        if (eCode == 200) {
                            if(callBack != null){
                                callBack.onComplete();
                            }
                        } else {
                            if(callBack != null){
                                callBack.onError();
                            }
                        }
                    }
                });
    }



}
