package recife.ifpe.edu.airpower.ui.insertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.DeviceIconAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.server.ServerInterfaceWrapper;
import recife.ifpe.edu.airpower.model.server.ServerManagerImpl;
import recife.ifpe.edu.airpower.ui.main.MainHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class WizardThreeFragment extends Fragment {

    private static final String TAG = WizardThreeFragment.class.getSimpleName();
    private final AirPowerDevice mDevice;
    private final int mAction;
    private TextView mStatus;
    private EditText mName;
    private Spinner mIcons;
    private Button mSubmitButton;
    private Context mContext;
    private AirPowerRepository mRepo;
    private ServerInterfaceWrapper.IServerManager mServerManager;
    private INavigate mNavigateBackPress;
    private ProgressDialog mProgressDialog;

    public interface INavigate {
        void setBackPress(boolean canBackPress);
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            if (mDevice == null) {
                if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Instance device is null");
                return;
            }
            switch (what) {
                case AirPowerConstants.NETWORK_CONNECTION_SUCCESS:
                    mStatus.setText("Done");
                    mName.setEnabled(false);
                    mIcons.setEnabled(false);
                    mSubmitButton.setEnabled(true);
                    mSubmitButton.setText("Finish");
                    mNavigateBackPress.setBackPress(false);
                    mProgressDialog.dismiss();
                    mSubmitButton.setOnClickListener(view -> {
                        Intent i = new Intent(getContext(), MainHolderActivity.class);
                        i.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
                        startActivity(i);
                        try {
                            getActivity().finish();
                        } catch (NullPointerException e) {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.e(TAG, "Fail when getting Activity");
                        }
                    });
                    break;

                case AirPowerConstants.NETWORK_CONNECTION_FAILURE:
                    mStatus.setText("Couldn't register device on server");
                    mSubmitButton.setText("Close");
                    mNavigateBackPress.setBackPress(true);
                    mProgressDialog.dismiss();
                    mSubmitButton.setOnClickListener(view -> {
                        try {
                            Intent i = new Intent(getContext(), MainHolderActivity.class);
                            i.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
                            startActivity(i);
                            getActivity().finish();
                        } catch (NullPointerException e) {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.e(TAG, "Fail when getting Activity");
                        }
                    });
                    break;
            }
        }
    };

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof INavigate) {
            mNavigateBackPress = (INavigate) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mRepo = AirPowerRepository.getInstance(mContext);
        mServerManager = ServerManagerImpl.getInstance();
        mProgressDialog = AirPowerUtil.getProgressDialog(getContext(), "waiting");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizard_three, container, false);
        findViewsById(view);
        if (mDevice == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Instance device is null");
            return view;
        }
        iconSpinnerSetup();
        switch (mAction) {
            case AirPowerConstants.ACTION_REGISTER_DEVICE:
                performRegisterDeviceAction();
                break;

            case AirPowerConstants.ACTION_EDIT_DEVICE:
                performEditDeviceAction();
                break;
        }
        return view;
    }

    private void iconSpinnerSetup() {
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
    }

    private void findViewsById(View view) {
        mStatus = view.findViewById(R.id.text_device_insertion_wizard_three_status);
        mName = view.findViewById(R.id.edit_device_insertion_wizard_three_name);
        mIcons = view.findViewById(R.id.spinner_device_insertion_wizard_three_icon);
        mSubmitButton = view.findViewById(R.id.button_device_insertion_wizard_three_sumbit);
    }

    /**
     * This method performs the edition of
     * device info on data base. This action
     * is independent of server communication
     * because the type of info that can be
     * edited is not relevant for server.
     */
    private void performEditDeviceAction() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "Action edit device");
        mName.setText(mDevice.getName());
        mSubmitButton.setOnClickListener(v -> {
            mDevice.setName(mName.getText().toString());
            mRepo.update(mDevice);
            Intent intent = new Intent(getContext(), MainHolderActivity.class);
            intent.setAction(AirPowerConstants.ACTION_LAUNCH_MY_DEVICES);
            startActivity(intent);
            try {
                getActivity().finish();
            } catch (NullPointerException e) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.e(TAG, "Fail when getting Activity");
            }
        });
    }

    /**
     * This method performs the device registry
     * on server
     */
    private void performRegisterDeviceAction() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "Action new device");
        mSubmitButton.setOnClickListener(v -> {
            mDevice.setName(mName.getText().toString());
            mStatus.setText("Registering device on Server");
            mStatus.setTextColor(getResources().getColor(R.color.purple_200));
            mName.setEnabled(false);
            mIcons.setEnabled(false);
            mSubmitButton.setEnabled(false);
            mProgressDialog.show();
            mServerManager.registerDevice(mDevice,
                    new ServerInterfaceWrapper.RegisterCallback() {
                        @Override
                        public void onResult(AirPowerDevice device) {
                            mRepo.insert(device);
                            mHandler.sendEmptyMessage(AirPowerConstants.NETWORK_CONNECTION_SUCCESS);
                        }

                        @Override
                        public void onFailure(String message) {
                            mHandler.sendEmptyMessage(AirPowerConstants.NETWORK_CONNECTION_FAILURE);
                        }
                    });
        });
    }
}