package cn.pocdoc.majiaxian.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.helper.SystemHelper;

/**
 * Created by pengwei on 14/12/14.
 */
public class BaseTitleLayout extends LinearLayout {

    public TextView title;
    public ImageButton leftImgBtn;
    public ImageButton rightImgBtn;
    public Button leftBtn;
    public Button rightBtn;
    public RelativeLayout title_rr;

    public FrameLayout titlePanel;

    private View titleBar;

    public BaseTitleLayout(Context context, int layoutId) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //titleBar布局
        titleBar = inflater.inflate(R.layout.titlebar_layout, this, false);
        LayoutParams titleParam = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = SystemHelper.getScreenHeight(context) / 12;
        titleParam.height = height;
        addView(titleBar, titleParam);

        //content布局
        View content = inflater.inflate(layoutId, null);
        LayoutParams contentParam = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(content, contentParam);

        title = getView(R.id.titleText);
        leftImgBtn = getView(R.id.leftButton);
        leftBtn = getView(R.id.leftImageButton);
        rightImgBtn = getView(R.id.rightButton);
        rightBtn = getView(R.id.rightImageButton);
        titlePanel = getView(R.id.titleBar_titlePanel);
        title_rr = getView(R.id.titleBar_rr);

        Typeface tf=Typeface.createFromAsset(context.getAssets(), "fonts/fangzhengxidengxianjianti.ttf");
        title.setTypeface(tf);
        leftBtn.setTypeface(tf);
        rightBtn.setTypeface(tf);

        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
    }

    public View getTitleBar(){
        return titleBar;
    }

    private <T extends View> T getView(int id) {
        return (T) titleBar.findViewById(id);
    }
}