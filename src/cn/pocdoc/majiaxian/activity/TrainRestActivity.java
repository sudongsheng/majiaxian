package cn.pocdoc.majiaxian.activity;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.helper.CountTimerHelper;
import cn.pocdoc.majiaxian.helper.MobClickAgentHelper;
import cn.pocdoc.majiaxian.listener.OnCustomDialogListener;
import cn.pocdoc.majiaxian.model.*;
import cn.pocdoc.majiaxian.service.RecordService;
import cn.pocdoc.majiaxian.utils.*;
import cn.pocdoc.majiaxian.view.VideoPlayer;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by pengwei on 15/1/19.
 * 训练休息页
 */
public class TrainRestActivity extends BaseActivity {

    @ViewInject(R.id.animImg)
    private ImageView animImg;
    @ViewInject(R.id.video_preview)
    private VideoPlayer player;
    @ViewInject(R.id.train_rest_title)
    private TextView title;
    @ViewInject(R.id.train_rest_time)
    private TextView time;
    @ViewInject(R.id.train_rest_time2)
    private TextView time2;
    @ViewInject(R.id.train_rest_next)
    private TextView next;
    @ViewInject(R.id.activity_train_rest)
    private RelativeLayout rl;

    private int restTime;
    private int currentTime;
    private CountTimerHelper timerHelper;
    private boolean isPause = false;
    private boolean doTask = false;
    private boolean isAnimEnd = false;
    CustomDialog dialog;
    private SoundPlayer soundPlayer;

    private int courseId;
    private CourseInfo courseInfo;
    private CourseDetailInfo courseDetailInfo;
    private List<ActionInfo> actionInfos;

    private MainApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_rest);

        application = MainApplication.getInstance();
        courseId = MainApplication.getInstance().currentCourseId;
        courseInfo = MainApplication.getInstance().getCourseListInfo().getCourse(courseId);
        courseDetailInfo = courseInfo.getCourseDetailInfo();
        actionInfos = courseDetailInfo.getActions();

        next.setTypeface(Config.tf);
        if (application.currentActionIndex == 0) {
            ContentCache.recordId = RecordsDB.getInstance(getContext()).insertRecord(application.currentCourseId,
                    TimeUtil.getCurrentTimestamp(), 0, PreferencesUtils.getString(this, "uid", UserInfo.NO_LOGIN), '0');
            restTime = 5;
            title.setText("准备");
            title.setTypeface(Config.tf);
        } else {
            //int groupRestTime = Config.ITEM_INFOS[ContentCache.itemId].getGroups()[ContentCache.actioncourseId].restTime;
            int groupRestTime = actionInfos.get(application.currentActionIndex).getDuration();
//            if(groupRestTime == -1){
//                restTime = Config.ITEM_INFOS[ContentCache.itemId].getRestTime();courseId         }else{
//                restTime = groupRestTime;
//            }

            restTime = groupRestTime;
            title.setText("休息");
            title.setTypeface(Config.tf);
            /*time.setText(Config.ITEM_INFOS[ContentCache.itemId].getRestTime() + "");
          courseId2.setText(Config.ITEM_INFOS[ContentCache.itemId].getRestTime() + "");*/
            time.setText(restTime + "");
            time2.setText(restTime + "");
        }
        currentTime = restTime;
        startAnim();
        soundPlayer = new SoundPlayer();
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backClick();
            }
        });
    }

    /**
     * 点击home键之后执行的操作
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("test", "onResume:" + doTask);
        if (!isPause && doTask) {
            soundPlayer.rePlay();
        }
        doTask();
    }

    /**
     * 动画结束后或点击暂停之后继续或点击home键之后执行的操作
     */
    private void doTask() {
        LogUtil.d("test", "doTask:" + "  isAnimEnd-->" + isAnimEnd + "  isPause-->" + isPause + "  doTask-->" + doTask);
        if (isAnimEnd && !isPause && doTask) {
            timerHelper = new CountTimerHelper(currentTime * 1000, 1000, new CountingTimerListener());
            timerHelper.TimeStart();

            //player.play(Config.ITEM_INFOS[ContentCache.itemId].groups[ContentCache.actionId].courseIddeoId);
            if (courseInfo instanceof LocalCourseInfo){
                // TODO when the action is rest
                int videoId = ((LocalActionInfo) (actionInfos.get(application.currentActionIndex))).getActionVideoId();
                LogUtil.e("jyj", "" + videoId);
                if (videoId == 0){
                    videoId = ((LocalActionInfo) (actionInfos.get(application.currentActionIndex + 1))).getActionVideoId();
                }
                LogUtil.e("jyj", "" + videoId);
                player.play(videoId);
            }else {
                String url = actionInfos.get(application.currentActionIndex).getVideoUrl();
                if (url.equals("")){
                    url = actionInfos.get(application.currentActionIndex + 1).getVideoUrl();
                }
                LogUtil.e("jyj", url);
                String name = url.substring(url.lastIndexOf("/") + 1);
                player.play(name);
            }

            player.setOnControlListener(new VideoPlayer.onControlListener() {
                @Override
                public void onCompletionListener(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }

                @Override
                public void onErrorListener(MediaPlayer mediaPlayer) {
                    new AlertDialog.Builder(TrainRestActivity.this).setTitle("温馨提示").setMessage("遇到一些问题无法播放视频，建议您清理一下自己手机的内存再回来！").setPositiveButton("确定", null).show();
                }
            });
        }
    }

    /**
     * 进入页面动画
     */
    private void startAnim() {
        title.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
        /**
         * 圆圈动画
         */
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.train_rest_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        if (rotateAnim != null)
            animImg.startAnimation(rotateAnim);

        /**
         * 时间动画
         */
        Animation time2TranslateAnim = AnimationUtils.loadAnimation(this, R.anim.train_rest_time2_anim);
        time2TranslateAnim.setInterpolator(new AccelerateInterpolator());
        time2.setAnimation(time2TranslateAnim);


        Animation playerAlphaAnim = AnimationUtils.loadAnimation(this, R.anim.train_rest_player_anim);
        player.setAnimation(playerAlphaAnim);
        time2TranslateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtil.d("test", "animation end");
                time2.setVisibility(View.INVISIBLE);
                time.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                /**
                 * 时间动画回弹
                 */
                Animation timeTranslateAnim = AnimationUtils.loadAnimation(TrainRestActivity.this, R.anim.train_rest_time_anim);
                timeTranslateAnim.setInterpolator(new LinearInterpolator());
                time.setAnimation(timeTranslateAnim);

                /**
                 * 标题栏动画
                 */
                Animation titleTranslateAnim = AnimationUtils.loadAnimation(TrainRestActivity.this, R.anim.train_rest_title_anim);
                titleTranslateAnim.setInterpolator(new LinearInterpolator());
                title.startAnimation(titleTranslateAnim);

                timeTranslateAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isAnimEnd = true;
                        currentTime += 1;
                        doTask = true;
                        doTask();
                    }
                });
            }
        });
    }

    /**
     * 点击home键之后执行操作
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("test", "onStop");
        if (soundPlayer.isPlaying())
            soundPlayer.pause();
        if (timerHelper != null)
            timerHelper.TimeCacel();
        doTask = true;
    }

    /**
     * 进入train页面之前,销毁当前activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlayer.stop();
        player.stopPlayback();
    }

    /**
     * 重写back键的监听事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            backClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 暂停,显示dialog
     */
    private void showDialog() {
        soundPlayer.pause();
        Config.soundPlay(Config.soundResource[5]);
        dialog = Pop.popDialog(this, R.layout.view_pause_dialog, R.style.translucentDialog);
        TextView end = dialog.findView(R.id.dialog_end);
        TextView con = dialog.findView(R.id.dialog_continue);
        end.setText(Html.fromHtml("<u>终止</u>"));
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTrain();
            }
        });
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPause = false;
                LogUtil.d("test", "continue: doTask-->" + (timerHelper == null));
                if (timerHelper != null) {
                    soundPlayer.rePlay();
                    timerHelper = new CountTimerHelper(currentTime * 1000, 1000, new CountingTimerListener());
                    time.setText(currentTime - 1 + "");
                    timerHelper.TimeStart();
                    player.reStart();
                } else {
                    doTask = true;
                    currentTime += 1;
                    doTask();
                }
                dialog.dismiss();

            }
        });

        /**
         * 点击Back之后再点击Back退出activity
         */
        dialog.setOnCustomDialogListener(new OnCustomDialogListener() {
            @Override
            public void onFinish() {
                super.onFinish();
                if (isPause) {
                    endTrain();
                }
            }
        });
    }

    /**
     * 倒计时的回调
     */
    class CountingTimerListener implements CountTimerHelper.MyCountDownTimerListener {
        @Override
        public void onTick(int count) {
            LogUtil.d("test", "rest_time:" + count);
            if (count == 5) {
                soundPlayer.play();
            }
            time.setText(count + "");
            currentTime = count + 1;
        }

        @Override
        public void onFinish() {
            Animation titleTranslateAnim = AnimationUtils.loadAnimation(TrainRestActivity.this, R.anim.train_rest_title_anim_out);
            titleTranslateAnim.setInterpolator(new LinearInterpolator());
            title.startAnimation(titleTranslateAnim);
            if (application.currentActionIndex == 0) {
                startActivity(TrainActivity.class);
            }
            Config.soundPlay(Config.soundResource[2]);
            TrainRestActivity.this.finish();
//            overridePendingTransition(R.anim.train_rest_in,R.anim.train_rest_out);
            if (application.currentActionIndex != 0){
                application.currentActionIndex ++;
            }
        }
    }

    private void backClick() {
        if (!isPause) {
            LogUtil.d("test", "Back click");
            player.pause();
            soundPlayer.pause();
            if (timerHelper != null)
                timerHelper.TimeCacel();
            showDialog();
            isPause = true;
        }
    }

    /**
     * 终止训练
     */
    private void endTrain() {
        application.currentActionIndex = 0;
        startActivity(ItemOverviewActivity.class);
        //插入未完成纪录
        TrainActivity.TrainPause(getContext());

        MobClickAgentHelper.onExitEvent();
        RecordService.startRecordService();
    }
}