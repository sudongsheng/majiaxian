package cn.pocdoc.majiaxian.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.ActionInfo;
import cn.pocdoc.majiaxian.model.LocalActionInfo;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.view.VideoPlayer;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class ItemPreviewActivity extends BaseActivity {


    @ViewInject(R.id.preview_btn_close)
    private TextView close;
    @ViewInject(R.id.video_preview)
    private VideoPlayer player;
    @ViewInject(R.id.title_preview)
    private TextView title;
    @ViewInject(R.id.count_preview)
    private TextView count;

    private String videoUrl;
    private FileInputStream fis;

    private ActionInfo actionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_preview);
        int courseId = MainApplication.getInstance().currentCourseId;
        int actionIndex = MainApplication.getInstance().currentActionIndex;
        actionInfo = MainApplication.getInstance().getCourseListInfo().getCourse(courseId).getCourseDetailInfo().getActions().get(actionIndex);
        title.setText(actionInfo.getActionName());
        int trainTimes = actionInfo.getDuration();
        if(trainTimes == 0)
            count.setText(getString(R.string.no_count_tip));
        else
            count.setText(trainTimes + "次");
        if (actionInfo instanceof LocalActionInfo){
            player.play(((LocalActionInfo)actionInfo).getActionVideoId());
        }else{
            String videoUrl = actionInfo.getVideoUrl();
            String videoName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
            player.play(videoName);
        }
        LogUtils.e("****" + videoUrl);
        player.setOnControlListener(new VideoPlayer.onControlListener() {
            @Override
            public void onCompletionListener(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }

            @Override
            public void onErrorListener(MediaPlayer mediaPlayer) {
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.relase();
                ItemPreviewActivity.this.finish();
            }
        });

        FontManager.changeFonts((ViewGroup) findViewById(R.id.activity_item_preview));

        findViewById(R.id.activity_item_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemPreviewActivity.this.finish();
            }
        });
    }
}
