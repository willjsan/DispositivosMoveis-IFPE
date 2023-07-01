package recife.ifpe.edu.airpower.ui.main;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.server.AirPowerAuthenticationManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.access.AuthActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private Button mLogOof;
    private ServersInterfaceWrapper.IAirPowerAuthManager authManager;
    private AirPowerRepository mRepo;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        mRepo = AirPowerRepository.getInstance(getContext());
        authManager = AirPowerAuthenticationManagerImpl.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewsById(view);
        setUpListeners();
        return view;
    }

    private void setUpListeners() {
        mLogOof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authManager.signOut(new ServersInterfaceWrapper.IUserAuthCallback() {
                    @Override
                    public void onSuccess(String msg) {
                        mRepo.setSessionStatus(false);
                        Intent intent = new Intent(getContext(), AuthActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                });
            }
        });
    }

    private void findViewsById(View view) {
        mLogOof = view.findViewById(R.id.bt_profile_logoff);
    }
}