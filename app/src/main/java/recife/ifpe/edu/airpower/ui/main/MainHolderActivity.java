package recife.ifpe.edu.airpower.ui.main;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.ui.DeviceDetailActivity;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class MainHolderActivity extends AppCompatActivity
        implements UIInterfaceWrapper.FragmentUtil {

    private static final String TAG = MainHolderActivity.class.getSimpleName();
    private BottomNavigationView mNavigationView;
    private int lastFragmentOpened = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        setContentView(R.layout.activity_main_holder);
        setUpBottomNavigationView();
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null &&
                    action.equals(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES)) {
                mNavigationView.setSelectedItemId(R.id.b2);
            } else {
                setTitle("Home");
                openFragment(HomeFragment.newInstance(), false);
                lastFragmentOpened = 1;
            }
        }
    }


    private void setUpBottomNavigationView() {
        mNavigationView = findViewById(R.id.main_bottom_nav);
        mNavigationView.setOnItemSelectedListener(item -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.e(TAG, "setUpBottomNavigationView fail: ActionBar is null");
                return false;
            }
            switch (item.getItemId()) {
                case R.id.b1:
                    if (lastFragmentOpened == 1) break;
                    actionBar.setTitle("Home");
                    Fragment homeFragment = HomeFragment.newInstance();
                    openFragment(homeFragment, false);
                    lastFragmentOpened = 1;
                    break;
                case R.id.b2:
                    if (lastFragmentOpened == 2) break;
                    actionBar.setTitle("My Devices");
                    Fragment myDevicesFragment = MyDevicesFragment.newInstance();
                    openFragment(myDevicesFragment, false);
                    lastFragmentOpened = 2;
                    break;
                case R.id.b3:
                    if (lastFragmentOpened == 3) break;
                    actionBar.setTitle("Profile");
                    Fragment profile = ProfileFragment.newInstance();
                    openFragment(profile, false);
                    lastFragmentOpened = 3;
                    break;
            }
            return true;
        });
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_holder, fragment);
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