package recife.ifpe.edu.airpower.ui.main;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class MainHolderActivity extends AppCompatActivity {

    private static final String TAG = MainHolderActivity.class.getSimpleName();
    private NavController navController;
    private Fragment mainFragmentHolder;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_holder);
        setUpBottomNavigationView();
        showUpMainFragment();

    }

    private void showUpMainFragment() {
        Fragment homeFragment = HomeFragment.newInstance();
        openFragment(homeFragment);
    }

    private void setUpBottomNavigationView() {
        navigationView = findViewById(R.id.main_bottom_nav);
        navigationView.setOnItemSelectedListener(item -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.e(TAG, "setUpBottomNavigationView fail: ActionBar is null");
                return false;
            }
            switch (item.getItemId()) {
                case R.id.b1:
                    actionBar.setTitle("Home");
                    Fragment homeFragment = HomeFragment.newInstance();
                    openFragment(homeFragment);
                    break;
                case R.id.b2:
                    actionBar.setTitle("My Devices");
                    Fragment myDevicesFragment = MyDevicesFragment.newInstance();
                    openFragment(myDevicesFragment);
                    break;
                case R.id.b3:
                    actionBar.setTitle("Profile");
                    Fragment profile = ProfileFragment.newInstance();
                    openFragment(profile);
                    break;
            }
            return true;
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}