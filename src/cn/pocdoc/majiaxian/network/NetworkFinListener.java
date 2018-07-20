package cn.pocdoc.majiaxian.network;

import android.content.Context;
import cn.pocdoc.majiaxian.config.TipConfig;
import cn.pocdoc.majiaxian.utils.Pop;
import org.json.JSONObject;

/**
 * @author xuri
 */
public abstract class NetworkFinListener<T> {
    private Context context;

    public NetworkFinListener(Context context) {
        this.context = context;
    }

    //数据获取成功，status为1,数据为空或success
    public void finish(String msg) {
        Pop.popToast(context, msg);
    }

    public void finish(String data, String msg){

    }

    //数据获取成功，status为1且解析成功
    public void finish(T t){

    }

    //数据获取成功，states为1，数据为空时或者Json解析失败
    public void parseError(int i) {
        if (i == 1)
            Pop.popToast(context, TipConfig.JSON_PARSE_NULL);
        else
            Pop.popToast(context, TipConfig.GSON_PARSE_ILLEGAL);
    }

    //数据获取成功，status不为1
    public void error(int status, String msg) {
        Pop.popToast(context, msg);
    }

    //数据网络请求获取失败
    public void error() {
        Pop.popToast(context, TipConfig.SYSTEM_ERROR);
    }

}
