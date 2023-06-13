package recife.ifpe.edu.airpower.model.adapter;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.Group;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;

public class GroupsAdapter extends BaseAdapter {

    private static final String TAG = GroupsAdapter.class.getSimpleName();
    private List<Group> mGroups;
    private Context mContext;

    private AirPowerRepository mRepo;


    public GroupsAdapter(Context context) {
        this.mRepo = AirPowerRepository.getInstance(context);
        this.mGroups = mRepo.getGroups();
        this.mContext = context;
        if (mGroups == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "can't set adapter");
        }
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        GroupsAdapter.ViewHolder holder;
        if (view == null) {
            v = LayoutInflater.from(mContext)
                    .inflate(R.layout.home_groups_item_dropdown, viewGroup, false);
            holder = new GroupsAdapter.ViewHolder();
            holder.groupName = v.findViewById(R.id.tv_home_groups_item_dropdown_label);
            holder.groupIcon = v.findViewById(R.id.iv_home_groups_item_dropdown_icon);
            v.setTag(holder);
        } else {
            v = view;
            holder = (GroupsAdapter.ViewHolder) view.getTag();
        }

        String selectedGroupName = mGroups.get(i).getName();
        String selectedIcon = mGroups.get(i).getIcon();
        Drawable iconDrawable = AirPowerUtil.getDrawable(selectedIcon, mContext);
        holder.groupName.setText(selectedGroupName);
        holder.groupIcon.setImageDrawable(iconDrawable);
        return v;
    }

    private static class ViewHolder {
        private TextView groupName;
        private ImageView groupIcon;
    }

    @Override
    public void notifyDataSetChanged() {
        mGroups = mRepo.getGroups();
        super.notifyDataSetChanged();
    }
}
