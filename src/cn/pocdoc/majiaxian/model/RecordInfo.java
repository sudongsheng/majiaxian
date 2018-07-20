package cn.pocdoc.majiaxian.model;

import java.util.List;

/**
 * Created by Administrator on 2015/1/28 0028.
 */
public class RecordInfo {

    public int id;
    public int courseId;
    public long startTime;
    public long endTime;
    public long uid;
    public String finished;
    public List<RecordDetailInfo> detailInfos;
    public List<BadgeSyncInfo> badgeSyncInfos;

    public RecordInfo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int recordId) {
        this.courseId = recordId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public List<RecordDetailInfo> getDetailInfos() {
        return detailInfos;
    }

    public void setDetailInfos(List<RecordDetailInfo> detailInfo) {
        this.detailInfos = detailInfo;
    }

    public List<BadgeSyncInfo> getBadgeSyncInfos() {
        return badgeSyncInfos;
    }

    public void setBadgeSyncInfos(List<BadgeSyncInfo> badgeSyncInfos) {
        this.badgeSyncInfos = badgeSyncInfos;
    }
}
