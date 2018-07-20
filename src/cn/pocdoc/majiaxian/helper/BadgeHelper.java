package cn.pocdoc.majiaxian.helper;

import android.content.Context;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.utils.TimeUtil;

/**
 * Created by pengwei on 14/12/17.
 */
public class BadgeHelper {

    /**
     * 能不能得到徽章
     *
     * @param badgeId
     * @return
     */
    public static boolean checkValid(Context context, int badgeId, int courseId) {
        String nowDay = TimeUtil.getDay();
        int dayCount = RecordsDB.getInstance(context).getTrainCountByDay(nowDay);
        int dayFinish2TimeNum = RecordsDB.getInstance(context).dayFinishTimeCount(2);
        int badgeNum = RecordsDB.getInstance(context).getNumByBadgeId(badgeId);
        dayFinish2TimeNum = dayFinish2TimeNum == 0 ? -1 : dayFinish2TimeNum;
        switch (badgeId) {
            case 1:
                return !RecordsDB.getInstance(context).hasGetBadge(courseId);
            case 2:
                if (dayCount == 2) return true;
                break;
            case 3:
                if (dayCount == 3) return true;
                break;
            case 4:
                if (dayFinish2TimeNum % 3 == 0 && dayFinish2TimeNum / 3 > badgeNum) return true;
                break;
            case 5:
                if (dayFinish2TimeNum % 7 == 0 && dayFinish2TimeNum / 7 > badgeNum) return true;
                break;
            case 6:
                if (dayFinish2TimeNum % 30 == 0 && dayFinish2TimeNum / 30 > badgeNum) return true;
                break;
            case 7:
                if (dayFinish2TimeNum % 90 == 0 && dayFinish2TimeNum / 90 > badgeNum) return true;
                break;
            case 8:
                if (dayFinish2TimeNum % 300 == 0 && dayFinish2TimeNum / 300 > badgeNum) return true;
                break;
            default:
        }
        return false;
    }

}


