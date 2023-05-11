package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.AirPowerDeviceDAO;
import recife.ifpe.edu.airpower.model.adapter.MainActivityItemAdapter;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;


public class MyDevicesFragment extends Fragment {

    private static final String TAG = MyDevicesFragment.class.getSimpleName();
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
        return view;
    }
}