package recife.ifpe.edu.airpower.ui.insertionwizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;

public class DeviceInsertionWizardOneActivity extends AppCompatActivity {

    private TextView mStatus;
    private TextView mAddressLabel;
    private EditText mAddress;
    private Button mConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_insertion_wizard_one);

        mStatus = findViewById(R.id.text_device_insertion_wizard_status);
        mAddressLabel = findViewById(R.id.text_device_insertion_wizard_address_label);
        mAddress = findViewById(R.id.edit_device_insertion_wizard_address);
        mConnect = findViewById(R.id.button_device_insertion_wizard_connect);


        mConnect.setOnClickListener(v -> {
            Intent i = new Intent(DeviceInsertionWizardOneActivity.this,
                    DeviceInsertionWizardTwoActivity.class);
            startActivity(i);
        });
    }
}