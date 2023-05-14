package recife.ifpe.edu.airpower.model.server;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;

public class AirPowerServerDummy {

    public DeviceMeasurement getMeasurementByDeviceToken(String deviceToken) {
        ArrayList<BarEntry> measurements = new ArrayList<>();
        measurements.add(new BarEntry(1,0));
        measurements.add(new BarEntry(2,0));
        measurements.add(new BarEntry(3,0));
        measurements.add(new BarEntry(4,400));
        measurements.add(new BarEntry(5,500));
        measurements.add(new BarEntry(6,600));
        measurements.add(new BarEntry(7,700));
        measurements.add(new BarEntry(8,700));
        measurements.add(new BarEntry(9,700));
        measurements.add(new BarEntry(10,502));
        measurements.add(new BarEntry(11,300));
        measurements.add(new BarEntry(12,400));
        measurements.add(new BarEntry(13,100));
        measurements.add(new BarEntry(14,800));
        measurements.add(new BarEntry(15,700));
        measurements.add(new BarEntry(16,800));
        measurements.add(new BarEntry(17,600));
        measurements.add(new BarEntry(18,700));
        measurements.add(new BarEntry(19,700));
        measurements.add(new BarEntry(20,200));
        measurements.add(new BarEntry(21,700));
        measurements.add(new BarEntry(22,800));
        measurements.add(new BarEntry(23,300));
        measurements.add(new BarEntry(24,700));
        measurements.add(new BarEntry(25,500));
        measurements.add(new BarEntry(26,700));
        measurements.add(new BarEntry(27,700));
        measurements.add(new BarEntry(28,900));
        measurements.add(new BarEntry(29,700));
        measurements.add(new BarEntry(30,600));
        measurements.add(new BarEntry(31,700));
        return new DeviceMeasurement(deviceToken, measurements);
    }
}
