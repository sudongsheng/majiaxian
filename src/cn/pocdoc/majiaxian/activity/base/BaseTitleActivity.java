package cn.pocdoc.majiaxian.activity.base;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.view.BaseTitleLayout;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by pengwei on 14/12/14.
 */
public abstract class BaseTitleActivity extends BaseActivity {
    public final static int BTN_BACK = R.drawable.back;
    public final static int BTN_MENU = 0;

    public final static int LEFT_BTN = 1;
    public final static int RIGHT_BTN = 2;
    public final static int CENTER_BTN = 0;


    private BaseTitleLayout layout;

    public void setContentView(int layoutResID) {
        if (layout == null) {
            layout = new BaseTitleLayout(this, layoutResID);
        }
        super.setContentView(layout);
        this.setClickListener(new View[]{layout.leftImgBtn, layout.leftBtn, layout.rightImgBtn, layout.rightBtn});
        ViewUtils.inject(this);
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(int title) {
        if (layout != null && title > 0) {
            layout.title.setText(getString(title));
        }
    }

    public void setTitle(String title) {
        if (layout != null && title != null) {
            layout.title.setText(title);
        }
    }

    public void setTitle(View v) {
        setTitle(v, null);
    }

    public void setTitle(View v, FrameLayout.LayoutParams param) {
        if (layout != null && v != null) {
            layout.title.setVisibility(View.GONE);
            FrameLayout content = layout.titlePanel;
            if (param == null) {
                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                p.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                content.addView(v, p);
            } else {
                content.addView(v, param);
            }
        }
    }

    /**
     * 设置标题字体颜色
     * @param color
     */
    public void setTitleColorResource(int color){
        getTitleView().setTextColor(color);
    }

    public void setTitleColor(String color){
        getTitleView().setTextColor(Color.parseColor(color));
    }

    /**
     * 设置背景颜色
     * @param resourceId
     */
    public void setTitleBackgroundResource(int resourceId) {
        layout.title_rr.setBackgroundResource(resourceId);
    }

    public void setTitleBackgroundColor(int color){
        layout.title_rr.setBackgroundColor(color);
    }

    public void setTitleBackgroundColor(String color){
        layout.title_rr.setBackgroundColor(Color.parseColor(color));
    }


    /**
     * 返回title对象
     * @return
     */
    public TextView getTitleView() {
        return layout.title;
    }

    /**
     * 设置按钮
     * @param leftBtnDraw
     * @param rightBtnDraw
     */
    public void setNavBtn(int leftBtnDraw, int rightBtnDraw) {
        if (layout != null) {
            setSingleNavBtn(layout.leftImgBtn, leftBtnDraw);
            setSingleNavBtn(layout.rightImgBtn, rightBtnDraw);
        }
    }

    public void setNavBtn(int leftBtnDraw, String leftVal, int rightBtnDraw, String rightVal) {
        if (layout != null) {
            setSingleNavBtn(layout.leftImgBtn, layout.leftBtn, leftBtnDraw, leftVal);
            setSingleNavBtn(layout.rightImgBtn, layout.rightBtn, rightBtnDraw, rightVal);
        }
    }


    /**
     * 设置单个按钮
     * @param btn
     * @param res
     */
    private void setSingleNavBtn(ImageButton btn, int res){
        if (res > 0) {
            btn.setImageResource(res);
        } else {
            btn.setVisibility(View.GONE);
        }
    }

    private void setSingleNavBtn(ImageButton imgBtn, Button btn , int res, String value){
        if (res > 0) {
            imgBtn.setImageResource(res);
        } else if (!TextUtils.isEmpty(value)) {
            imgBtn.setVisibility(View.GONE);
            btn.setVisibility(View.VISIBLE);
            btn.setText(value);
        } else {
            imgBtn.setVisibility(View.GONE);
        }
    }

    /**
     * TitleBar事件处理
     * @param buttonId
     * @param v
     */
    protected abstract void HandleTitleBarEvent(int buttonId, View v);


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.equals(layout.leftBtn) || view.equals(layout.leftImgBtn)) {
                HandleTitleBarEvent(LEFT_BTN, view);
            } else if (view.equals(layout.rightBtn) || view.equals(layout.rightImgBtn)) {
                HandleTitleBarEvent(RIGHT_BTN, view);
            } else if (view.equals(layout.title)) {
                HandleTitleBarEvent(CENTER_BTN, view);
            }
        }
    };

    /**
     * 设置事件处理
     *
     * @param views
     */
    private void setClickListener(View[] views) {
        for (View v : views) {
            v.setOnClickListener(listener);
        }
    }

    protected void hideTitle(boolean flag){
        if(!flag){
            layout.getTitleBar().setVisibility(View.VISIBLE);
        }else{
            layout.getTitleBar().setVisibility(View.GONE);
        }
    }

    protected void setTitleBarLeftButtonVisibility(int visibility){
       layout.leftBtn.setVisibility(visibility);
    }

    protected void setTitleBarLeftImageButtonVisibility(int visibility){
       layout.leftImgBtn.setVisibility(visibility);
    }

    protected void setTitleBarTitleVisibility(int visibility){
       layout.title.setVisibility(visibility);
    }

    protected void setTitleBarRightButtonVisibility(int visibility){
       layout.rightBtn.setVisibility(visibility);
    }

    protected void setTitleBarRightImageButtonVisibility(int visibility){
       layout.rightImgBtn.setVisibility(visibility);
    }

}
