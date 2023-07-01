package recife.ifpe.edu.airpower.model.adapter;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.device.DeviceMeasurement;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class ChartAdapter {
    private final String TAG = ChartAdapter.class.getSimpleName();

    private ChartAdapter(View viewGroup,
                         List<DeviceMeasurement> measurements,
                         String description) {
        if (description == null || description.isEmpty())
            description = "Daily Consumption";
        try {
            BarChart barChart = (BarChart) viewGroup;

            ArrayList<BarEntry> barEntry = new ArrayList<>();

            for (DeviceMeasurement m : measurements) {
                barEntry.add(new BarEntry(m.getX(), m.getY()));
            }

            BarDataSet dataSet = new BarDataSet(barEntry, "kWh/day");
            dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(0);
            BarData barData = new BarData(dataSet);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setText(description);
            barChart.animateY(2000);

        } catch (Exception e) {
            AirPowerLog.e(TAG, "Fail while building single chart \n" +
                    e.getMessage());
        }
    }


    public static class Builder {
        private final View sViewGroup;
        private final List<DeviceMeasurement> sMeasurements;
        private String sDescription;

        public Builder(@NonNull View viewGroup,
                       @NonNull List<DeviceMeasurement> measurements) {
            this.sViewGroup = viewGroup;
            this.sMeasurements = measurements;
        }

        public Builder setDescription(String description) {
            sDescription = description;
            return this;
        }

        public ChartAdapter build() {
            return new ChartAdapter(sViewGroup, sMeasurements,
                    sDescription);
        }
    }
}