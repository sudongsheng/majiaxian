package cn.pocdoc.majiaxian.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.pocdoc.majiaxian.MainApplication;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.CourseInfo;
import cn.pocdoc.majiaxian.model.CourseListInfo;
import cn.pocdoc.majiaxian.model.LocalCourseInfo;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.utils.ViewHolder;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class MainAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;

    private List<CourseInfo> courseInfos;

    private BitmapUtils bitmapUtils;

    public MainAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        courseInfos = MainApplication.getInstance().getCourseListInfo().getCourses();

    }

    @Override
    public int getCount() {
        //return CLocalCoursesN_IMGS.length;
        return courseInfos.size();
    }

    @Override
    public Object getItem(int i) {
        //return CLocalCoursesN_IMGS[i];
        return courseInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        //return CLocalCoursesN_IMGS[i].getId();
        return courseInfos.get(i).getId();
    }

    // TODO maybe need to download the background image through the server
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.adapter_main, null);
        LinearLayout bg= ViewHolder.get(view,R.id.main_ll);
        TextView title= ViewHolder.get(view,R.id.main_img_title);
        TextView subTitle= ViewHolder.get(view,R.id.main_img_sub);
        ImageView lock=ViewHolder.get(view,R.id.lock);
        //MainItemInfo imageInLocalCourseInfoIN_IMGS[i];
        CourseInfo courseInfo = courseInfos.get(i);

        if (courseInfo instanceof LocalCourseInfo){
            bg.setBackgroundResource(((LocalCourseInfo) courseInfo).getResourcesId());
        }else {
            if (bitmapUtils == null){
                bitmapUtils = new BitmapUtils(context);
            }
            bitmapUtils.display(bg, courseInfo.getImageUrl());
        }

        title.setText(courseInfo.getName());
        subTitle.setText(courseInfo.getSubdesc());
        //if (CLocalCoursesN_IMGS[i].isActive() && CLocalCoursesN_IMGS[i].isExist()) {
        if (courseInfo.isUnlocked()) {
            lock.setVisibility(View.INVISIBLE);
            setAlpha(new TextView[]{title, subTitle}, 1.0f);
        }else {
            lock.setVisibility(View.VISIBLE);
            setAlpha(new TextView[]{title, subTitle}, 0.4f);
        }

        FontManager.changeFonts((ViewGroup) view.findViewById(R.id.adapter_main));
        return view;
    }

    @TargetApi(11)
    private void setAlpha(TextView[] tvs, float alpha){
        if(Build.VERSION.SDK_INT >= 11){
            for(TextView tv : tvs){
                tv.setAlpha(alpha);
            }
        }
    }

}
