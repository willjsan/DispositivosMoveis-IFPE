package recife.ifpe.edu.airpower.ui.access;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.server.AirPowerAuthenticationManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class AuthActivity extends AppCompatActivity implements UIInterfaceWrapper.INavigate,
        UIInterfaceWrapper.FragmentUtil {

    private String TAG = AuthActivity.class.getSimpleName();
    private boolean mCanBackPress = true;
    static final String PREF_USER_STATE = "airpowerapp.prefuserstate";
    static final String KEY_USER_STATE = "isUserLoggedIn";
    static final String KEY_AUTH_TIMESTAMP = "authTimestamp";
    private ServersInterfaceWrapper.IAirPowerAuthManager authManager;

    private AirPowerRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        authManager = AirPowerAuthenticationManagerImpl.getInstance();
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        if (mRepo.isUserLoggedIn() && !isSessionExpired()) {
            // USER IS LOGGED IN AND SESSION IS NOT EXPIRED
            startActivity(new Intent(AuthActivity.this, MainHolderActivity.class));
            finish();
        } else {
            authManager.signOut(new ServersInterfaceWrapper.IUserAuthCallback() {
                @Override
                public void onSuccess(String msg) {
                    openFragment(LoginFragment.newInstance(), false);
                }

                @Override
                public void onFailure(String msg) {
                    if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure FIX ME");
                    // TODO think how to handle this scenario
                    openFragment(LoginFragment.newInstance(), false);
                }
            });

        }
    }

    private boolean isSessionExpired() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "isSessionExpired");
        final long SEVEN_DAYS_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;
        final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
        final long EXP_TIME_MILLIS = 5 * 60 * 1000; // 5 min
        SharedPreferences sharedPreferences =
                getSharedPreferences(PREF_USER_STATE, Context.MODE_PRIVATE);
        String storedDateTime = sharedPreferences.getString(KEY_AUTH_TIMESTAMP, "");
        if (storedDateTime.isEmpty()) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.w(TAG, "storedDateTime is null");
            return true;
        }
        try {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date storedDate = dateFormat.parse(storedDateTime);
            long storedTimestamp = storedDate.getTime();
            long currentTimestamp = System.currentTimeMillis();
            long differenceInMillis = currentTimestamp - storedTimestamp;
            boolean result = differenceInMillis > EXP_TIME_MILLIS;
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.d(TAG, "isExpired:" + result);
            return result;
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "fail to get time");
            return true;
        }
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.access_fragment_holder, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }

    @Override
    public void setBackPress(boolean canBackPress) {
        mCanBackPress = canBackPress;
    }
}