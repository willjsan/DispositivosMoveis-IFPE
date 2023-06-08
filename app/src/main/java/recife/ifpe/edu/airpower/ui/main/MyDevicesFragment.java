package recife.ifpe.edu.airpower.ui.main;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.MainActivityItemAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.DeviceDetailActivity;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.insertionwizard.DeviceSetupWizardHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class MyDevicesFragment extends Fragment {

    private static final String TAG = MyDevicesFragment.class.getSimpleName();
    private FloatingActionButton mFloatingActionButton;
    private ListView listView;
    private List<AirPowerDevice> mDevices = new ArrayList<>();
    private AirPowerRepository mRepo;
    private TextView mNoDevices;
    private MainActivityItemAdapter mDevicesAdapter;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;

    public MyDevicesFragment() {
    }

    public static MyDevicesFragment newInstance() {
        return new MyDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        mRepo = AirPowerRepository.getInstance(getContext());
        mDevices = mRepo.getDevices();
    }

    ViewGroup mContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_my_devices, container, false);
        mContainer = container;
        // Text view no devices
        mNoDevices = view.findViewById(R.id.text_mydevices_no_devices);
        mNoDevices.setEnabled(false);
        if (mDevices.isEmpty()) {
            mNoDevices.setEnabled(true);
            mNoDevices.setText("No Devices");
        }

        // Devices list view
        listView = view.findViewById(R.id.list_device_detail_items);
        mDevicesAdapter = new MainActivityItemAdapter(mDevices, getContext());
        listView.setAdapter(mDevicesAdapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
            intent.setAction(AirPowerConstants.ACTION_LAUNCH_DETAIL);
            intent.putExtra(AirPowerConstants.KEY_DEVICE_ID, mDevices.get(position).getId());
            getActivity().finish();
            startActivity(intent);
        });

        // Floating button
        mFloatingActionButton = view.findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), DeviceSetupWizardHolderActivity.class);
            getActivity().finish();
            startActivity(i);
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
    }
}