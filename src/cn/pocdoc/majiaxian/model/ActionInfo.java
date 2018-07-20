package cn.pocdoc.majiaxian.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/3/4 0004.
 */
public class ActionInfo implements Serializable{
    String action_icon;
    int action_id;
    String action_name;
    String video_url;
    int duration;

    public String getActionIconUrl(){
        return action_icon;
    }

    public int getActionId(){
        return action_id;
    }

    public String getActionName(){
        return action_name;
    }

    public String getVideoUrl(){
        return video_url;
    }

    public int getDuration(){
        return duration;
    }


}
