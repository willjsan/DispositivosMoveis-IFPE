package recife.ifpe.edu.airpower.ui.insertionwizard;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class WizardThreeFragment extends Fragment {

    private static final String TAG = WizardThreeFragment.class.getSimpleName();
    private TextView mStatus;
    private EditText mName;
    private Spinner mIcons;
    private Button mSubmit;

    public WizardThreeFragment() {
        // Required empty public constructor
    }

    public static WizardThreeFragment newInstance() {
        WizardThreeFragment fragment = new WizardThreeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wizard_three, container, false);

        mStatus = view.findViewById(R.id.text_device_insertion_wizard_three_status);
        mName = view.findViewById(R.id.edit_device_insertion_wizard_three_name);
        mIcons = view.findViewById(R.id.spinner_device_insertion_wizard_three_icon);
        mSubmit = view.findViewById(R.id.button_device_insertion_wizard_three_sumbit);

        mSubmit.setOnClickListener(v -> {
            // TODO -- ensure fragments were removed from sack
            Intent i = new Intent(getContext(), MainHolderActivity.class);
            startActivity(i);
            try {
                getActivity().finish();
            } catch (NullPointerException e) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.e(TAG, "Fail when getting Activity");
            }
        });

        return view;
    }
}