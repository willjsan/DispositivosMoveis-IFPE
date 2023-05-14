package recife.ifpe.edu.airpower.model.repo.model;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class DeviceMeasurement {
    private final String mDeviceToken;
    private final ArrayList<BarEntry> mMeasurementSet;

    public DeviceMeasurement(String mDeviceToken, ArrayList<BarEntry> measurementSet) {
        this.mDeviceToken = mDeviceToken;
        this.mMeasurementSet = measurementSet;
    }

    public ArrayList<BarEntry> getMeasurementSet(){
        return mMeasurementSet;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }
}