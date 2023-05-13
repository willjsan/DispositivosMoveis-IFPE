package recife.ifpe.edu.airpower.ui.insertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class WizardOneFragment extends Fragment {

    private static final String TAG = WizardOneFragment.class.getSimpleName();
    private TextView mStatus;
    private TextView mAddressLabel;
    private EditText mAddress;
    private Button mConnect;
    private WizardOneListener mWizardOneListener;

    public WizardOneFragment() {
        // Required empty public constructor
    }

    public static WizardOneFragment newInstance() {
        WizardOneFragment fragment = new WizardOneFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wizard_one, container, false);

        mStatus = view.findViewById(R.id.text_device_insertion_wizard_status);
        mAddressLabel = view.findViewById(R.id.text_device_insertion_wizard_address_label);
        mAddress = view.findViewById(R.id.edit_device_insertion_wizard_address);
        mConnect = view.findViewById(R.id.button_device_insertion_wizard_connect);

        mConnect.setOnClickListener(v -> {
            mWizardOneListener.onDone(mAddress.getText().toString());
            Fragment wizardTwo = WizardTwoFragment.newInstance();
            openFragment(wizardTwo);
        });


        return view;
    }

    private void openFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction =
                    getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.device_wizard_fragment_holder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mWizardOneListener = (WizardOneListener) context;
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Can't get listener implementation");
        }
    }

    interface WizardOneListener {
        void onDone(String deviceIPAddress);
    }
}