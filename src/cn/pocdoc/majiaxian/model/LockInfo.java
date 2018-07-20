package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/3/4 0004.
 */
public class LockInfo {
    private int code;
    private Data data;

    public int getCode(){
        return code;
    }

    public boolean isUnlocked(){
        return data.unlock;
    }

    public String getUnlockDesc(){
        return data.desc;
    }

    static class Data {
        boolean unlock;
        String desc;
    }
}
