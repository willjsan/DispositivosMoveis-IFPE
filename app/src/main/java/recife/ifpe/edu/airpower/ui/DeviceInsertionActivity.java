package recife.ifpe.edu.airpower.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

public class DeviceInsertionActivity extends AppCompatActivity {

    private EditText mDeviceName;
    private EditText mDeviceDescription;
    private Button mLocalizationSet;
    private Button mLocalizationReset;
    private Button mDeviceSave;
    private Button mDeviceDiscard;
    private AirPowerRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_insertion);

        mDeviceName = findViewById(R.id.text_device_insertion_info_name_edit);
        mDeviceDescription = findViewById(R.id.text_device_insertion_info_description_edit);
        mLocalizationSet = findViewById(R.id.button_device_insertion_localization_set);
        mLocalizationReset = findViewById(R.id.button_device_insertion_localization_reset);
        mDeviceSave = findViewById(R.id.button_device_insertion_menu_save);
        mDeviceDiscard = findViewById(R.id.button_device_insertion_menu_discard);

        mRepo = AirPowerRepository.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDeviceSave.setOnClickListener(v -> {
            AirPowerDevice device = new AirPowerDevice(
                    mDeviceName.getText().toString(),
                    mDeviceDescription.getText().toString(),
                    "ainda nao é assim", "icone"
            );

            mRepo.insert(device);
            String name = mRepo.getDevices().get(0).getName();
            int size = mRepo.getDevices().size();
            Toast.makeText(this, name + " " + size + " itens salvos ", Toast.LENGTH_LONG).show();
        });

//        Intent intent = getIntent();
//        String teste = intent.getStringExtra("teste");
//        if (teste.equals("teste")) {
//            AirPowerDevice device = mRepo.getDevices().get(1);
//            mDeviceName.setText(device.getName());
//        }
    }
}