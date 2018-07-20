package cn.pocdoc.majiaxian.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.model.BadgeSyncInfo;
import cn.pocdoc.majiaxian.model.RecordDetailInfo;
import cn.pocdoc.majiaxian.model.RecordInfo;
import cn.pocdoc.majiaxian.utils.LogUtil;
import cn.pocdoc.majiaxian.utils.PreferencesUtils;
import cn.pocdoc.majiaxian.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengwei on 15/1/23.
 */
public class RecordsDB {

    private SQLiteDatabase db;
    public static final String DB_NAME = "record.db";
    private static final String PREFIX = "t_";
    public static final String TB_RECORD = PREFIX + "record";
    public static final String TB_BADGE = PREFIX + "badge";
    public static final String TB_RECORD_DETAIL = PREFIX + "record_detail";

    private static RecordsDB single;

    public static RecordsDB getInstance(Context context) {
        return single == null ? new RecordsDB(context) : single;
    }

    private RecordsDB(Context context) {
        db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        this.createRecordTable();
        this.createBadgeTable();
        this.createRecordDetail();
    }

    /**
     * 插入运动纪录
     *
     * @param courseId  课程id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param finish    是否完成
     * @return 插入id
     */
    public int insertRecord(int courseId, long startTime, long endTime, String uid, char finish) {
        String sql = "insert into " + TB_RECORD + " (course_id,start_at,end_at,uid,finished,sync) values (?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{courseId, startTime, endTime, uid, finish, '0'});
        Cursor c = db.rawQuery("select LAST_INSERT_ROWID() ", null);
        c.moveToFirst();
        return c.getInt(0);
    }

    /**
     * 插入徽章获得纪录
     *
     * @param badge_id  徽章id
     * @param record_id 外键，运动id
     */
    public void insertBadge(int badge_id, String uid, int record_id) {
        String sql = "insert into " + TB_BADGE + " (badge_id,created_at,uid,record_id,sync) values (?,?,?,?,?)";
        db.execSQL(sql, new Object[]{badge_id, TimeUtil.getCurrentTimestamp(), uid, record_id, '0'});
    }

    /**
     * 更新结束时间和是否完成
     *
     * @param record_id
     * @param finish
     */
    public void updateEndTime(int record_id, char finish) {
        String sql = "update " + TB_RECORD + " set end_at=" + TimeUtil.getCurrentTimestamp() + ",finished="
                + finish + " where _id =" + record_id;
        db.execSQL(sql);
    }

    public void updateUid(String uid) {
        String recordSql = "update " + TB_RECORD + " set uid=" + uid + " where uid=0";
        db.execSQL(recordSql);
        String badgeSql = "update " + TB_BADGE + " set uid=" + uid + " where uid=0";
        db.execSQL(badgeSql);
    }

    /**
     * 插入运动细节
     *
     * @param recordId recordId
     * @param start    开始时间
     * @param end      结束时间
     * @param opId     -1为暂停，其他为动作id
     */
    public void insertRecordDetail(int recordId, long start, long end, int opId) {
        String sql = "insert into " + TB_RECORD_DETAIL + " (record_id,start_at,end_at,op_id,sync) values (?,?,?,?,?)";
        db.execSQL(sql, new Object[]{recordId, start, end, opId, '0'});
    }

    /**
     * 获取未同步的record数据
     */
    public List<RecordInfo> getRecordInfoList(Context context) {
        List<RecordInfo> infoList = new ArrayList<RecordInfo>();
        Cursor c = db.rawQuery("SELECT * from " + TB_RECORD +" where sync = '0'", null);
        Long uid = Long.parseLong(PreferencesUtils.getString(context, "uid"));
        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex("sync")).equals("0")) {
                RecordInfo info = new RecordInfo();
                int id = c.getInt(c.getColumnIndex("_id"));
                info.setId(id);
                info.setCourseId(c.getInt(c.getColumnIndex("course_id")));
                info.setStartTime(c.getLong(c.getColumnIndex("start_at")));
                info.setEndTime(c.getLong(c.getColumnIndex("end_at")));
                //info.setUid(c.getLong(c.getColumnIndex("uid")));
                info.setUid(uid);
                info.setFinished(c.getString(c.getColumnIndex("finished")));
                info.setDetailInfos(getRecordDetailInfoList(id));
                //if (ContentCache.getBadge)
                //info.setBadgeSyncInfos(getBadgeInfoList(id));
                infoList.add(info);
            }
        }
        c.close();
        return infoList;
    }

    /**
     * 获取未同步的recordDetail数据
     */
    public List<RecordDetailInfo> getRecordDetailInfoList(int id) {
        List<RecordDetailInfo> infoList = new ArrayList<RecordDetailInfo>();
        Cursor c = db.rawQuery("SELECT * from " + TB_RECORD_DETAIL, null);
        while (c.moveToNext()) {
            if (id == c.getInt(c.getColumnIndex("record_id"))) {
                RecordDetailInfo info = new RecordDetailInfo();
                info.setStartTime(c.getLong(c.getColumnIndex("start_at")));
                info.setEndTime(c.getLong(c.getColumnIndex("end_at")));
                info.setOpId(c.getLong(c.getColumnIndex("op_id")));
                infoList.add(info);
            }
        }
        c.close();
        return infoList;
    }

    /**
     * 获取未同步的badge数据
     */
    public List<BadgeSyncInfo> getBadgeInfoList(int id) {
        List<BadgeSyncInfo> infoList = new ArrayList<BadgeSyncInfo>();
        Cursor c = db.rawQuery("SELECT * from " + TB_BADGE, null);
        while (c.moveToNext()) {
            if (id == c.getInt(c.getColumnIndex("record_id")) && c.getString(c.getColumnIndex("sync")).equals("0")) {
                BadgeSyncInfo info = new BadgeSyncInfo();
                info.setBadgeId(c.getInt(c.getColumnIndex("badge_id")));
                info.setCreateTime(c.getLong(c.getColumnIndex("created_at")));
                info.setUid(c.getLong(c.getColumnIndex("uid")));
                infoList.add(info);
            }
        }
        c.close();
        return infoList;
    }

    /**
     * 删除同步的recordDetail数据
     */
    public void deleteRecordInfoList(int[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1)
                sb.append(ids[i] + ")");
            else
                sb.append(ids[i] + ",");
        }
//        String deleteRecord = "delete from " + TB_RECORD + " where _id in(" + sb.toString();
////        LogUtil.d("record", deleteRecord);
//        db.execSQL(deleteRecord);
        String deleteRecordDetail = "delete from " + TB_RECORD_DETAIL + " where record_id in(" + sb.toString();
        LogUtil.d("record", deleteRecordDetail);
        db.execSQL(deleteRecordDetail);
    }

    /**
     * 同步record数据,修改sync为已同步
     */
    public void updateRecordSync(int[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1)
                sb.append("_id=" + ids[i] + ")");
            else
                sb.append("_id=" + ids[i] + " or ");
        }
        String recordSql = "update " + TB_RECORD + " set sync=1" + " where (" + sb.toString() + " and sync=0";
        LogUtil.d("record", recordSql);
        db.execSQL(recordSql);
    }

    /**
     * 同步badge数据,修改sync为已同步
     */
    public void updateBadgeSync(int[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1)
                sb.append("record_id=" + ids[i] + ")");
            else
                sb.append("record_id=" + ids[i] + " or ");
        }
        String recordSql = "update " + TB_BADGE + " set sync=1" + " where (" + sb.toString() + " and sync=0";
        LogUtil.d("record", recordSql);
        db.execSQL(recordSql);
    }

    /**
     * 获取徽章个数
     *
     * @return
     */
    public Map<Integer, Integer> getBadgeNum() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Cursor c = db.rawQuery("SELECT badge_id,count(*) as sum from " + TB_BADGE
                + " group by badge_id ", null);
        while (c.moveToNext()) {
            map.put(c.getInt(c.getColumnIndex("badge_id")),
                    c.getInt(c.getColumnIndex("sum")));
        }
        return map;
    }

    /**
     * 某种徽章的数目
     *
     * @param badgeId
     * @return
     */
    public int getNumByBadgeId(int badgeId) {
        Cursor c = db.rawQuery("SELECT *  from " + TB_BADGE
                + " where badge_id =" + badgeId, null);
        return c.getCount();
    }

    /**
     * 获得某天完成训练的次数
     *
     * @param day
     * @return
     */
    public int getTrainCountByDay(String day) {
        Cursor c = db.rawQuery("SELECT * from " + TB_RECORD
                + " where date(end_at/1000,'unixepoch') = ? and finished = '1'", new String[]{day});
        return c.getCount();
    }

    /**
     * 课程锻炼天数
     *
     * @param courseId
     * @return
     */
    public int getDayCountByCourseId(int courseId) {
        return getCount("SELECT distinct date(end_at/1000,'unixepoch')  from " + TB_RECORD
                + " where finished = '1' and  course_id =" + courseId);
    }

    /**
     * 课程锻炼次数
     *
     * @return
     */
    public int getTimeCountByCourseId(int courseId) {
        return getCount("SELECT * from " + TB_RECORD
                + " where finished = '1' and  course_id = " + courseId);
    }


    /**
     * 每天完成dayFinishTime次运动的天数
     *
     * @param dayFinishTime
     * @return
     */
    public int dayFinishTimeCount(int dayFinishTime) {
        return getCount("SELECT * from " + TB_RECORD
                + " where finished = '1' group by date(end_at/1000,'unixepoch') having count(_id) >=" + dayFinishTime);
    }

    /**
     * 某个教程是否已经获得徽章
     *
     * @param courseId
     * @return
     */
    public boolean hasGetBadge(int courseId) {
        String sql = "select * from " + TB_RECORD + " where course_id = " + courseId + " and _id in (select _id from " + TB_BADGE + ")";
        return getCount(sql) > 0;
    }

    /**
     * 获得徽章的总数目
     *
     * @return
     */
    public int getBadgeCount() {
        return getCount("SELECT *  from " + TB_BADGE);
    }


    /**
     * 创建运动纪录表
     */
    private void createRecordTable() {
        execSQL("CREATE TABLE IF NOT EXISTS " + TB_RECORD + " (" +
                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "`course_id` INTEGER NOT NULL ," +
                "`start_at` timestamp NOT NULL ," +
                "`end_at` timestamp," +
                "`uid` BIGINT DEFAULT 0," +
                "`finished` char(1) DEFAULT ‘0’, " +
                "`sync` char(1) DEFAULT ‘0’" +
                ")");
    }

    /**
     * 创建勋章纪录表
     */
    private void createBadgeTable() {
        execSQL("CREATE TABLE IF NOT EXISTS " + TB_BADGE + " (" +
                "`_id` INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "`badge_id` INTEGER NOT NULL," +
                "`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "`uid` BIGINT DEFAULT 0," +
                "`record_id` BIGINT NOT NULL," +
                "`sync` char(1) DEFAULT ‘0’" +
                ")");
    }

    /**
     * 运动细节表
     */
    private void createRecordDetail() {
        execSQL("CREATE TABLE IF NOT EXISTS " + TB_RECORD_DETAIL + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "record_id INTEGER NOT NULL," +
                "start_at timestamp NOT NULL," +
                "end_at timestamp," +
                "op_id INTEGER NOT NULL," +
                "`sync` char(1) DEFAULT ‘0’" +
                ")");
    }

    protected void execSQL(String sql) {
        db.execSQL(sql);
    }

    /**
     * 返回执行sql语句的条数
     *
     * @param sql
     * @return
     */
    private int getCount(String sql) {
        Cursor c = db.rawQuery(sql, null);
        return c.getCount();
    }

    /**
     * 删除数据库
     *
     * @param tableName
     */
    private void dropTable(String tableName) {
        db.execSQL("drop table if exists " + tableName);
    }


    /**
     * 关闭数据库
     */
    protected void close(){
        if(db.isOpen()){
            db.close();
        }
    }

}
