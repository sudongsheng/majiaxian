package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class LocalCourseInfo extends CourseInfo {

    public int resourcesId;
    public int lockResourceId;

    public LocalCourseInfo(int id, String name, String subdesc, String desc, boolean isUnlocked, int resourcesId, int lockResourceId) {
        this.id = id;
        this.name = name;
        this.desc= desc;
        this.subdesc = subdesc;
        this.lock = isUnlocked;
        this.resourcesId = resourcesId;
        this.lockResourceId = lockResourceId;
    }

    public int getResourcesId() {
        return resourcesId;
    }

    public int getLockResourceId() {
        return lockResourceId;
    }
}
