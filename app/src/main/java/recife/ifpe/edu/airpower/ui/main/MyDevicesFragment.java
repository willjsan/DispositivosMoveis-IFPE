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
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.AirPowerDeviceDAO;
import recife.ifpe.edu.airpower.model.adapter.MainActivityItemAdapter;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.ui.DeviceDetailActivity;
import recife.ifpe.edu.airpower.ui.insertionwizard.DeviceSetupWizardHolderActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;


public class MyDevicesFragment extends Fragment {

    private static final String TAG = MyDevicesFragment.class.getSimpleName();
    private FloatingActionButton mFloatingActionButton;
    private ListView listView;
    private List<AirPowerDevice> devices = new ArrayList<>();

    public MyDevicesFragment() {
        // Required empty public constructor
    }

    public static MyDevicesFragment newInstance() {
        return new MyDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_devices, container, false);

        listView = view.findViewById(R.id.list_device_detail_items);
        listView.setAdapter(new MainActivityItemAdapter(new AirPowerDeviceDAO().getDevices(),
                getContext())); // TODO does need to setup the adapter to real data source

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent i = new Intent(getContext(), DeviceDetailActivity.class);
            i.putExtra(AirPowerConstants.DEVICE_ITEM_INDEX, position);
            startActivity(i);
        });


        mFloatingActionButton = view.findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), DeviceSetupWizardHolderActivity.class);
            startActivity(i);
        });

        return view;
    }
}