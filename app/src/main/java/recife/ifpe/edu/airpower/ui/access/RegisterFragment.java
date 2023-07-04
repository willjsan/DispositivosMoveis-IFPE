package recife.ifpe.edu.airpower.ui.access;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.model.server.AirPowerAuthenticationManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class RegisterFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConfirmation;
    private Button mRegister;
    private final int USER_VALIDATION_OK = 0;
    private final int USER_VALIDATION_PASSWORD_MISMATCH = 1;
    private final int USER_VALIDATION_EMPTY_FIELD = 2;
    private ServersInterfaceWrapper.IAirPowerAuthManager authManager;
    private AirPowerRepository mRepo;

    public RegisterFragment() {
    }


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        authManager = AirPowerAuthenticationManagerImpl.getInstance();
        mRepo = AirPowerRepository.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findViewsById(view);
        setupListeners();
        return view;
    }

    private void setupListeners() {
        mRegister.setOnClickListener(view -> {
            handleUserRegistration();
        });
    }

    private void handleUserRegistration() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "handleUserRegistration");
        AirPowerUser newUser = new AirPowerUser();
        newUser.setName(mName.getText().toString());
        newUser.setEmail(mEmail.getText().toString());
        newUser.setPassword(mPassword.getText().toString());
        newUser.setPasswordConfirmation(mPasswordConfirmation.getText().toString());

        switch (getNewUserValidationCode(newUser)) {
            case USER_VALIDATION_EMPTY_FIELD:
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_LONG)
                        .show();
                mPassword.setText("");
                mPasswordConfirmation.setText("");
                break;
            case USER_VALIDATION_PASSWORD_MISMATCH:
                Toast.makeText(getContext(), "Password mismatch", Toast.LENGTH_LONG)
                        .show();
                mPassword.setText("");
                mPasswordConfirmation.setText("");
                break;
            case USER_VALIDATION_OK:
                registerUser(newUser);
                break;
        }
    }

    private void registerUser(AirPowerUser newUser) {
        authManager.register(newUser, new ServersInterfaceWrapper.IUserAuthCallback() {
            @Override
            public void onSuccess(String msg) {
                mRepo.setSessionStatus(true);
                Intent intent = new Intent(getContext(), MainHolderActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getNewUserValidationCode(AirPowerUser user) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "getNewUserValidationCode");
        ArrayList<String> userFields = new ArrayList<>(Arrays.asList(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPasswordConfirmation()
        ));
        for (String field: userFields) {
            if (field == null || field.isEmpty())
                return USER_VALIDATION_EMPTY_FIELD;
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())){
            return USER_VALIDATION_PASSWORD_MISMATCH;
        }
        return USER_VALIDATION_OK;
    }

    private void findViewsById(View view) {
        mName = view.findViewById(R.id.ed_register_name);
        mEmail = view.findViewById(R.id.et_register_email);
        mPassword = view.findViewById(R.id.tv_register_password);
        mPasswordConfirmation = view.findViewById(R.id.et_register_password_confirmation);
        mRegister = view.findViewById(R.id.bt_register_confirm);
    }
}