package cn.pocdoc.majiaxian.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.BadgeActivity;
import cn.pocdoc.majiaxian.config.Config;

/**
 * Created by pengwei on 15/3/6.
 */
public class NotificationHelper {

    /**
     * 徽章通知栏
     *
     * @param context
     * @param badgeId 徽章id
     */
    public static void showNotification(Context context, int badgeId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        int icon = R.drawable.icon;
        String badgeName = Config.BADGE_INFOS[badgeId-1].getName();
        CharSequence tickerText = "恭喜您获得“"+badgeName+"”徽章";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        CharSequence contentTitle = "徽章提示";
        CharSequence contentText = tickerText;
        notification.defaults |= Notification.DEFAULT_SOUND;
        Intent notificationIntent = new Intent(context, BadgeActivity.class);
        notificationIntent.putExtra("badgeId", badgeId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
    }
}
