package recife.ifpe.edu.airpower.model.adapter;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class GroupDevicesSelectionAdapter extends BaseAdapter {

    private final String TAG = GroupDevicesSelectionAdapter.class.getSimpleName();

    private List<AirPowerDevice> mDevices;
    private final List<AirPowerDevice> mSelectedDevices = new ArrayList<>();
    private final Context mContext;

    private ViewHolder holder;

    public GroupDevicesSelectionAdapter(List<AirPowerDevice> items, Context context) {
        this.mDevices = items;
        this.mContext = context;

        if (items == null) {
            this.mDevices = new ArrayList<>();
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "Item list is null");
        }
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public AirPowerDevice getItem(int i) {
        return mDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDevices.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.group_device_selection_dropdown_item, parent, false);
            holder = new ViewHolder();
            holder.sDeviceName = view.findViewById(R.id.group_device_selection_dropdown_item_label);
            holder.sItemBackground = view.findViewById(R.id.image_view_item_device_group);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        AirPowerDevice device = mDevices.get(position);
        holder.sDeviceName.setText(device.getName());

        if (mSelectedDevices.contains(device)) {
            holder.sItemBackground.setBackgroundColor(
                    mContext.getResources().getColor(R.color.airpower_group_devices_item_selected));
        } else {
            holder.sItemBackground.setBackgroundColor(
                    mContext.getResources().getColor(R.color.airpower_banner));
        }
        return view;
    }

    public List<AirPowerDevice> getSelectedDevices() {
        return mSelectedDevices;
    }

    public void handleSelection(int position) {
        AirPowerDevice selectedDevice = mDevices.get(position);
        if (mSelectedDevices.contains(selectedDevice)) {
            mSelectedDevices.remove(selectedDevice);
            holder.sItemBackground.setBackgroundColor(
                    mContext.getResources().getColor(R.color.airpower_banner));
        } else {
            mSelectedDevices.add(selectedDevice);
            holder.sItemBackground.setBackgroundColor(
                    mContext.getResources().getColor(R.color.airpower_group_devices_item_selected));
        }
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView sDeviceName;
        private ImageView sItemBackground;
    }
}
