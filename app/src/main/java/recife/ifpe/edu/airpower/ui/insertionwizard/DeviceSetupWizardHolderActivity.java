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
    private int mIntExtra;
    private boolean mCanBackPress = true;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup_wizard_holder);
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        Intent intent = getIntent();
        mIntExtra = intent.getIntExtra(AirPowerConstants.KEY_ACTION, AirPowerConstants.ACTION_NONE);
        mAction = intent.getAction();
        switch (mIntExtra) {
            case AirPowerConstants.ACTION_EDIT_DEVICE:
                mDevice = mRepo.getDeviceById(intent
                        .getIntExtra(AirPowerConstants.KEY_DEVICE_ID, -1));
                if (mDevice == null) {
                    if (AirPowerLog.ISLOGABLE)
                        AirPowerLog.e(TAG, "Device is null, cancelling");
                    return;
                }
                setTitle("Edit Device");
                Fragment editFragment = WizardTwoFragment
                        .newInstance(mDevice, AirPowerConstants.ACTION_EDIT_DEVICE);
                openFragment(editFragment, false);
                break;

            case AirPowerConstants.ACTION_NONE:
                Fragment wizard = WizardOneFragment.newInstance();
                openFragment(wizard, false);
                break;
        }
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
            if(mAction != null && mAction.equals(AirPowerConstants.ACTION_EDIT_DEVICE_) ) {
                launchMainActivity();
            }
        }
        getSupportFragmentManager().popBackStack();
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
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }
}