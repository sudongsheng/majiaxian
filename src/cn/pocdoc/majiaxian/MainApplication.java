package cn.pocdoc.majiaxian;

import android.app.Application;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.CourseDetailInfo;
import cn.pocdoc.majiaxian.model.CourseInfo;
import cn.pocdoc.majiaxian.model.CourseListInfo;
import cn.pocdoc.majiaxian.model.UserInfo;
import cn.pocdoc.majiaxian.utils.LogUtil;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pengwei on 14/12/12.
 */
public class MainApplication extends Application {
    public static MainApplication mInstance = null;
    /**
     * 登录上后用户信息保存到此
     */
    public static UserInfo mUser = null;

    private CourseListInfo courseListInfo;

    public int currentCourseId; // not the index in the list
    public int currentActionIndex; // it is the index of the action list, not actionId

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setDebug(Config.isDebug);
        LogUtils.allowE = Config.isDebug;
        MobclickAgent.setDebugMode(Config.isDebug);
        com.umeng.socialize.utils.Log.LOG = Config.isDebug;
        mInstance = this;

        courseListInfo = new CourseListInfo();

        moveJsToData();

        CourseDetailInfo courseDetailInfo;
        CourseInfo courseInfo;
        for(int i = 0; i < Config.LocalCourses.length; i++){
            courseInfo = Config.LocalCourses[i];

            if (i == Config.LocalCourses.length - 1){
                courseInfo.setStatus(false);
            }else {
                courseDetailInfo = new CourseDetailInfo();
                for (int j = 0; j < Config.LocalActions[i].length; j ++){
                    courseDetailInfo.addAction(Config.LocalActions[i][j]);
                }
                courseDetailInfo.setCourseId(i);
                courseInfo.setCourseDetailInfo(courseDetailInfo);

            }
           courseListInfo.addCourse(courseInfo);
        }
    }

    public CourseListInfo getCourseListInfo(){
        return courseListInfo;
    }

    public static MainApplication getInstance() {
        return mInstance;
    }

    public static void UserLogin(UserInfo user) {
        mUser = user;
        PreferencesUtils.putString(MainApplication.getInstance(), "uid", mUser.getUid());
        PreferencesUtils.putString(MainApplication.getInstance(), "openid", mUser.getOpenId());
        PreferencesUtils.putString(MainApplication.getInstance(), "name", mUser.getName());
        PreferencesUtils.putString(MainApplication.getInstance(), "headImg", mUser.getHeadImg());
        PreferencesUtils.putString(MainApplication.getInstance(), "type", mUser.getType());
    }

    public static boolean isLogin() {
        if (PreferencesUtils.getString(MainApplication.getInstance(), "uid", UserInfo.NO_LOGIN).equals(UserInfo.NO_LOGIN))
            return false;
        else
            return true;
    }

    /**
     * 登出
     */
    public void logout(){
        PreferencesUtils.putString(this, "uid", UserInfo.NO_LOGIN);
    }

    /**
     * 移动js文件到data/data目录下
     */
    private void moveJsToData(){
        File file = new File(getFilesDir()+"/js");
        if(!new File(getFilesDir()+"/js/jquery.js").exists()){
            if(!file.exists()){
                file.mkdir();
            }
            moveAssetsFile("echarts-all.js");
            moveAssetsFile("jquery.js");
            moveAssetsFile("macarons.js");
        }
    }

    /**
     * 移动assets下文件到/data/data下
     * @param fileName
     */
    private void moveAssetsFile(String fileName){
        File file = new File(getFilesDir()+"/js/"+fileName);
        try {
            InputStream in = getAssets().open("js/"+fileName);
            FileOutputStream out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1){
                out.write(buf, 0, length);
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
