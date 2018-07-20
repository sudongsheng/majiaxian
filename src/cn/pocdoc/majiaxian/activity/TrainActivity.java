package cn.pocdoc.majiaxian.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.base.BaseActivity;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.helper.BadgeHelper;
import cn.pocdoc.majiaxian.helper.CountTimerHelper;
import cn.pocdoc.majiaxian.helper.MobClickAgentHelper;
import cn.pocdoc.majiaxian.helper.NotificationHelper;
import cn.pocdoc.majiaxian.listener.OnCustomDialogListener;
import cn.pocdoc.majiaxian.model.*;
import cn.pocdoc.majiaxian.service.RecordService;
import cn.pocdoc.majiaxian.utils.*;
import cn.pocdoc.majiaxian.view.TrainProgressBar;
import cn.pocdoc.majiaxian.view.VideoPlayer;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 15/1/19.
 * 训练页面
 */
public class TrainActivity extends BaseActivity {
    private static final String TAG = "TrainActivity";

    @ViewInject(R.id.videoPlayer)
    private VideoPlayer player;
    @ViewInject(R.id.train_time)
    private TextView time;
    @ViewInject(R.id.train_time2)
    private TextView time2;
    @ViewInject(R.id.train_progressbar)
    private TrainProgressBar progressBar;
    @ViewInject(R.id.train_video_ll)
    private LinearLayout ll;
    @ViewInject(R.id.sound_btn)
    private CheckBox sound_btn;

    private CountTimerHelper timerHelper;
    private int actionCount;
    private int maxTime;
    private int currentTime;
    private boolean isPause = false;
    private boolean isAnimEnd = false;
    private int tempActionId = -1;
    private SoundPlayer soundPlayer;
    CustomDialog dialog;

    private long startTime;
    private long pauseTime;

    private int courseId;
    private CourseInfo courseInfo;
    private CourseDetailInfo courseDetailInfo;
    private List<ActionInfo> actionInfos;

    private MainApplication application;
    private int currentPrograss = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //禁止休眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_train);

        application = MainApplication.getInstance();
        courseId = application.currentCourseId;
        application.currentActionIndex = 0;
        courseInfo = application.getCourseListInfo().getCourse(courseId);
        courseDetailInfo = courseInfo.getCourseDetailInfo();
        actionInfos = courseDetailInfo.getActions();

        //actionCount = Config.ITEM_INFOS[ContentCache.courseId].getGroupNum();
        actionCount = actionInfos.size();
        // TODO
        //maxTime = Config.ITEM_INFOS[ContentCache.courseId].groups[ContentCache.actionId].getTime();
        maxTime = actionInfos.get(application.currentActionIndex).getDuration();
        currentTime = maxTime;
        getContext().getResources().getDrawable(R.drawable.back);
        soundPlayer = new SoundPlayer();
        sound_btn.setChecked(PreferencesUtils.getBoolean(TrainActivity.this, "sound", true));
        LogUtil.d("sound", "sound-->" + soundPlayer.getVolume());

        sound_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean b) {
                if (btn.isChecked()) {
                    LogUtil.d("sound", "put sound-->true");
                    soundPlayer.setVolumn(true);
                    PreferencesUtils.putBoolean(TrainActivity.this, "sound", true);
                } else {
                    LogUtil.d("sound", "put sound-->false");
                    soundPlayer.setVolumn(false);
                    PreferencesUtils.putBoolean(TrainActivity.this, "sound", false);
                }
            }
        });
    }


    /**
     * 进入TrainRest之后回来,激活当前activity
     */
    @Override
    protected void onResume() {
        startTime = TimeUtil.getCurrentTimestamp();
        super.onResume();
        if (soundPlayer.getVolume() > 0) {
            sound_btn.setChecked(true);
            PreferencesUtils.putBoolean(TrainActivity.this, "sound", true);
        }
        if (!isPause) {
            if (tempActionId != application.currentActionIndex) {
                LogUtil.d(TAG, "onResume-->doNextTrain");
                //maxTime = Config.ITEM_INFOS[ContentCache.courseId].groups[ContentCache.actionId].getTime();
                maxTime = actionInfos.get(application.currentActionIndex).getDuration();
                currentTime = maxTime;
                tempActionId++;
                time.setText(maxTime + "");
                time2.setText(maxTime + "");
                ll.setBackgroundColor(getResources().getColor(R.color.white));
                startAnim();
            } else if (isAnimEnd) {
                LogUtil.d(TAG, "onResume-->doThisTrain");
                soundPlayer.rePlay();
                timerHelper = new CountTimerHelper(currentTime * 1000, 1000, new CountingTimerListener());
                timerHelper.TimeStart();
            }

            //player.play(Config.ITEM_INFOS[ContentCache.courseId].groups[ContentCache.actionId].groupVideoId);
            if (courseInfo instanceof LocalCourseInfo){
                int videoId = ((LocalActionInfo) (actionInfos.get(application.currentActionIndex))).getActionVideoId();
                LogUtil.e("Train", "" + videoId);
                //player.play(((LocalActionInfo) (actionInfos.get(application.currentActionId))).getActionVideoId());
                player.play(videoId);
            }else {
                String url = actionInfos.get(application.currentActionIndex).getVideoUrl();
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
                    new AlertDialog.Builder(TrainActivity.this).setTitle("温馨提示").setMessage("遇到一些问题无法播放视频，建议您清理一下自己手机的内存再回来！").setPositiveButton("确定", null).show();
                }
            });
        }
    }

    /**
     * 倒计时的回调
     */
    class CountingTimerListener implements CountTimerHelper.MyCountDownTimerListener {
        @Override
        public void onTick(int count) {
            LogUtil.d(TAG, "tickTime:" + count);
            if (count == 5) {
                soundPlayer.play();
            }
            time.setText(count + "");
            currentTime = count + 1;
            progressBar.setProgress(currentPrograss, maxTime - count);
        }

        @Override
        public void onFinish() {
            finishTrain();
        }
    }

    /**
     * 暂停按钮的监听
     */
    @OnClick(R.id.pause_btn)
    public void pauseListener(View v) {
        if (!isPause) {
            player.pause();
            timerHelper.TimeCacel();
            isPause = true;
            showDialog();
        } else {
            timerHelper = new CountTimerHelper(currentTime * 1000, 1000, new CountingTimerListener());
            time.setText(currentTime - 1 + "");
            timerHelper.TimeStart();
            player.reStart();
            isPause = false;
        }
    }

    /**
     * 点击home键之后执行操作
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (timerHelper != null)
            timerHelper.TimeCacel();
        soundPlayer.pause();
    }

    /**
     * 暂停,显示dialog
     */
    private void showDialog() {
        pauseTime = TimeUtil.getCurrentTimestamp();
        Config.soundPlay(Config.soundResource[5]);
        soundPlayer.pause();
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
                soundPlayer.rePlay();
                timerHelper = new CountTimerHelper(currentTime * 1000, 1000, new CountingTimerListener());
                LogUtil.d(TAG, "time:" + (currentTime - 1));
                time.setText(currentTime - 1 + "");
                timerHelper.TimeStart();
                player.reStart();
                dialog.dismiss();

                //存储暂停时间
                RecordsDB.getInstance(getContext()).insertRecordDetail(ContentCache.recordId,
                        pauseTime, TimeUtil.getCurrentTimestamp(), -1);
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

        /**
         * 重写字体
         */
        FontManager.changeFonts((ViewGroup) dialog.findView(R.id.view_pause_dialog));
    }

    /**
     * 重写back键的监听事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (!isPause) {
                player.pause();
                if (timerHelper != null)
                    timerHelper.TimeCacel();
                isPause = true;
                showDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 进入页面开始动画
     */
    private void startAnim() {
        time2.setVisibility(View.VISIBLE);
        Animation time2TranslateAnim = AnimationUtils.loadAnimation(this, R.anim.train_time2_anim);
        time2TranslateAnim.setInterpolator(new LinearInterpolator());
        time2.setAnimation(time2TranslateAnim);
        time.setVisibility(View.INVISIBLE);
        time2TranslateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimEnd = true;
                time2.setVisibility(View.INVISIBLE);
                time.setVisibility(View.VISIBLE);
                timerHelper = new CountTimerHelper((currentTime + 1) * 1000, 1000, new CountingTimerListener());
                timerHelper.TimeStart();

                ll.setBackgroundColor(getResources().getColor(R.color.transparent));
//                Animation timeTranslateAnim = AnimationUtils.loadAnimation(TrainActivity.this, R.anim.train_time_anim);
//                timeTranslateAnim.setInterpolator(new LinearInterpolator());
//                time.setAnimation(timeTranslateAnim);
            }
        });
    }

    /**
     * 完成当前训练
     */
    private void finishTrain() {
        progressBar.setProgress(currentPrograss ++, maxTime);


        //存储每组运动数据
        RecordsDB.getInstance(this).insertRecordDetail(ContentCache.recordId,
                startTime, TimeUtil.getCurrentTimestamp(), actionInfos.get(application.currentActionIndex).getActionId());
                //Config.ITEM_INFOS[ContentCachcourseIdId].getGroups()[ContentCache.actionId].groupId);

        /**
         * 完成全部训练
         */
        if (++application.currentActionIndex >= actionCount) {
            TrainActivity.TrainFinish(getContext());

            /*int badgeId = 0;
            int record_id = TrainActivity.TrainFinish(getContext());
            List<Integer> badges = new ArrayList<Integer>();
            for (BadgeInfo badgeInfo : Config.BADGE_INFOS) {
                int id = badgeInfo.getId();
                if (BadgeHelper.checkValid(TrainActivity.this, id, application.currentCourseId)) {
                    badges.add(id);
                    badgeId = id;
                }
            }
            //未获得徽章
            if (badges.size() == 0) {
                startActivity(StatisticsActivity.class);
            } else {
                for (int i : badges) {
                    RecordsDB.getInstance(getContext()).insertBadge(i, PreferencesUtils.getString(this, "uid", UserInfo.NO_LOGIN), record_id);
                }
                //是否同步勋章数据
                ContentCache.getBadge = true;

                Intent intent = new Intent(TrainActivity.this, BadgeActivity.class);
                intent.putExtra("badgeId", badgeId);
                TrainActivity.this.startActivity(intent);
            }*/

            Config.soundPlay(Config.soundResource[4]);
            application.currentActionIndex = 0;

            RecordService.startRecordService();
            startActivity(StatisticsActivity.class);
            finish();
        } else {
            Config.soundPlay(Config.soundResource[3]);
            startActivity(TrainRestActivity.class);
//                overridePendingTransition(R.anim.train_rest_in, R.anim.train_rest_out);
        }
    }

    /**
     * 终止训练
     */
    private void endTrain() {
        application.currentActionIndex = 0;
        TrainActivity.this.finish();
        //插入未完成纪录
        TrainActivity.TrainPause(getContext());
        MobClickAgentHelper.onExitEvent();
        RecordService.startRecordService();
    }


    /**
     * 训练完成
     */
    public static int TrainFinish(Context context) {
        RecordsDB.getInstance(context).updateEndTime(ContentCache.recordId, '1');
        return ContentCache.recordId;
    }

    /**
     * 训练中断
     *
     * @param context
     */
    public static void TrainPause(Context context) {
        RecordsDB.getInstance(context).updateEndTime(ContentCache.recordId, '2');
    }

}