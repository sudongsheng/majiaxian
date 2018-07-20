package cn.pocdoc.majiaxian.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/1/15 0015.
 */
public class Pop {
    static Toast toast = null;

    public static void popToast(Context context, String title) {
        if (toast == null)
            toast = Toast.makeText(context, title, Toast.LENGTH_LONG);
        else
            toast.setText(title);
        toast.show();
    }

    public static CustomDialog popDialog(Context context, int layout) {
        return new CustomDialog(context, layout).show();
    }

    public static CustomDialog popDialog(Context context, int layout, int style) {
        return new CustomDialog(context, layout, style).show();
    }
}
