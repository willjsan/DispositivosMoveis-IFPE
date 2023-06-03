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
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.ChartAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.server.ServerInterfaceWrapper;
import recife.ifpe.edu.airpower.model.server.ServerManagerImpl;
import recife.ifpe.edu.airpower.ui.insertionwizard.DeviceSetupWizardHolderActivity;
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
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        if (intent.getAction().equals(AirPowerConstants.ACTION_LAUNCH_DETAIL)) {
            mDevice = mRepo.getDeviceById(intent
                    .getIntExtra(AirPowerConstants.KEY_DEVICE_ID, INVALID_ID));
            if (mDevice == null) return;
        }
        findViewsById();
        retrieveDeviceMeasurement();
        setListeners();

        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            return;
        }
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            LatLng recife = new LatLng(-8.04993041384011, -34.933112917530515);
            mMap.addMarker(new MarkerOptions().position(recife).title("Marker in Recife"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recife, 17F));
        });
    }

    private void setListeners() {
        mDeleteDeviceButton.setOnClickListener(v -> {
            performUnregisterDevice();
        });

        mEditDeviceButton.setOnClickListener(v -> {
            performEditDevice();
            finish();
        });

        mContextButton.setOnClickListener(v -> {
        });
    }

    private void retrieveDeviceMeasurement() {
        ServerManagerImpl.getInstance().getDeviceMeasurement(mDevice,
                new ServerInterfaceWrapper.MeasurementCallback() {
                    @Override
                    public void onResult(List<DeviceMeasurement> measurements) {
                        new ChartAdapter.Builder(findViewById(R.id.my_chart2), measurements)
                                .build();
                    }

                    @Override
                    public void onFailure(String message) {
                        AirPowerLog.e(TAG, "onFailure: " + message);
                    }
                });
    }

    private void performEditDevice() {
        Intent i = new Intent(DeviceDetailActivity.this,
                DeviceSetupWizardHolderActivity.class);
        i.putExtra(AirPowerConstants.KEY_DEVICE_ID, mDevice.getId());
        i.putExtra(AirPowerConstants.KEY_ACTION, AirPowerConstants.ACTION_EDIT_DEVICE);
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
        ServerManagerImpl.getInstance().unregisterDevice(
                mDevice, new ServerInterfaceWrapper.UnregisterCallback() {
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