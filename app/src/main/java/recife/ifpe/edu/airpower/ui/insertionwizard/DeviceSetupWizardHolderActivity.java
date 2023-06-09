package recife.ifpe.edu.airpower.ui.insertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.DeviceDetailActivity;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class DeviceSetupWizardHolderActivity extends AppCompatActivity
        implements WizardThreeFragment.INavigate,
        UIInterfaceWrapper.FragmentUtil {

    private static final String TAG = DeviceSetupWizardHolderActivity.class.getSimpleName();
    private AirPowerRepository mRepo;
    private AirPowerDevice mDevice = null;
    private boolean mCanBackPress = true;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_wizard_holder);
        Intent intent = getIntent();
        if (intent == null) return;
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        mAction = intent.getAction();
        switch (mAction) {
            case AirPowerConstants.ACTION_EDIT_DEVICE_:
                actionEditDevice(intent);
                break;

            case AirPowerConstants.ACTION_NEW_DEVICE:
                actionNewDevice();
                break;

            default:
                if (AirPowerLog.ISLOGABLE) {
                    AirPowerLog.e(TAG, "missing action");
                }

        }
    }

    private void actionNewDevice() {
        if (AirPowerLog.ISLOGABLE) {
            AirPowerLog.d(TAG, "actionNewDevice");
        }
        Fragment wizard = WizardOneFragment.newInstance();
        this.openFragment(wizard, false);
    }

    private void actionEditDevice(Intent intent) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "actionEditDevice");
        int deviceId = intent.getIntExtra(AirPowerConstants.KEY_DEVICE_ID,
                AirPowerConstants.INVALID_DEVICE_ID);
        mDevice = mRepo.getDeviceById(deviceId);
        if (mDevice == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Device is null, cancelling");
            return;
        }
        setTitle("Edit Device");
        Fragment editFragment = WizardTwoFragment
                .newInstance(mDevice, AirPowerConstants.ACTION_EDIT_DEVICE_);
        this.openFragment(editFragment, false);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.device_wizard_fragment_holder);
        if (fragment instanceof WizardThreeFragment) {
            if (!mCanBackPress) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.d(TAG, "CanBackPress: false");
                return;
            }
        } else if (fragment instanceof WizardOneFragment) {
            launchMainActivity();
        } else if (fragment instanceof WizardTwoFragment) {
            if (mAction != null && mAction.equals(AirPowerConstants.ACTION_EDIT_DEVICE_)) {
                launchDeviceDetailActivity();
            }
        }
        getSupportFragmentManager().popBackStack();
    }

    private void launchDeviceDetailActivity() {
        Intent intent = new Intent(DeviceSetupWizardHolderActivity.this,
                DeviceDetailActivity.class);
        intent.setAction(AirPowerConstants.ACTION_LAUNCH_DETAIL);
        intent.putExtra(AirPowerConstants.KEY_DEVICE_ID, mDevice.getId());
        startActivity(intent);
        finish();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(DeviceSetupWizardHolderActivity.this,
                MainHolderActivity.class);
        intent.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
        startActivity(intent);
        finish();
    }

    @Override
    public void setBackPress(boolean canBackPress) {
        mCanBackPress = canBackPress;
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.device_wizard_fragment_holder, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }
}