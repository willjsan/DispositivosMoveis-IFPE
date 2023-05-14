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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.DeviceIconAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class WizardThreeFragment extends Fragment {

    private static final String TAG = WizardThreeFragment.class.getSimpleName();
    private final AirPowerDevice mDevice;
    private final int mAction;
    private TextView mStatus;
    private EditText mName;
    private Spinner mIcons;
    private Button mSubmit;
    private AirPowerRepository mRepo;

    public WizardThreeFragment() {
        this.mDevice = null;
        this.mAction = AirPowerConstants.ACTION_NONE;
    }

    private WizardThreeFragment(AirPowerDevice device, int action) {
        this.mDevice = device;
        this.mAction = action;
    }

    public static WizardThreeFragment newInstance() {
        return new WizardThreeFragment();
    }

    public static WizardThreeFragment newInstance(AirPowerDevice device, int action) {
        return new WizardThreeFragment(device, action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepo = AirPowerRepository.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wizard_three, container, false);

        mStatus = view.findViewById(R.id.text_device_insertion_wizard_three_status);
        mName = view.findViewById(R.id.edit_device_insertion_wizard_three_name);
        mIcons = view.findViewById(R.id.spinner_device_insertion_wizard_three_icon);
        mSubmit = view.findViewById(R.id.button_device_insertion_wizard_three_sumbit);

        if (mDevice == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Instance device is null");
            return view;
        }

        // Spinner setup
        mIcons.setAdapter(new DeviceIconAdapter(getContext()));
        mIcons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDevice.setIcon(DeviceIconAdapter.getIcons().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (mAction) {
            case AirPowerConstants.ACTION_NEW_DEVICE:
                mSubmit.setOnClickListener(v -> {
                    mDevice.setName(mName.getText().toString());
                    mRepo.insert(mDevice);
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
                break;

            case AirPowerConstants.ACTION_EDIT_DEVICE:
                mName.setText(mDevice.getName());
                mSubmit.setOnClickListener(v -> {
                    mDevice.setName(mName.getText().toString());
                    mRepo.update(mDevice);
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
                break;
        }
        return view;
    }
}