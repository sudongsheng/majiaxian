package cn.pocdoc.majiaxian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.ActionInfo;
import cn.pocdoc.majiaxian.model.CourseDetailInfo;
import cn.pocdoc.majiaxian.model.CourseInfo;

import java.util.List;

/**
 * Created by pengwei on 15/1/19.
 */
public class TrainProgressBar extends LinearLayout {
    private Context context;
    private ProgressBar[] pbs;
    private int groupNum;
    private int[] maxTimes;

    public TrainProgressBar(Context context) {
        super(context);
        this.init(context);
    }

    public TrainProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public TrainProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setOrientation(LinearLayout.HORIZONTAL);

        //groupNum = Config.ITEM_INFOS[ContentCache.courseId].getGroupNum();
        CourseInfo courseInfo = MainApplication.getInstance().getCourseListInfo().getCourse(MainApplication.getInstance().currentCourseId);
        CourseDetailInfo courseDetailInfo = courseInfo.getCourseDetailInfo();
        List<ActionInfo> actionInfos = courseDetailInfo.getActions();
        // TODO calculate the actual value
        //groupNum = actionInfos.size() - actionInfos.size() / 2;

        groupNum = 0;
        for (ActionInfo actionInfo : actionInfos){
            if (actionInfo.getActionId() != Config.ACTION_ID_REST){
                groupNum ++;
            }
        }
        maxTimes = new int[groupNum];

        int o = 0;
        for (ActionInfo actionInfo : actionInfos){
            if (actionInfo.getActionId() != Config.ACTION_ID_REST){
                maxTimes[o] = actionInfo.getDuration();
                o ++;
            }
        }

//        for (int i = 0; i < groupNum; i++)
//            maxTimes[i] = actionInfos.get(i).getDuration();
        pbs = new ProgressBar[maxTimes.length];

        int sum = 0;
        for (int k : maxTimes) {
            sum += k;
        }
        int margin = 2;
        for (int i = 0; i < maxTimes.length; i++) {
            pbs[i] = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            pbs[i].setProgressDrawable(getResources().getDrawable(R.drawable.train_progress_selector));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            );
            pbs[i].setMax(maxTimes[i]);
            if (i == 0) {
                param.setMargins(0, 0, margin, 0);
            } else if (i == maxTimes.length - 1) {
                param.setMargins(margin, 0, 0, 0);
            } else {
                param.setMargins(margin, 0, margin, 0);
            }
            param.weight = 1;
            addView(pbs[i], param);
        }
    }

    /**
     * 获得进度条
     *
     * @return
     */
    public ProgressBar[] getProgressBars() {
        return pbs;
    }

    /**
     * 设置进度条
     *
     * @param index
     * @param progress
     */
    public void setProgress(int index, int progress) {
        if (index < pbs.length) {
            for (int i = 0; i < index; i++) {
                pbs[i].setProgress(maxTimes[i]);
            }
            pbs[index].setProgress(progress);
        }
    }
}
