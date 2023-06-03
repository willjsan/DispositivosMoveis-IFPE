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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class DeviceIconAdapter extends BaseAdapter {

    private static final String TAG = DeviceIconAdapter.class.getSimpleName();
    private final Context mContext;
    private static final List<String> mIcons = new ArrayList<>(Arrays.asList(
            "air_conditioner_icon",
            "blender_juice_kitchen_mixer_icon",
            "blow_breeze_cool_icon",
            "cleaner_vacuum_icon",
            "coffeemaker_icon",
            "cooker_kitchen_oven_icon",
            "cooking_kitchen_microwave_con",
            "freezer_fridge_icon",
            "head_shower_icon",
            "kitchen_rice_cooker_icon",
            "sound_speakers_icon"
    ));

    public static List<String> getIcons(){
        return mIcons;
    }

    public DeviceIconAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return mIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.device_icon_dropdown, parent, false);
            holder = new DeviceIconAdapter.ViewHolder();
            holder.deviceIcon = view.findViewById(R.id.device_spinner_icon);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DeviceIconAdapter.ViewHolder) convertView.getTag();
        }

        String selectedIcon = mIcons.get(position);
        Drawable icon = AirPowerUtil.getDrawable(selectedIcon, mContext);
        holder.deviceIcon.setImageDrawable(icon);
        return view;
    }

    private static class ViewHolder {
        private ImageView deviceIcon;
    }
}