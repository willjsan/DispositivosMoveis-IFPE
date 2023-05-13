package recife.ifpe.edu.airpower.ui.insertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceSetupWizardHolderActivity extends AppCompatActivity
        implements WizardOneFragment.WizardOneListener {

    private static final String TAG = DeviceSetupWizardHolderActivity.class.getSimpleName();
    private AirPowerRepository mRepo;
    private AirPowerDevice mDevice= null;
    private boolean mIsDeviceEditRoutine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_wizard_holder);

        // Retrieve repo instance
        mRepo = AirPowerRepository.getInstance(getApplicationContext());

        // Check if it is device editable routine
        Intent intent = getIntent();
        mIsDeviceEditRoutine =
                intent.getBooleanExtra(AirPowerConstants.KEY_EDIT_DEVICE, false);
        if (mIsDeviceEditRoutine) {
            mDevice = mRepo.getDeviceById(intent
                    .getIntExtra(AirPowerConstants.KEY_DEVICE_ID, -1));
            if (mDevice == null) {
                if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Device is null, cancelling");
                return;
            }
            setTitle("Edit Device");
            Fragment editFragment = WizardTwoFragment.newInstance(mDevice);
            openFragment(editFragment);
            return;
        }

        Fragment wizard = WizardOneFragment.newInstance();
        openFragment(wizard);
    }

    private void openFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.device_wizard_fragment_holder, fragment);
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }

    @Override
    public void onDone(String deviceIPAddress) {
        AirPowerDevice airPowerDevice = new AirPowerDevice();
        airPowerDevice.setName(deviceIPAddress);
        airPowerDevice.setDescription(deviceIPAddress);
        airPowerDevice.setIcon("air_conditioner_icon");
        mRepo.insert(airPowerDevice);
    }
}