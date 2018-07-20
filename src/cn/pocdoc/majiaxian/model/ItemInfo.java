package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class ItemInfo {
    //项目id
    public int id;
    //项目下每个组信息
    public LocalActionInfo[] groups;
    //休息时间
    public int restTime;
    //需要解锁个数
    public int lockBadgeNum;

    public ItemInfo(int id, LocalActionInfo[] groups, int restTime,int lockBadgeNum) {
        this.id = id;
        this.groups = groups;
        this.restTime = restTime;
        this.lockBadgeNum=lockBadgeNum;
    }

    public int getId() {
        return id;
    }

    public int getGroupNum() {
//        return groups.length;
        return 1;
    }

    public LocalActionInfo[] getGroups() {
        return groups;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getLockBadgeNum() {
        return lockBadgeNum;
    }

}
