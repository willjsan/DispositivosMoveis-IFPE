package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.ChartAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceStatus;
import recife.ifpe.edu.airpower.model.server.AirPowerServerManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.deviceinsertionwizard.DeviceSetupWizardHolderActivity;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailActivity.class.getSimpleName();
    public static final int INVALID_ID = -1;
    private AirPowerRepository mRepo;
    private Button mDeleteDeviceButton;
    private Button mEditDeviceButton;
    private AirPowerDevice mDevice;
    private Button mContextButton;
    private GoogleMap mMap;
    private ProgressDialog mProgressDialog;

    /**
     * Status card
     */
    private TextView mStatusTVStatus;
    private TextView mStatusTVIssue;
    private SwitchCompat mStatusSwActivate;
    private CardView mStatusCard;
    private CardView mCardLocalization;
    private CardView mCardConsumption;
    private AirPowerServerManagerImpl mAirPowerServer;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case AirPowerConstants.DEVICE_CONNECTION_SUCCESS:
                    mRepo.delete(mDevice);
                    Toast.makeText(getApplicationContext(), "Device removed",
                            Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(DeviceDetailActivity.this,
                            MainHolderActivity.class);
                    intent.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
                    startActivity(intent);
                    finish();
                    break;
                case AirPowerConstants.DEVICE_CONNECTION_FAIL:
                    mProgressDialog.dismiss();
                    if (AirPowerLog.ISLOGABLE)
                        AirPowerLog.w(TAG, "Failure when unregister device");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        Intent intent = getIntent();
        if (intent == null || intent.getAction() == null) {
            return;
        }
        mAirPowerServer = AirPowerServerManagerImpl.getInstance();
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        if (intent.getAction().equals(AirPowerConstants.ACTION_LAUNCH_DETAIL)) {
            mDevice = mRepo.getDeviceById(intent
                    .getIntExtra(AirPowerConstants.KEY_DEVICE_ID, INVALID_ID));
            if (mDevice == null) return;
        }
        findViewsById();
        setListeners();
        retrieveDeviceMeasurement();
        retrieveDeviceStatus();
        retrieveMap();
    }

    private void retrieveMap() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "retrieveMap");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.group_map);
        if (mapFragment == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "maps fragment is null");
            return;
        }
        if (mDevice.getLocalization() == null || mDevice.getLocalization().isEmpty()) {
            mCardLocalization.setVisibility(View.GONE);
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.w(TAG, "device localization not set");
            return;
        }
        mCardLocalization.setVisibility(View.VISIBLE);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            String localization = mDevice.getLocalization();
            if (localization == null) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.w(TAG, "device localization is null");
                Toast.makeText(this, "Localization not set", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] split = localization.split(",");
            String lat = split[0];
            String lon = split[1];
            if (lon.isEmpty() || lat.isEmpty()) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.w(TAG, "device latitude or longitude is empty");
                return;
            }
            LatLng deviceLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            mMap.addMarker(new MarkerOptions()
                    .position(deviceLocation).title("Device Localization"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 17F));
        });
    }

    private void retrieveDeviceStatus() {
        mAirPowerServer.getDeviceStatus(mDevice,
                new ServersInterfaceWrapper.DeviceStatusCallback() {
                    @Override
                    public void onSuccess(DeviceStatus deviceStatus) {
                        if (deviceStatus == null) {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.e(TAG, "response is null");
                            return;
                        }
                        mStatusSwActivate.setChecked(deviceStatus.isActivated());
                        mStatusTVStatus.setText(deviceStatus.getStatusMessage());
                        mStatusTVIssue.setText(String.valueOf(deviceStatus.getIssuesCount()));
                        statusSwitchActivateByUser = true;
                    }

                    @Override
                    public void onFailure(String message) {
                        mStatusTVStatus.setText(message);
                        mStatusSwActivate.setEnabled(false);
                    }
                });

    }

    private void setListeners() {
        mDeleteDeviceButton.setOnClickListener(v -> {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "delete device");
            performUnregisterDevice();
        });

        mEditDeviceButton.setOnClickListener(v -> {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "edit device");
            performEditDevice();
            finish();
        });

        mContextButton.setOnClickListener(v -> {
        });

        mStatusSwActivate.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.d(TAG, "device status switch changed:is checked:" + isChecked);
            if (!statusSwitchActivateByUser) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.d(TAG, "switch changed by system:is checked:" + isChecked);
                statusSwitchActivateByUser = false;
            } else {
                enableDisableDevice(mDevice);
            }
        });

        mStatusCard.setOnClickListener(view -> {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.d(TAG, "device status");
            Intent i = new Intent(DeviceDetailActivity.this,
                    DeviceStatusActivity.class);
            startActivity(i);
        });
    }

    private boolean statusSwitchActivateByUser = false;

    private void enableDisableDevice(AirPowerDevice device) {
        mAirPowerServer.enableDisableDevice(device,
                new ServersInterfaceWrapper.DeviceEnableDisableCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
    }

    private void retrieveDeviceMeasurement() {
        mAirPowerServer.getDeviceMeasurement(mDevice,
                new ServersInterfaceWrapper.MeasurementCallback() {
                    @Override
                    public void onSuccess(List<DeviceMeasurement> measurements) {
                        mCardConsumption.setVisibility(View.VISIBLE);
                        new ChartAdapter.Builder(findViewById(R.id.home_consumption_overview_chart),
                                measurements).build();
                    }

                    @Override
                    public void onFailure(String message) {
                        mCardConsumption.setVisibility(View.GONE);
                    }
                });
    }

    private void performEditDevice() {
        Intent i = new Intent(DeviceDetailActivity.this,
                DeviceSetupWizardHolderActivity.class);
        i.setAction(AirPowerConstants.ACTION_EDIT_DEVICE_);
        i.putExtra(AirPowerConstants.KEY_DEVICE_ID, mDevice.getId());
        startActivity(i);
        finish();
    }

    private void performUnregisterDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Unregister Device")
                .setMessage("Do you want unregister " + mDevice.getName() + "?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    showProgressDialog();
                    unregisterDevice();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                });
        builder.create().show();
    }

    private void unregisterDevice() {
        mAirPowerServer.unregisterDevice(
                mDevice, new ServersInterfaceWrapper.UnregisterCallback() {
                    @Override
                    public void onSuccess(String message) {
                        mHandler.sendEmptyMessage(AirPowerConstants.DEVICE_CONNECTION_SUCCESS);
                    }

                    @Override
                    public void onFailure(String message) {
                        mHandler.sendEmptyMessage(AirPowerConstants.DEVICE_CONNECTION_FAIL);
                    }
                }
        );
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Removing...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void findViewsById() {
        mDeleteDeviceButton = findViewById(R.id.button_miscellaneous_delete_device);
        mEditDeviceButton = findViewById(R.id.button_miscellaneous_edit_device);
        mContextButton = findViewById(R.id.button_device_detail_menu_context);

        // Status card
        mStatusTVStatus = findViewById(R.id.tv_ddetail_status_status);
        mStatusTVIssue = findViewById(R.id.tv_ddetail_status_issue_value);
        mStatusSwActivate = findViewById(R.id.sw_ddetail_status);
        mStatusCard = findViewById(R.id.card_detail_status);
        mCardLocalization = findViewById(R.id.card_localization);
        mCardConsumption = findViewById(R.id.card_consumption);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeviceDetailActivity.this,
                MainHolderActivity.class);
        intent.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
        startActivity(intent);
        finish();
    }
}