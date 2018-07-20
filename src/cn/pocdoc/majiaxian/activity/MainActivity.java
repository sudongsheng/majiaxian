package cn.pocdoc.majiaxian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseTitleActivity;
import cn.pocdoc.majiaxian.adapter.MainAdapter;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.helper.MobClickAgentHelper;
import cn.pocdoc.majiaxian.model.*;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.update.UmengUpdateAgent;

import java.io.*;

public class MainActivity extends BaseTitleActivity {

    @ViewInject(R.id.mainLV)
    private ListView listView;

    private MainAdapter adapter;
    CourseListInfo courseListInfo = null;
    private MainApplication application;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleBackgroundResource(R.color.title_bg);
        setNavBtn(R.drawable.home_activity, R.drawable.home_badge);
        setTitle(R.string.app_name);
        getTitleView().setTextColor(getResources().getColor(R.color.title));

        application = MainApplication.getInstance();
        // TODO insert more items into adapter
        adapter = new MainAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseInfo courseInfo = (CourseInfo) adapter.getItem(i);
                application.currentCourseId = courseInfo.getId();
                if (courseInfo.isUnlocked()) {
                    //startActivity(ItemOverviewActivity.class);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ItemOverviewActivity.class);
                    //intent.putExtra("courseInfo", courseInfo);
                    application.currentCourseId = courseInfo.getId();
                    intent.putExtra("courseId", courseInfo.getId());
                    startActivity(intent);
                    Config.soundPlay(Config.soundResource[0]);
                } else {
                    checkCourseStatusAndStartActivity(courseInfo);
                }
                MobClickAgentHelper.onEvent("main_course_item_click");
            }
        });

        // JUST FOR TEST
        //checkCourseStatusAndStartActivity(3);
        getOnLineCourseList();

        //开启友盟自动更新
        UmengUpdateAgent.update(this);
    }

    @Override
    protected void HandleTitleBarEvent(int buttonId, View v) {
        if (buttonId == BaseTitleActivity.LEFT_BTN) {
            if(MainApplication.getInstance().isLogin()){
                startActivity(StatisticsActivity.class);
            }else{
                startActivity(LoginActivity.class);
            }
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            MobClickAgentHelper.onEvent("main_statistics_click");
        } else if (buttonId == BaseTitleActivity.RIGHT_BTN) {
            if(MainApplication.getInstance().isLogin()){
                WebBrowserActivity.initUrl(
                        String.format(Config.BADGE_DETAIL, PreferencesUtils.getString(this, "uid"))
                );
            }else{
                startActivity(LoginActivity.class);
            }
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            MobClickAgentHelper.onEvent("main_achievement_click");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    /**
     * try get the latest course list from the server
     */
    private void getOnLineCourseList(){
        String uid = PreferencesUtils.getString(this, "uid");
        // TODO delete the following line
        //uid = "329673";
//        if (uid.equals("")){
//            return;
//        }
        String url = String.format(Config.COURSE_LIST_URL, uid);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Gson gson = new Gson();
                        CourseListInfo courseListInfo = gson.fromJson(responseInfo.result, CourseListInfo.class);
                        if (courseListInfo.getCode() == 0){
                            saveCourseList2Cache(responseInfo.result);
                            updateCourseList(courseListInfo);
                        }else {
                            courseListInfo = readCachedCourseList();
                            updateCourseList(courseListInfo);

                        }
                    }

                    @Override
                    public void onStart() {
                    }

                    // when the network is not available
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CourseListInfo courseListInfo = readCachedCourseList();
                        updateCourseList(courseListInfo);

                    }
                });
    }

    /**
     * update the course in the ui
     */
    private void updateCourseList(CourseListInfo courseListInfo){
        if (courseListInfo == null){
            return;
        }

        // TODO the new courses do not get the detail
        for (int i = application.getCourseListInfo().getCourses().size(); i > 3; i --){
            application.getCourseListInfo().getCourses().remove(i - 1);
        }
        application.getCourseListInfo().addCourses(courseListInfo.getCourses());
        adapter.notifyDataSetChanged();

    }

    private void checkCourseStatusAndStartActivity(final CourseInfo courseInfo){

        String uid = PreferencesUtils.getString(this, "uid");
        // TODO start LoginActivity
        if (uid.equals("")) {
           // do nothing
            startActivity(LoginActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return;
        }

        //uid = "329676";
        String url = String.format(Config.COURSE_STATUS_URL, courseInfo.getId(), uid);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Gson gson = new Gson();
                        LockInfo lockInfo = gson.fromJson(responseInfo.result, LockInfo.class);
                        if (lockInfo.getCode() == 0){
                            if (lockInfo.isUnlocked()){
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, ItemOverviewActivity.class);
                                //intent.putExtra("courseInfo", courseInfo);
                                intent.putExtra("courseId", courseInfo.getId());
                                startActivity(intent);
                            }else {
                                showToast(courseInfo.getDesc());
                            }
                        }else {
                            showToast(courseInfo.getDesc());
                        }

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // Toast
                        showToast("onFailure");
                    }
                });
    }

    // TODO operate the file in a background task?
    private void saveCourseList2Cache(String courseListContent){
        save2Cache(Config.COURSE_LIST_FILE_NAME, courseListContent);
    }

    private void save2Cache(String fileName, String content){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private CourseListInfo readCachedCourseList() {
        Gson gson = new Gson();
        CourseListInfo courseListInfo = null;
        try {
            courseListInfo = gson.fromJson(new FileReader(getFilesDir().getAbsolutePath() + "/" + Config.COURSE_LIST_FILE_NAME), CourseListInfo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return courseListInfo;
    }

}
