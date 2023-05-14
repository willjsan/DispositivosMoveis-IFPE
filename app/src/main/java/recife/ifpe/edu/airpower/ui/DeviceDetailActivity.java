package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.insertionwizard.DeviceSetupWizardHolderActivity;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailActivity.class.getSimpleName();

    private List<AirPowerDevice> mDevices = new ArrayList<>();
    private AirPowerRepository mRepo;
    private Button mDeleteDevice;
    private Button mEditDevice;
    private AirPowerDevice mDevice;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");

        // Retrieve repo instance
        mRepo = AirPowerRepository.getInstance(getApplicationContext());

        // Device Description
        mDescription = findViewById(R.id.text_device_banner_description);

        // Retrieve selected item
        Intent intent = getIntent();
        if (intent != null) {
            int selectedItemIndex = intent.getIntExtra(AirPowerConstants.DEVICE_ITEM_INDEX, -1);
            List<AirPowerDevice> devices = mRepo.getDevices();

            if (devices == null || devices.isEmpty()) {
                Toast.makeText(this, "No Item", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!(selectedItemIndex < 0 || selectedItemIndex > devices.size())) {
                mDevice = devices.get(selectedItemIndex);
                //mDescription.setText(mDevice.getDescription());
            }
        }

        // Misc. Delete button
        mDeleteDevice = findViewById(R.id.button_miscellaneous_delete_device);
        mDeleteDevice.setOnClickListener(v -> {
            mRepo.delete(mDevice);
            // TODO improve this implementation to just update listview items instead going to main activity
            // TODO change implementation to be similar with edit device
            Toast.makeText(this, "Device Removed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DeviceDetailActivity.this, MainHolderActivity.class);
            startActivity(i);
            finish();
        });

        // Misc. Edit button
        mEditDevice = findViewById(R.id.button_miscellaneous_edit_device);
        mEditDevice.setOnClickListener(v -> {
            Intent i = new Intent(DeviceDetailActivity.this,
                    DeviceSetupWizardHolderActivity.class);
            i.putExtra(AirPowerConstants.KEY_DEVICE_ID, mDevice.getId());
            i.putExtra(AirPowerConstants.KEY_ACTION, AirPowerConstants.ACTION_EDIT_DEVICE);
            startActivity(i);
            finish();
        });

    }
}