package cn.pocdoc.majiaxian.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/3/4 0004.
 */
public class CourseInfo implements Serializable{
    int id;
    String name;
    String desc;
    String imageurl;
    String subdesc;
    boolean lock;

    CourseDetailInfo courseDetailInfo;


    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDesc(){
        return desc;
    }

    public String getImageUrl(){
        return imageurl;
    }


    public String getSubdesc(){
        return subdesc;
    }

    public CourseDetailInfo getCourseDetailInfo(){
        return courseDetailInfo;
    }

    public boolean isUnlocked(){
        return lock;
    }

    public void setStatus(boolean isUnlocked){
        this.lock = isUnlocked;
    }

    public void setCourseDetailInfo(CourseDetailInfo courseDetailInfo){
        this.courseDetailInfo = courseDetailInfo;
    }
}
