package cn.pocdoc.majiaxian.model;

/**
 * Created by Administrator on 2015/1/20 0020.
 */
public class BadgeInfo {
    private int id;
    private int smallResourceId;
    private int bigResourceId;
    private String name;
    private String rule;

    public BadgeInfo(int id, int smallResourceId, int bigResourceId, String name, String rule) {
        this.id = id;
        this.smallResourceId = smallResourceId;
        this.bigResourceId = bigResourceId;
        this.name = name;
        this.rule = rule;
    }

    public int getId() {
        return id;
    }

    public int getSmallResourceId() {
        return smallResourceId;
    }

    public int getBigResourceId() {
        return bigResourceId;
    }

    public String getName() {
        return name;
    }

    public String getRule() {
        return rule;
    }
}
