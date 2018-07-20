package cn.pocdoc.majiaxian.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseTitleActivity;
import cn.pocdoc.majiaxian.adapter.ItemOverviewAdapter;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.*;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.*;
import java.util.List;

/**
 * Created by pengwei on 15/1/16.
 */
public class ItemOverviewActivity extends BaseTitleActivity {

    @ViewInject(R.id.subTitle)
    private TextView subTitle;

    @ViewInject(R.id.listView)
    private ListView lv;

    private ItemOverviewAdapter adapter;

    @ViewInject(R.id.start_plan)
    private TextView startBtn;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    private HttpUtils httpUtils = new HttpUtils();
    private String baseUrl;
    private boolean needDown = true;

    private int courseId;
    private CourseInfo courseInfo;
    private List<ActionInfo> actionInfos;
    private MainApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_overview);

        application = MainApplication.getInstance();
        courseId = application.currentCourseId;

        courseInfo = application.getCourseListInfo().getCourse(courseId);
        setTitle(courseInfo.getName());
        setTitleBackgroundResource(R.color.base);
        getTitleView().setTextColor(Color.WHITE);
        setNavBtn(R.drawable.back, 0);

        baseUrl = getFilesDir().getPath() + "/videos/%s";

        subTitle.setText(courseInfo.getSubdesc());

        courseInfo = application.getCourseListInfo().getCourse(courseId);

        if (courseInfo.getCourseDetailInfo() != null){
            actionInfos = courseInfo.getCourseDetailInfo().getActions();
            adapter = new ItemOverviewAdapter(getContext(), courseInfo.getCourseDetailInfo().getActions());
            lv.setAdapter(adapter);

            checkAndUpdateUI();
        }else {
            //uid = "329676"
            String uid = PreferencesUtils.getString(this, "uid");
            //uid = "329676";
            getOnlineActionList(uid, courseId);

        }

        lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ActionInfo info = actionInfos.get(i);
                if (info.getActionId() != Config.ACTION_ID_REST){
                    application.currentActionIndex = i;
                    if (info instanceof LocalActionInfo){
                        Intent intent = new Intent(ItemOverviewActivity.this, ItemPreviewActivity.class);
                        startActivity(intent);
                    }else {
                        if(existVideo(info.getVideoUrl())){
                            Intent intent = new Intent(ItemOverviewActivity.this, ItemPreviewActivity.class);
                            startActivity(intent);
                        }else{
                            showToast(getString(R.string.need_load));
                        }

                    }
                }
            }
        });
        application.currentActionIndex = 0;
        FontManager.changeFonts((ViewGroup) findViewById(R.id.activity_item_overview));
    }

    @OnClick(R.id.start_plan)
    public void startTrain(View v) {
        application.currentActionIndex = 0;
        if(!(courseInfo instanceof  LocalCourseInfo) && needDown){
            showToast(getString(R.string.down_tip));
            //startBtn.setClickable(false);
            //startBtn.setText(R.string.now_load);
            startBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            for(ActionInfo info: actionInfos){
                if(!existVideo(info.getVideoUrl())){
                    down(info.getVideoUrl());
                }
            }
        }else{
            startActivity(TrainRestActivity.class);
        }
    }

    @Override
    protected void HandleTitleBarEvent(int buttonId, View v) {
        if (buttonId == BaseTitleActivity.LEFT_BTN)
            ItemOverviewActivity.this.finish();
    }



    /**
     * 下载
     *
     * @param videoUrl
     */
    private void down(String videoUrl) {
        String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
        if (!existVideo(videoUrl)) {
            httpUtils.download(videoUrl, String.format(baseUrl, fileName), false, false, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    adapter.notifyDataSetChanged();
                    checkAndUpdateUI();

                }
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
//                    if(total == current){
//                        adapter.notifyDataSetChanged();
//                        if(checkVideoDownloadFinish(actionInfos)){
//                            startBtn.setText(R.string.start_exercise);
//                            startBtn.setClickable(true);
//                        }
//                    }
                }
                @Override
                public void onFailure(HttpException e, String s) {}
            });
        }
    }


    /**
     * 获取文件的本地路径
     * @param videoUrl
     * @return
     */
    public String getLocalPath(String videoUrl){
        String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
        return new File(String.format(baseUrl, fileName)).getPath();
    }
    /**
     * 视频是否存在
     * @param videoUrl
     * @return
     */
    public boolean existVideo(String videoUrl){
        return existVideo(this, videoUrl);
    }
    public static boolean existVideo(Context context, String videoUrl){
        String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
        String baseUrl = context.getFilesDir().getPath() + "/videos/%s";
        return new File(String.format(baseUrl, fileName)).exists();
    }

    private void checkAndUpdateUI(){
        if(checkVideoDownloadFinish(actionInfos)){
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setText(R.string.start_exercise);
            progressBar.setVisibility(View.GONE);
            needDown = false;

        }else{
            startBtn.setText(R.string.start_load);
            needDown = true;
        }
        startBtn.setClickable(true);
    }
    /**
     * 判断视频是否存在
     * @param list
     * @return
     */
    private boolean checkVideoDownloadFinish(List<ActionInfo> list){
        if (courseInfo instanceof LocalCourseInfo){
            return true;
        }
        for(ActionInfo info : list){
            if(!existVideo(info.getVideoUrl())) return  false;
        }
        return  true;
    }

    // TODO uid
    private void getOnlineActionList(String uid, final int courseId){
        HttpUtils http = new HttpUtils();
        String requestUrl = String.format(Config.COURSE_DETAIL_URL, courseId, uid);
        LogUtils.e("*****" + requestUrl);
        http.send(HttpRequest.HttpMethod.GET, requestUrl, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.e("***success");
                CourseDetailInfo detail = new Gson().fromJson(responseInfo.result, CourseDetailInfo.class);
                saveCourseDetail2Cache(detail.getCourseId(), responseInfo.result);
                if (detail.getCode() == 0) {
                    courseInfo.setCourseDetailInfo(detail);
                    actionInfos = detail.getActions();
                    adapter = new ItemOverviewAdapter(getContext(), actionInfos);
                    lv.setAdapter(adapter);
                    checkAndUpdateUI();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                // TODO read the cache
                LogUtils.e("***failure");
                CourseDetailInfo courseDetailInfo = readCachedCourseDetail(courseId);
                courseInfo.setCourseDetailInfo(courseDetailInfo);
                actionInfos = courseDetailInfo.getActions();
                adapter = new ItemOverviewAdapter(getContext(), actionInfos);
                lv.setAdapter(adapter);

                checkAndUpdateUI();
            }
        });

    }

    private void saveCourseDetail2Cache(int courseId, String courseDetailContent){
        String fileName = String.format(Config.COURSE_DETAIL_NAME, courseId);
        save2Cache(fileName, courseDetailContent);
    }

    // TODO refactor
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

    private CourseDetailInfo readCachedCourseDetail(int courseId){
        String fileName = String.format(Config.COURSE_DETAIL_NAME, courseId);
        Gson gson = new Gson();
        CourseDetailInfo courseDetailInfo = null;
        try {
            courseDetailInfo = gson.fromJson(new FileReader(getFilesDir().getAbsolutePath() + "/" + fileName), CourseDetailInfo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return courseDetailInfo;
    }
}