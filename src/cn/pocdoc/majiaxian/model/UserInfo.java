package cn.pocdoc.majiaxian.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2015/1/27 0027.
 */
public class UserInfo {
    public static final String QQ="2";
    public static final String SINA="1";
    public static final String WEIXIN="3";

    public static final String NO_LOGIN="0";

    @SerializedName("uid")
    public String uid;
    public String openId;
    public String name;
    public String headImg;
    public String type;

    public UserInfo(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
