package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class LocalActionInfo extends ActionInfo {

//    private String action_icon;
//    private int action_id;
//    private String action_name;
//    private String video_url;
//    private int duration;

    //组图片id
    public int actionResourceId;
    //组视屏id
    public int actionVideoId;

    public LocalActionInfo(int actionId, int duration, String actionName, int actionResourceId, int actionVideoId) {
        this.action_id = actionId;
        this.duration = duration;
        this.action_name = actionName;
        this.actionResourceId = actionResourceId;
        this.actionVideoId = actionVideoId;
    }

    public int getActionResourceId() {
        return actionResourceId;
    }

    public int getActionVideoId() {
        return actionVideoId;
    }

}
