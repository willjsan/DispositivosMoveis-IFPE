package recife.ifpe.edu.airpower.ui.deviceinsertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class WizardTwoFragment extends Fragment {

    private static final String TAG = WizardTwoFragment.class.getSimpleName();
    private final AirPowerDevice mDevice;
    private final String mAction;
    private TextView mStatus;
    private EditText mSSID;
    private EditText mPassword;
    private Button mSubmit;
    private ProgressDialog mProgressDialog;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            if (mDevice == null) {
                if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Instance device is null");
                return;
            }
            switch (what) {
                case AirPowerConstants.NETWORK_CONNECTION_SUCCESS:
                    mStatus.setText("Network Connected");
                    mSubmit.setText("Next");
                    mSubmit.setEnabled(true);
                    mProgressDialog.dismiss();
                    mSubmit.setOnClickListener(v1 -> {
                        mDevice.setDeviceSSID(mSSID.getText().toString());
                        mDevice.setDevicePassword(mPassword.getText().toString());
                        Fragment fragment = WizardThreeFragment
                                .newInstance(mDevice, AirPowerConstants.ACTION_REGISTER_DEVICE);
                        mFragmentUtil.openFragment(fragment, true);
                    });
                    break;

                case AirPowerConstants.NETWORK_CONNECTION_FAILURE:
                    mStatus.setText("Network NOT Connected");
                    mStatus.setTextColor(getResources().getColor(R.color.teal_700));
                    mSSID.setEnabled(true);
                    mPassword.setEnabled(true);
                    mSubmit.setEnabled(true);
                    mProgressDialog.dismiss();
                    break;

                case AirPowerConstants.EDIT_NETWORK_CONNECTION_SUCCESS:
                    mStatus.setText("Network Connected");
                    mSubmit.setText("Next");
                    mSubmit.setEnabled(true);
                    mProgressDialog.dismiss();
                    mSubmit.setOnClickListener(v1 -> {
                        mDevice.setDeviceSSID(mSSID.getText().toString());
                        mDevice.setDevicePassword(mPassword.getText().toString());
                        Fragment fragment = WizardThreeFragment
                                .newInstance(mDevice, AirPowerConstants.ACTION_EDIT_DEVICE);
                        mFragmentUtil.openFragment(fragment, true);
                    });
                    break;
            }
        }
    };

    public WizardTwoFragment() {
        this.mDevice = null;
        this.mAction = null;
    }

    private WizardTwoFragment(AirPowerDevice device, String action) {
        this.mDevice = device;
        this.mAction = action;
    }

    public static WizardTwoFragment newInstance() {
        return new WizardTwoFragment();
    }

    public static WizardTwoFragment newInstance(AirPowerDevice device, String action) {
        return new WizardTwoFragment(device, action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        mProgressDialog = AirPowerUtil.getProgressDialog(getContext(), "waiting");
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
            mFragmentUtil.openFragment(wizardThree, true);
        });

        if (mDevice == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Instance device is null");
            return view;
        }

        switch (mAction) {
            case AirPowerConstants.ACTION_NEW_DEVICE:
                mSubmit.setOnClickListener(v -> {
                    mStatus.setText("Connecting with Network...");
                    mStatus.setTextColor(getResources().getColor(R.color.purple_200));
                    mSSID.setEnabled(false);
                    mPassword.setEnabled(false);
                    mSubmit.setEnabled(false);
                    mProgressDialog.show();
                    new Thread(() -> {
                        // TODO this routine should be replaced by network connection routine
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(AirPowerConstants.NETWORK_CONNECTION_SUCCESS);
                    }).start();
                });
                break;

            case AirPowerConstants.ACTION_EDIT_DEVICE_:
                mSSID.setText(mDevice.getDeviceSSID());
                mPassword.setText("");
                mSubmit.setOnClickListener(v -> {
                    mStatus.setText("Connecting with Network...");
                    mStatus.setTextColor(getResources().getColor(R.color.purple_200));
                    mSSID.setEnabled(false);
                    mPassword.setEnabled(false);
                    mSubmit.setEnabled(false);
                    mProgressDialog.show();
                    new Thread(() -> {
                        // TODO this routine should be replaced by network connection routine
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(AirPowerConstants
                                .EDIT_NETWORK_CONNECTION_SUCCESS);
                    }).start();
                });
                break;
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }
}