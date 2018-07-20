package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/1/28 0028.
 */
public class RecordDetailInfo {

    public long startTime;
    public long endTime;
    public long opId;

    public RecordDetailInfo(){}

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getOpId() {
        return opId;
    }

    public void setOpId(long opId) {
        this.opId = opId;
    }

}
