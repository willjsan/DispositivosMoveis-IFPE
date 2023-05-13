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

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;

public class MainActivityItemAdapter extends BaseAdapter {

    private static final String TAG = MainActivityItemAdapter.class.getSimpleName();
    private final Context mContext;
    private List<AirPowerDevice> mItems;

    public MainActivityItemAdapter(List<AirPowerDevice> items, Context context) {
        this.mContext = context;
        this.mItems = items;

        if (items == null) {
            this.mItems = new ArrayList<>();
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "Item list is null");
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public AirPowerDevice getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.main_list_item, parent, false);
            holder = new ViewHolder();
            holder.deviceDescription = view.findViewById(R.id.text_main_item_description);
            holder.deviceIcon = view.findViewById(R.id.image_main_item_icon);
            holder.deviceLabel = view.findViewById(R.id.text_main_item_label);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        AirPowerDevice item = mItems.get(position);
        Drawable icon = AirPowerUtil.getDrawable(item.getIcon(), mContext);
        holder.deviceIcon.setImageDrawable(icon);
        holder.deviceLabel.setText(item.getName());
        holder.deviceDescription.setText(item.getDescription());
        return view;
    }

    private static class ViewHolder {
        private ImageView deviceIcon;
        private TextView deviceLabel;
        private TextView deviceDescription;
    }
}