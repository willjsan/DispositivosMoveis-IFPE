package recife.ifpe.edu.airpower.ui.insertionwizard;

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
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class WizardOneFragment extends Fragment {

    private static final String TAG = WizardOneFragment.class.getSimpleName();
    private TextView mStatus;
    private EditText mDeviceAddress;
    private Button mButtonConnect;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private ProgressDialog mProgressDialog;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case AirPowerConstants.DEVICE_CONNECTION_SUCCESS:
                    // Change the Button behavior
                    mStatus.setText("Device Connected");
                    mButtonConnect.setText("Next");
                    mButtonConnect.setEnabled(true);
                    mProgressDialog.dismiss();
                    mButtonConnect.setOnClickListener(v1 -> {
                        AirPowerDevice newDevice = new AirPowerDevice();
                        newDevice.setDeviceURL(mDeviceAddress.getText().toString());
                        Fragment wizardTwo = WizardTwoFragment
                                .newInstance(newDevice, AirPowerConstants.ACTION_NEW_DEVICE);
                        //openFragment(wizardTwo);
                        mFragmentUtil.openFragment(wizardTwo, true);
                    });
                    break;

                case AirPowerConstants.DEVICE_CONNECTION_FAIL:
                    mStatus.setText("Device NOT Connected");
                    mStatus.setTextColor(getResources().getColor(R.color.teal_700));
                    mDeviceAddress.setEnabled(true);
                    mButtonConnect.setEnabled(true);
                    mProgressDialog.dismiss();
                    break;
            }
        }
    };

    public WizardOneFragment() {
    }

    public static WizardOneFragment newInstance() {
        return new WizardOneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = AirPowerUtil.getProgressDialog(getContext(),"waiting");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wizard_one, container, false);

        mStatus = view.findViewById(R.id.text_device_insertion_wizard_status);
        mDeviceAddress = view.findViewById(R.id.edit_device_insertion_wizard_address);
        mButtonConnect = view.findViewById(R.id.button_device_insertion_wizard_connect);

        mButtonConnect.setOnClickListener(v -> {
            mStatus.setText("Connecting with Device...");
            mStatus.setTextColor(getResources().getColor(R.color.purple_200));
            mDeviceAddress.setEnabled(false);
            mButtonConnect.setEnabled(false);
            mProgressDialog.show();
            new Thread(() -> {
                // TODO this routine should be replaced by URLHTTPSConnection routine
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(AirPowerConstants.DEVICE_CONNECTION_SUCCESS);
            }).start();
        });
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