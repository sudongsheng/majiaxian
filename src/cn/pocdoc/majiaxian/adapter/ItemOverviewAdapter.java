package cn.pocdoc.majiaxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pocdoc.majiaxian.R;
import cn.pocdoc.majiaxian.activity.ItemOverviewActivity;
import cn.pocdoc.majiaxian.config.Config;
import cn.pocdoc.majiaxian.model.ActionInfo;
import cn.pocdoc.majiaxian.model.LocalActionInfo;
import cn.pocdoc.majiaxian.utils.ViewHolder;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/16 0016.
 */
public class ItemOverviewAdapter extends BaseAdapter {

    private Context context;
    private List<ActionInfo> list;
    private LayoutInflater inflater;
    private BitmapUtils bmUtil;

    public ItemOverviewAdapter(Context context, List<ActionInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        bmUtil = new BitmapUtils(context);
        bmUtil.configDefaultLoadFailedImage(R.drawable.preview_icon_fuwoliangtouqi);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_plan_preview_item, viewGroup, false);
        }
        ActionInfo action = list.get(i);
        ImageView icon = ViewHolder.get(view, R.id.icon);
        ImageView flag = ViewHolder.get(view, R.id.flag);
        TextView name = ViewHolder.get(view, R.id.name);
        TextView count = ViewHolder.get(view, R.id.count);
        name.setText(action.getActionName());
        int trainTimes = action.getDuration();
        if (action.getActionId() != Config.ACTION_ID_REST) {
            if (trainTimes == 0)
                count.setText(context.getResources().getString(R.string.no_count_tip));
            else
                count.setText(trainTimes + "次");
            flag.setVisibility(View.VISIBLE);
            if (action instanceof LocalActionInfo){
                flag.setImageResource(R.drawable.preview_icon_preview);
            }else if(ItemOverviewActivity.existVideo(context, action.getVideoUrl())){
                flag.setImageResource(R.drawable.preview_icon_preview);
            }else{
                flag.setImageResource(R.drawable.preivew_download);
            }
            view.setBackgroundColor(context.getResources().getColor(R.color.itemOverview_group_bg));
            if (action instanceof LocalActionInfo){
                icon.setImageResource(((LocalActionInfo) action).getActionResourceId());
            }else {
                bmUtil.display(icon, action.getActionIconUrl());
            }
        } else {
            icon.setImageResource(R.drawable.preview_icon_rest);
            count.setText(trainTimes + "s");
            flag.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(context.getResources().getColor(R.color.itemOverview_rest_bg));
        }
        return view;
    }
}
