package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.AirPowerDeviceDAO;
import recife.ifpe.edu.airpower.model.adapter.MainActivityItemAdapter;

public class MainActivity extends AppCompatActivity {

    Button mButtonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonHome = findViewById(R.id.button_menu_home);

        ListView mListView = findViewById(R.id.list_items);
        mListView.setAdapter(
                new MainActivityItemAdapter(new AirPowerDeviceDAO().getDevices(), this));


        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DeviceDetailActivity.class);
                startActivity(i);
            }
        });
    }
}