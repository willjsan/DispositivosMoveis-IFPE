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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.model.server.AirPowerAuthenticationManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;
    private ServersInterfaceWrapper.IAirPowerAuthManager authManager;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private Context mContext;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        getActivity().setTitle("Login");
        mContext = getContext();
        authManager = AirPowerAuthenticationManagerImpl.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findViewsById(view);
        setupListeners();
        return view;
    }

    private void setupListeners() {
        mLogin.setOnClickListener(view -> {
            String email = mEmail.getText().toString();
            String pass = mPassword.getText().toString();
            if (pass.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "You must provide E-mail and Password",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            AirPowerUser airPowerUser = new AirPowerUser();
            airPowerUser.setEmail(email);
            airPowerUser.setPassword(pass);
            authManager.signIn(airPowerUser, new ServersInterfaceWrapper.IUserAuthCallback() {
                @Override
                public void onSuccess(String msg) {
                    setSessionStatus(true);
                    startActivity(new Intent(getContext(), MainHolderActivity.class));
                    getActivity().finish();
                }

                @Override
                public void onFailure(String msg) {
                    setSessionStatus(false);
                    Toast.makeText(getContext(), "Login fail:" + msg,
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        mRegister.setOnClickListener(view -> {
            mFragmentUtil.openFragment(RegisterFragment.newInstance(), false);
        });
    }

    private void findViewsById(View view) {
        mEmail = view.findViewById(R.id.et_login_email);
        mPassword = view.findViewById(R.id.et_login_password);
        mLogin = view.findViewById(R.id.bt_login_login);
        mRegister = view.findViewById(R.id.bt_login_register);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }

    private void setSessionStatus(boolean isLoggedIn) {
        String timestamp = AirPowerUtil.getCurrentDateTime();
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(AuthActivity.PREF_USER_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AuthActivity.KEY_USER_STATE, isLoggedIn);
        editor.putString(AuthActivity.KEY_AUTH_TIMESTAMP, timestamp);
        editor.apply();
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "setSessionStatus:" + isLoggedIn + " timestamp:" + timestamp);
    }
}