package cn.pocdoc.majiaxian.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/4 0004.
 */
public class CourseDetailInfo implements Serializable{
    private int code;
    private Data data;

    public CourseDetailInfo(){
        code = 0;
        data = new Data();
    }

    public int getCode(){
        return code;
    }

    public int getCourseId(){
        return data.course_id;
    }

    public void setCode(int code){
        this.code = code;
    }

    public void setCourseId(int courseId){
        this.data.course_id = courseId;
    }
    public void setActions(List<ActionInfo> actions){
        this.data.actions = (ArrayList<ActionInfo>) actions;
    }

    public void addAction(ActionInfo actionInfo){
        this.data.actions.add(actionInfo);
    }

    public ArrayList<ActionInfo> getActions(){
        return data.actions;
    }

    private static class Data{
        Data(){
           course_id = -1;
            actions = new ArrayList<ActionInfo>();
        }
        int course_id;
        ArrayList<ActionInfo> actions;
    }

}
