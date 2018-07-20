package cn.pocdoc.majiaxian.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/4 0004.
 */
public class CourseListInfo {
    private int code;
    private Data data;

    public CourseListInfo(){
        code = 0;
        data = new Data();

    }

    public int getCode(){
        return code;
    }

    public ArrayList<CourseInfo> getCourses(){
        return data.courses;
    }

    public void addCourse(CourseInfo courseInfo){
        this.data.courses.add(courseInfo);
    }

    public void addCourses(ArrayList<CourseInfo> courseInfos){
        this.data.courses.addAll(courseInfos);
    }

    public CourseInfo getCourse(int courseId){
        for (CourseInfo courseInfo : data.courses){
            if (courseInfo.getId() == courseId){
                return courseInfo;
            }
        }

        return null;
    }

    private static class Data{
        Data(){
            courses = new ArrayList<CourseInfo>();
        }
        ArrayList<CourseInfo> courses;
    }
}
