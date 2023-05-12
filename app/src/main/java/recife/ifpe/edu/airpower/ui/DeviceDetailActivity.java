package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.AirPowerDeviceDAO;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TextView mDescription = findViewById(R.id.text_device_banner_description);

        Intent intent = getIntent();
        if (intent != null) {
            int itemIndex = intent.getIntExtra(AirPowerConstants.DEVICE_ITEM_INDEX, -1);
            List<AirPowerDevice> devices = new AirPowerDeviceDAO().getDevices();

            if( !(itemIndex < 0 || itemIndex > devices.size())) {
                AirPowerDevice airPowerDevice = devices.get(itemIndex);
                mDescription.setText(airPowerDevice.getDescription());
            }

        }
    }
}