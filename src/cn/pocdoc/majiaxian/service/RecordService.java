package cn.pocdoc.majiaxian.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.cache.ContentCache;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.helper.NotificationHelper;
import cn.pocdoc.majiaxian.helper.SystemHelper;
import cn.pocdoc.majiaxian.model.RecordInfo;
import cn.pocdoc.majiaxian.network.NetWorkRequest;
import cn.pocdoc.majiaxian.network.NetworkFinListener;
import cn.pocdoc.majiaxian.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/1/28 0028.
 */
public class RecordService extends Service {

    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!SystemHelper.isNetworkConnected(this))
            return START_REDELIVER_INTENT;

        if (MainApplication.getInstance().isLogin()) {
            upLoadData();

        }
        LogUtil.d("record","******start");
        //return START_REDELIVER_INTENT;
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startRecordService() {
        MainApplication.getInstance().startService(
                new Intent(MainApplication.getInstance(), RecordService.class));
        LogUtil.d("record","%%%%%%%%%%%%");
    }

    private void upLoadData() {
        LogUtil.d("record","******upload");
        NetWorkRequest request = new NetWorkRequest(this);
        request.setProgressDialog(false);
        if (RecordsDB.getInstance(this).getRecordInfoList(this).size() != 0) {
            Map<String, String> map = new HashMap<String, String>();
            List<RecordInfo> recordInfos = RecordsDB.getInstance(this).getRecordInfoList(this);
            final int[] ids = new int[recordInfos.size()];
            for (int i = 0; i < recordInfos.size(); i++) {
                ids[i] = recordInfos.get(i).getId();
            }
            LogUtil.d("record",gson.toJson(recordInfos, new TypeToken<List<RecordInfo>>() {
            }.getType()));
            map.put(RecordsDB.TB_RECORD, gson.toJson(recordInfos, new TypeToken<List<RecordInfo>>() {
            }.getType()));

            request.postRequest(Config.UPDATE_RECORD, null, map, new NetworkFinListener(this) {
                @Override
                public void finish(String msg){
                    RecordsDB.getInstance(MainApplication.getInstance()).deleteRecordInfoList(ids);
                    RecordsDB.getInstance(MainApplication.getInstance()).updateRecordSync(ids);
                    RecordsDB.getInstance(MainApplication.getInstance()).updateBadgeSync(ids);
                }

                @Override
                public void finish(String data, String msg) {
                    try {
                        if(!data.equals("{}")){
                            JSONObject json = new JSONObject(data);
                            int badge = json.getInt("badge_id");
                            NotificationHelper.showNotification(getApplicationContext(), badge);
                        }else{
                            LogUtil.e("badge", "没获得徽章");
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void error() {
                    LogUtil.d("record", "some thing error");
                }

                @Override
                public void error(int status, String msg) {
                    LogUtil.d("record", "some thing error "+msg);
                }

                @Override
                public void parseError(int i) {
                    LogUtil.d("record", "****json parse error"+i);
                }
            });
        }
        stopSelf();
    }
}
