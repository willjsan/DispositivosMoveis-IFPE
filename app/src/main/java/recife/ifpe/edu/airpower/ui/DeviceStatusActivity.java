package recife.ifpe.edu.airpower.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;

public class DeviceStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_status_actividy);
        setTitle("Device Status");
    }
}