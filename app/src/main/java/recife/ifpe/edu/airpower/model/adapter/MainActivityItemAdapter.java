package recife.ifpe.edu.airpower.model.adapter;/*
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
import recife.ifpe.edu.airpower.model.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerUtil;

public class MainActivityItemAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<AirPowerDevice> mItems;
    private ImageView mDeviceIcon;
    private TextView mDeviceLabel;
    private TextView mDeviceDescription;
    private View mView = null;


    public MainActivityItemAdapter(List<AirPowerDevice> items, Context context) {
        this.mContext = context;
        this.mItems = items;
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

            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.main_list_item, parent, false);
            mDeviceIcon = mView.findViewById(R.id.image_main_item_icon);
            mDeviceLabel = mView.findViewById(R.id.text_main_item_label);
            mDeviceDescription = mView.findViewById(R.id.text_main_item_description);

            AirPowerDevice item = mItems.get(position);
            mDeviceLabel.setText(item.getName());
            mDeviceDescription.setText(item.getDescription());

            Drawable icon = AirPowerUtil.getDrawable(mItems.get(position).getIcon(), mContext);
            mDeviceIcon.setImageDrawable(icon);
        return mView;
    }
}
