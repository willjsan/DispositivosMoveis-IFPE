package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.AirPowerDeviceDAO;
import recife.ifpe.edu.airpower.model.adapter.MainActivityItemAdapter;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView mListView;
    private List<AirPowerDevice> mDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");

        mDevices = new AirPowerDeviceDAO().getDevices();
        mListView = findViewById(R.id.list_items);
        mListView.setAdapter(new MainActivityItemAdapter(mDevices, this));
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(mDevices.get(position));
        });
    }

    private void startActivity(AirPowerDevice device) {
        Intent i = new Intent(MainActivity.this, DeviceDetailActivity.class);
        i.putExtra(AirPowerConstants.KEY_DEVICE_DESCRIPTION, device.getDescription());
        startActivity(i);
    }
}