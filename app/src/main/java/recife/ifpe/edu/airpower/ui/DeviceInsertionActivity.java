package recife.ifpe.edu.airpower.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

public class DeviceInsertionActivity extends AppCompatActivity {

    private EditText mDeviceName;
    private EditText mDeviceDescription;
    private Button mButtonLocalizationSet;
    private Button mButtonLocalizationReset;
    private Button mButtonSave;
    private Button mButtonDiscard;
    private AirPowerRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_insertion);

        mRepo = AirPowerRepository.getInstance(getApplicationContext());

        mDeviceName = findViewById(R.id.text_device_insertion_info_name_edit);
        mDeviceDescription = findViewById(R.id.text_device_insertion_info_description_edit);
        mButtonLocalizationSet = findViewById(R.id.button_device_insertion_localization_set);
        mButtonLocalizationReset = findViewById(R.id.button_device_insertion_localization_reset);
        mButtonSave = findViewById(R.id.button_device_insertion_menu_save); // TODO mudar botão na view
        mButtonDiscard = findViewById(R.id.button_device_insertion_menu_discard); // TODO mudar botão na view
    }

    @Override
    protected void onStart() {
        super.onStart();

        mButtonSave.setOnClickListener(v -> {
            AirPowerDevice device = new AirPowerDevice(
                    mDeviceName.getText().toString(),
                    mDeviceDescription.getText().toString(),
                    "1111,11111", "air_conditioner_icon", "token"
            );
            mRepo.insert(device);
        });
    }
}