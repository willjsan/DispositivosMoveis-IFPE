package recife.ifpe.edu.airpower.ui.insertionwizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;

public class DeviceInsertionWizardTwoActivity extends AppCompatActivity {

    private TextView mStatus;
    private EditText mSSID;
    private EditText mPassword;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_insertion_wizard_two);

        mStatus = findViewById(R.id.text_device_insertion_wizard_two_status);
        mSSID = findViewById(R.id.edit_device_insertion_wizard_two_ssid);
        mPassword = findViewById(R.id.edit_device_insertion_wizard_two_pwrd);
        mSubmit = findViewById(R.id.button_device_insertion_wizard_two_submit);
        mSubmit.setOnClickListener(v -> {
            Intent i = new Intent(DeviceInsertionWizardTwoActivity.this,
                    DeviceInsertionWizardThreeActivity.class);

            startActivity(i);
        });
    }
}