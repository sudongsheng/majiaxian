package cn.pocdoc.majiaxian.helper;

import cn.pocdoc.majiaxian.MainApplication;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by pengwei on 15/1/25.
 */
public class MobClickAgentHelper {

    /**
     * 点击事件统计
     * @param event_id
     */
    public static void onEvent(String event_id){
        MobclickAgent.onEvent(MainApplication.getInstance(), event_id);
    }

    public static void onExitEvent(){
        onEvent("exit_num");
    }
}
