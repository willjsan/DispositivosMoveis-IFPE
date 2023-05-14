package recife.ifpe.edu.airpower.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import recife.ifpe.edu.airpower.R;

public class ChartTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);

        BarChart chart = findViewById(R.id.my_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,100));
        entries.add(new BarEntry(2,200));
        entries.add(new BarEntry(3,300));
        entries.add(new BarEntry(4,400));
        entries.add(new BarEntry(5,500));
        entries.add(new BarEntry(6,600));
        entries.add(new BarEntry(7,700));
        entries.add(new BarEntry(8,700));
        entries.add(new BarEntry(9,700));
        entries.add(new BarEntry(10,502));
        entries.add(new BarEntry(11,300));
        entries.add(new BarEntry(12,400));
        entries.add(new BarEntry(13,100));
        entries.add(new BarEntry(14,800));
        entries.add(new BarEntry(15,700));
        entries.add(new BarEntry(16,800));
        entries.add(new BarEntry(17,600));
        entries.add(new BarEntry(18,700));
        entries.add(new BarEntry(19,700));
        entries.add(new BarEntry(20,200));
        entries.add(new BarEntry(21,700));
        entries.add(new BarEntry(22,800));
        entries.add(new BarEntry(23,300));
        entries.add(new BarEntry(24,700));
        entries.add(new BarEntry(25,500));
        entries.add(new BarEntry(26,700));
        entries.add(new BarEntry(27,700));
        entries.add(new BarEntry(28,900));
        entries.add(new BarEntry(29,700));
        entries.add(new BarEntry(30,600));
        entries.add(new BarEntry(31,700));

        BarDataSet dataSet = new BarDataSet(entries,"entradas");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(0);

        BarData barChart = new BarData(dataSet);

        chart.setFitBars(true);
        chart.setData(barChart);
        chart.getDescription().setText("example");
        chart.animateY(2000);
    }
}