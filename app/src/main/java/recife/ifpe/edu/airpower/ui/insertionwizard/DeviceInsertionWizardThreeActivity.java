package recife.ifpe.edu.airpower.ui.insertionwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import recife.ifpe.edu.airpower.R;

public class DeviceInsertionWizardThreeActivity extends AppCompatActivity {

    private TextView mStatus;
    private EditText mName;
    private Spinner mIcons;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_insertion_wizard_three);

        mStatus = findViewById(R.id.text_device_insertion_wizard_three_status);
        mName = findViewById(R.id.edit_device_insertion_wizard_three_name);
        mIcons = findViewById(R.id.spinner_device_insertion_wizard_three_icon);
        mSubmit = findViewById(R.id.button_device_insertion_wizard_three_sumbit);

        mSubmit.setOnClickListener(v -> {

        });
    }
}