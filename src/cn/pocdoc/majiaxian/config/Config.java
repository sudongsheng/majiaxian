package cn.pocdoc.majiaxian.config;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.model.BadgeInfo;
import cn.pocdoc.majiaxian.model.LocalActionInfo;
import cn.pocdoc.majiaxian.model.LocalCourseInfo;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;

/**
 * Created by Xuri on 2015/1/16 0016.
 */
public class Config {

    public static final boolean isDebug = true;

    public static String PREFERENCE_NAME = "MAJIAXIAN";
    public static Typeface tf = Typeface.createFromAsset(MainApplication.getInstance().getAssets(), "fonts/fangzhengxidengxianjianti.ttf");
    public static SoundPool soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

    public static int ACTION_ID_REST = -2;
    public static int ACTION_ID_PAUSE = -1;

    // TODO use the right url
    public final static String APP_NAME = "majiaxian";
    public final static String COURSE_LIST_URL = Config.HOST_URL + "keepfit_api/sync/courses/" + APP_NAME + "/?uid=%s";
    public final static String COURSE_DETAIL_URL = Config.HOST_URL +"keepfit_api/sync/course/"+APP_NAME+"/%d?uid=%s"; //{courseId}?{uid}
    public final static String COURSE_STATUS_URL = Config.HOST_URL +"keepfit_api/sync/lock/"+APP_NAME+"/%d?uid=%s"; //{courseId}?{uid}
    public final static String COURSE_LIST_FILE_NAME = "courseList.json";
    public final static String COURSE_DETAIL_NAME = "courseDetail_%d.json";


    public final static String REST = "休息";

    //public LocalCourseInfo(int id, String name, String subdesc, String desc, boolean isUnlocked, int resourcesId, int lockResourceId) {
    public static final LocalCourseInfo[] LocalCourses = {
            new LocalCourseInfo(0, "塑 造 腹 肌", "2分34秒 48个动作", "", true, R.drawable.plan_1, R.drawable.plan_1_dark),
            new LocalCourseInfo(1, "平 坦 小 腹 进 阶", "5分09秒 96个动作", "得到 30 个徽章即可解锁（10天）", true, R.drawable.plan_2, R.drawable.plan_2_dark),
            new LocalCourseInfo(2, "腹 肌 轰 炸", "11分41秒 135个动作", "得到 90 个徽章即可解锁（30天）", true, R.drawable.plan_3, R.drawable.plan_3_dark),
            new LocalCourseInfo(3, "腹 部 塑 形 高 阶", "11分43秒 193个动作", "得到 160个 徽章即可解锁（50天）", false, R.drawable.plan_4, R.drawable.plan_4_dark)
            //new LocalCourseInfo(4, "虐 腹 吧！超 狠 版", "11分 150个动作", "得到 280个 徽章即可解锁（80天）", true, R.drawable.plan_5, R.drawable.plan_4_dark),
            //new LocalCourseInfo(5, "女 神 训 练 营", "12分 160个动作", "得到 400个 徽章即可解锁（120天）", true, R.drawable.plan_6, R.drawable.plan_4_dark)
    };

    public static final LocalActionInfo[][] LocalActions = {
            {
                    new LocalActionInfo(2, 8, "抬腿屈膝卷腹", R.drawable.preview_icon_taituiquxijuanfu, R.raw.taituiquxijuanfu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(3, 8, "触足屈腹", R.drawable.preview_icon_chuzuqufu, R.raw.chuzuqufu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(1, 8, "卷腹", R.drawable.preview_icon_juanfu, R.raw.juanfu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(4, 16, "俄罗斯旋转", R.drawable.preview_icon_eluosi, R.raw.eluosi),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(5, 8, "直腿收腹伸展", R.drawable.preview_icon_zhituishoufu, R.raw.zhituishoufu)
            },
            {
                    new LocalActionInfo(2, 12, "抬腿屈膝卷腹", R.drawable.preview_icon_taituiquxijuanfu, R.raw.taituiquxijuanfu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(6, 12, "V字两头起", R.drawable.preview_icon_vliangtouqi, R.raw.vliangtouqi),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(1, 12, "卷腹", R.drawable.preview_icon_juanfu, R.raw.juanfu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(4, 12, "俄罗斯旋转", R.drawable.preview_icon_eluosi, R.raw.eluosi),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(7, 12, "游式挺身", R.drawable.preview_icon_youshitingshen, R.raw.youshitingshen),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(5, 12, "直腿收腹伸展", R.drawable.preview_icon_zhituishoufu, R.raw.zhituishoufu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(3, 12, "触足屈腹", R.drawable.preview_icon_chuzuqufu, R.raw.chuzuqufu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(6, 12, "V字两头起", R.drawable.preview_icon_vliangtouqi, R.raw.vliangtouqi)
            },
            {
                    new LocalActionInfo(8, 15, "扭转卷腹", R.drawable.preview_icon_niuzhuangjuanfu, R.raw.niuzhuanjuanfu),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(9, 15, "屈膝仰卧起坐", R.drawable.preview_icon_quxiyangwoqizuo, R.raw.quxiyangwoqizuo),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(29, 15, "俯卧两头起", R.drawable.preview_icon_fuwoliangtouqi, R.raw.fuwoliangtouqi),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(10, 15, "仰卧举腿", R.drawable.preview_icon_yangwojutui, R.raw.yangwojutui),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(11, 15, "臀桥", R.drawable.prview_icon_biqiao, R.raw.biqiao),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(12, 15, "空中蹬车", R.drawable.preview_icon_kongzhongdengche, R.raw.kongzhongdengche),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(23, 15, "抬腿臀桥", R.drawable.preview_icon_taituibiqiao, R.raw.taituibiqiao),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(6, 15, "V字两头起", R.drawable.preview_icon_vliangtouqi, R.raw.vliangtouqi),
                    new LocalActionInfo(Config.ACTION_ID_REST, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(14, 15, "侧臀桥", R.drawable.preview_icon_cebiqiao, R.raw.cebiqiao)
            },
            /*
            {
                    new LocalActionInfo(0, 25, "空中蹬车", R.drawable.preview_icon_kongzhongdengche, R.raw.kongzhongdengche),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(2, 24, "臀桥", R.drawable.prview_icon_biqiao, R.raw.biqiao),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(3, 20, "卷腹", R.drawable.preview_icon_juanfu, R.raw.juanfu),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(4, 25, "直腿收腹伸展", R.drawable.preview_icon_zhituishoufu, R.raw.zhituishoufu),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(5, 24, "抬腿臀桥", R.drawable.preview_icon_taituibiqiao, R.raw.taituibiqiao),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(6, 20, "侧臀桥", R.drawable.preview_icon_cebiqiao, R.raw.cebiqiao),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(7, 0, "游式挺身", R.drawable.preview_icon_youshitingshen, R.raw.youshitingshen),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(8, 20, "抬腿屈膝卷腹", R.drawable.preview_icon_taituiquxijuanfu, R.raw.taituiquxijuanfu),
                    new LocalActionInfo(1, 20, REST, R.drawable.preview_icon_rest, 0),
                    new LocalActionInfo(9, 20, "V字两头起", R.drawable.preview_icon_vliangtouqi, R.raw.vliangtouqi),
                    new LocalActionInfo(10, 15, "仰卧举腿", R.drawable.preview_icon_yangwojutui, R.raw.yangwojutui)
            },
            */
//            new ItemInfo(4, new LocalActionInfo[]{
//                    new LocalActionInfo(20, 80, "直腿收腹伸展", R.drawable.preview_icon_taituiquxijuanfu),
//                    new LocalActionInfo(20, 60, "屈膝仰卧起坐", R.drawable.preview_icon_vliangtouqi),
//                    new LocalActionInfo(20, 80, "上腹部仰卧起坐", R.drawable.preview_icon_juanfu),
//                    new LocalActionInfo(20, 80, "仰卧举腿", R.drawable.preview_icon_eluosi),
//                    new LocalActionInfo(20, 80, "仰卧双腿扭转", R.drawable.preview_icon_youshitingshen),
//                    new LocalActionInfo(20, 50, "臀桥交换抬腿", R.drawable.preview_icon_zhituishoufu),
//                    new LocalActionInfo(20, 80, "V字两头起", R.drawable.preview_icon_vliangtouqi)}, 25 ,150),
    };

    public static final BadgeInfo[] BADGE_INFOS = {
            new BadgeInfo(1, R.drawable.badge_1_s, R.drawable.badge_1, "破冰者", "每种训练方案，第1次完成后，获得1个\n（每个方案只能获得一次）"),
            new BadgeInfo(2, R.drawable.badge_2_s, R.drawable.badge_2, "勇往直前", "一天内2次完成任意训练方案，获得1个\n（每天都可以获得1次）"),
            new BadgeInfo(3, R.drawable.badge_3_s, R.drawable.badge_3, "无畏无惧", "一天内3次完成任意训练方案，获得1个\n（每天都可以获得1次）"),
            new BadgeInfo(4, R.drawable.badge_4_s, R.drawable.badge_4, "三连冠", "累计3天，每天2次完成任意训练方案，获得1个\n（可重复获得）"),
            new BadgeInfo(5, R.drawable.badge_5_s, R.drawable.badge_5, "一次突破者", "累计7天，每天2次完成任意训练方案，获得1个\n（可重复获得）"),
            new BadgeInfo(6, R.drawable.badge_6_s, R.drawable.badge_6, "改变者", "累计30天，每天2次完成任意训练方案，获得1个\n（可重复获得）"),
            new BadgeInfo(7, R.drawable.badge_7_s, R.drawable.badge_7, "掌控者", "累计90天，每天2次完成任意训练方案，获得1个\n（可重复获得）"),
            new BadgeInfo(8, R.drawable.badge_8_s, R.drawable.badge_8, "王者归来", "累计300天，每天2次完成任意训练方案，获得1个\n（可重复获得）")
    };

    public static final int[] soundResource = new int[]{
            soundPool.load(MainApplication.getInstance(), R.raw.areyouready, 1),
            R.raw.fivetoone,
            soundPool.load(MainApplication.getInstance(), R.raw.go, 1),
            soundPool.load(MainApplication.getInstance(), R.raw.rest, 1),
            soundPool.load(MainApplication.getInstance(), R.raw.workoutcompleted, 1),
            soundPool.load(MainApplication.getInstance(), R.raw.dontgiveup, 1),
    };

    public static void soundPlay(int soundId) {
        if (PreferencesUtils.getBoolean(MainApplication.getInstance(), "sound", true))
            soundPool.play(soundId, 1, 1, 0, 0, 1);
    }

    //社会化分享配置
    public static final String wx_appID = "wx6b9ea8a6e96ce005";
    public static final String wx_appSecret = "e1300234da6e3a84882f502af5ac7cd5";

    public static final String qq_appID = "1104111629";
    public static final String qq_appKey = "VYwIvLRvxYoZ2pjt";

    //public static final String HOST_URL = "http://api.pocdoc.cn/";
    public static final String HOST_URL = "http://testapi.ikeepfit.cn/";

    public static final String LOGIN = HOST_URL + "keepfit_api/UC/users";
    public static final String UPDATE_RECORD = HOST_URL + "keepfit_api/sync/"+APP_NAME;
    public static final String SYNC_BADGE = HOST_URL + "keepfit_api/sync/badge/%s";
    public static final String BADGE_DETAIL = HOST_URL + "keepfit_api/Badge/Badge/index?uid=%s";

    public static final String STATISTICS_URL = HOST_URL + "keepfit_api/stat/stat/index?uid=%s&appname="+APP_NAME;




}
