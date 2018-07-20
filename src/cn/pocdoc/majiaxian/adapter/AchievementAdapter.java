package cn.pocdoc.majiaxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.BadgeInfo;
import cn.pocdoc.majiaxian.utils.FontManager;
import cn.pocdoc.majiaxian.utils.ViewHolder;

import java.util.Map;

/**
 * Created by pengwei on 15/1/20.
 */
public class AchievementAdapter extends BaseAdapter {
    private Context context;
    private Map<Integer, Integer> badgeNum;

    public AchievementAdapter(Context context, Map<Integer, Integer> badgeNum) {
        this.context = context;
        this.badgeNum = badgeNum;
    }

    @Override
    public int getCount() {
        return Config.BADGE_INFOS.length;
    }

    @Override
    public Object getItem(int i) {
        return Config.BADGE_INFOS[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_achievement, viewGroup, false);
        }

        ImageView icon = ViewHolder.get(view, R.id.icon);
        TextView tv = ViewHolder.get(view, R.id.name);
        TextView num = ViewHolder.get(view, R.id.achievement_number);

        BadgeInfo info = Config.BADGE_INFOS[i];
        int numTip = badgeNum.get(info.getId()) == null ? 0 : badgeNum.get(info.getId());
        if (numTip == 0) {
            icon.setImageResource(R.drawable.badge_unknow_s);
            num.setVisibility(View.GONE);
        } else {
            num.setVisibility(View.VISIBLE);
            num.setText(numTip + "");
            icon.setImageResource(info.getSmallResourceId());
        }
        tv.setText(info.getName());

        FontManager.changeFonts((ViewGroup) view.findViewById(R.id.adapter_achievement));
        return view;
    }
}
