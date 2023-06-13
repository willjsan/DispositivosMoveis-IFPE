package recife.ifpe.edu.airpower.ui.groupinsertionwizard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.GroupDevicesSelectionAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.Group;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class GroupInsertionFragment extends Fragment {

    private static final String TAG = GroupInsertionFragment.class.getSimpleName();

    private ListView mDeviceSelectionListView;
    private List<AirPowerDevice> mSelectedDevices;
    private FloatingActionButton mSaveGroupButton;
    private FloatingActionButton mSetLocalizationButton;
    private TextView mGroupName;
    private TextView mGroupDescription;
    private String mDeviceLocalization = "";
    private AirPowerRepository mRepo;
    private GoogleMap mMap;
    private boolean mLocationGrantedByUser = false;;
    private int FINE_LOCATION_REQUEST = 11;
    private Group mGroup;
    private SupportMapFragment mapFragment;

    public GroupInsertionFragment() {

    }

    public static GroupInsertionFragment newInstance() {
        return new GroupInsertionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepo = AirPowerRepository.getInstance(getContext());
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        mGroup = new Group();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_insertion,
                container, false);
        findViewsByIds(view);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.group_map_view);
        GroupDevicesSelectionAdapter adapter =
                new GroupDevicesSelectionAdapter(mRepo.getDevices(), getContext());
        mDeviceSelectionListView.setAdapter(adapter);
        mDeviceSelectionListView.setOnItemClickListener((parent, view1, position, id) -> {
            adapter.handleSelection(position);
            mSelectedDevices = adapter.getSelectedDevices();
        });

        mSaveGroupButton.setOnClickListener(view12 -> {
            mGroup.setDevices(mSelectedDevices);
            mGroup.setName(mGroupName.getText().toString());
            mGroup.setDescription(mGroupDescription.getText().toString());
            mGroup.setLocalization(mDeviceLocalization);
            mRepo.insert(mGroup);
            try {
                getActivity().finish();
            } catch (Exception e) {
                if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG,"fail to finish activity");
            }
        });
        groupLocalizationSetup();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.mLocationGrantedByUser = (requestCode == FINE_LOCATION_REQUEST) && granted;
        AirPowerLog.w(TAG, "granted:" + granted); // TODO remover
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Boiler plate
     */
    private void groupLocalizationSetup() {
        mSetLocalizationButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Set Group localization")
                    .setMessage("Do you want to use your real localization and set this in this group?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        setGroupLocalization();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    });
            builder.create().show();
        });
    }

    private void setGroupLocalization() {
        requestPermission();
        setGroupLocation();
    }

    /**
     * Boiler plate
     */
    private void requestPermission() {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            mLocationGrantedByUser = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            if (mLocationGrantedByUser) return;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_REQUEST);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "error while getting permission");
        }
    }

    /**
     * Boiler plate
     */
    public void setGroupLocation() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "setGroupLocation");
        try {
            FusedLocationProviderClient fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(getActivity());
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location == null) {
                    if (AirPowerLog.ISLOGABLE)
                        AirPowerLog.w(TAG, "addOnSuccessListener: location is null");
                    return;
                }
                mDeviceLocalization = location.getLatitude() + "," + location.getLongitude();
                mSetLocalizationButton.setEnabled(false);
                Toast.makeText(getContext(), "Localization set", Toast.LENGTH_SHORT).show();
                AirPowerLog.e(TAG, "group location:" + mDeviceLocalization); // TODO remover
                setupMapFragment();
            });
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "error while getting localization\n" + e.getMessage());
        }
    }

    private void setupMapFragment() {
        if (mapFragment == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "maps fragment is null");
            return;
        }
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (mDeviceLocalization == null || mDeviceLocalization.isEmpty()) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.w(TAG, "group localization is null");
                Toast.makeText(getContext(), "Localization not set", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] split = mDeviceLocalization.split(",");
            String lat = split[0];
            String lon = split[1];
            if (lon.isEmpty() || lat.isEmpty()) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.w(TAG, "device latitude or longitude is empty");
                return;
            }
            LatLng deviceLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            mMap.addMarker(new MarkerOptions()
                    .position(deviceLocation).title("Group Localization"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 17F));
        });
    }

    private void findViewsByIds(View view) {
        mDeviceSelectionListView = view.findViewById(R.id.groups_devices_list_view);
        mSaveGroupButton = view.findViewById(R.id.floating_group_localization_finish);
        mGroupName = view.findViewById(R.id.et_group_name);
        mGroupDescription = view.findViewById(R.id.et_group_description);
        mSetLocalizationButton = view.findViewById(R.id.group_wizard_localization);
    }
}