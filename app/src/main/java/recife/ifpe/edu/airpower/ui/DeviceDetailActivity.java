package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.server.AirPowerServerDummy;
import recife.ifpe.edu.airpower.ui.insertionwizard.DeviceSetupWizardHolderActivity;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceDetailActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailActivity.class.getSimpleName();

    private final List<AirPowerDevice> mDevices = new ArrayList<>();
    private AirPowerRepository mRepo;
    private Button mDeleteDevice;
    private Button mEditDevice;
    private AirPowerDevice mDevice;
    private Button mContextButton;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");

        // Retrieve repo instance
        mRepo = AirPowerRepository.getInstance(getApplicationContext());

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

        mContextButton = findViewById(R.id.button_device_detail_menu_context);
        mContextButton.setOnClickListener(f -> {
            startActivity(new Intent(DeviceDetailActivity.this, ChartTest.class));
        });

        // Chart
        BarChart chart = findViewById(R.id.my_chart2);

        AirPowerServerDummy airPowerServer = new AirPowerServerDummy();
        DeviceMeasurement measureSet = airPowerServer
                .getMeasurementByDeviceToken(mDevice.getToken());
        BarDataSet dataSet = new BarDataSet(measureSet.getMeasurementSet(), "Day/KWh");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(0);

        BarData barChart = new BarData(dataSet);

        chart.setFitBars(true);
        chart.setData(barChart);
        chart.getDescription().setText("Daily Consumption");
        chart.animateY(2000);

        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            return;
        }
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng recife = new LatLng(-8.04993041384011, -34.933112917530515);
            mMap.addMarker(new MarkerOptions().position(recife).title("Marker in Recife"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recife, 17F));
        });
    }
}