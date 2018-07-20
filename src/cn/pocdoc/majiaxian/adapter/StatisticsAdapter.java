package cn.pocdoc.majiaxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.db.RecordsDB;
import cn.pocdoc.majiaxian.model.LocalCourseInfo;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 15/1/20.
 */
public class StatisticsAdapter extends BaseAdapter {

    private Context context;
    private List<LocalCourseInfo> infos;
    public StatisticsAdapter(Context context){
        this.context = context;
        infos=new ArrayList<LocalCourseInfo>();
        for(int i=0;i<Config.LocalCourses.length;i++){
            if(Config.LocalCourses[i].isUnlocked()){
                infos.add(Config.LocalCourses[i]);
            }
        }
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_statistics, viewGroup, false);
        }
        RelativeLayout layout = ViewHolder.get(view, R.id.adapter_statistics);
        TextView name = ViewHolder.get(view, R.id.name);
        TextView dayCount = ViewHolder.get(view, R.id.day_count);
        TextView timeCount = ViewHolder.get(view, R.id.time_count);

        LocalCourseInfo info=infos.get(i);
        layout.setBackgroundResource(info.getLockResourceId());
        name.setText(info.getName());
        dayCount.setText(RecordsDB.getInstance(context).getDayCountByCourseId(info.getId())+"");
        timeCount.setText(RecordsDB.getInstance(context).getTimeCountByCourseId(info.getId())+"");

        FontManager.changeFonts((ViewGroup) view.findViewById(R.id.adapter_statistics));
        return view;
    }
}
