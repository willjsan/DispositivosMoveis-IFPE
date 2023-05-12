package recife.ifpe.edu.airpower.ui.insertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class WizardTwoFragment extends Fragment {

    private static final String TAG = WizardTwoFragment.class.getSimpleName();
    private TextView mStatus;
    private EditText mSSID;
    private EditText mPassword;
    private Button mSubmit;

    public WizardTwoFragment() {
        // Required empty public constructor
    }

    public static WizardTwoFragment newInstance() {
        return new WizardTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wizard_two, container, false);
        mStatus = view.findViewById(R.id.text_device_insertion_wizard_two_status);
        mSSID = view.findViewById(R.id.edit_device_insertion_wizard_two_ssid);
        mPassword = view.findViewById(R.id.edit_device_insertion_wizard_two_pwrd);
        mSubmit = view.findViewById(R.id.button_device_insertion_wizard_two_submit);
        mSubmit.setOnClickListener(v -> {
            Fragment wizardThree = WizardThreeFragment.newInstance();
            openFragment(wizardThree);
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
}