package recife.ifpe.edu.airpower.ui.groupinsertionwizard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.GroupDevicesSelectionAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


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
                if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "fail to finish activity");
            }
        });
        groupLocalizationSetup();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void groupLocalizationSetup() {
        mSetLocalizationButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Set Group localization")
                    .setMessage("Do you want to use your real localization and set this in this group?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        setGroupLocation();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    });
            builder.create().show();
        });
    }

    public void setGroupLocation() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "setGroupLocation");
        Context context = getContext();
        FragmentActivity activity = getActivity();
        if (context == null || activity == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "context or activity is null");
            return;
        }
        AirPowerUtil.getCurrentLocation(context, activity,
                location -> {
                    if (location == null) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.w(TAG, "addOnSuccessListener: location is null");
                        return;
                    }
                    mDeviceLocalization = location;
                    mSetLocalizationButton.setEnabled(false);
                    Toast.makeText(getContext(), "Localization set", Toast.LENGTH_SHORT).show();
                    setupMapFragment();
                });
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