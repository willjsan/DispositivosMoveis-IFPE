package recife.ifpe.edu.airpower.ui.main;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.adapter.ChartAdapter;
import recife.ifpe.edu.airpower.model.adapter.GroupsAdapter;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.model.repo.model.weather.Weather;
import recife.ifpe.edu.airpower.model.repo.model.weather.WeatherDetail;
import recife.ifpe.edu.airpower.model.repo.model.weather.WeatherInfo;
import recife.ifpe.edu.airpower.model.server.AirPowerServerManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.model.server.WeatherServerManagerImpl;
import recife.ifpe.edu.airpower.ui.groupinsertionwizard.GroupActivity;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;


public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    public static final String WEATHER_ICON_PREFIX = "weather_icon_";
    private List<Group> mGroups;
    private AirPowerRepository mRepo;
    private Button mButtonAddGroup;
    private Spinner mGroupsSpinner;
    private TextView mBannerTitleConsumption;
    private TextView mBannerTitleLocalization;
    private GroupsAdapter mGroupAdapter;
    private Group mCurrentGroup;
    private SupportMapFragment mapFragment;

    // Weather
    private ImageView mWeatherCardIcon;
    private TextView mWeatherTempValue;
    private TextView mWeatherHumidityValue;
    private TextView mWeatherDetail;
    private TextView mWeatherPlaceName;
    private TextView mWeatherTitle;

    // Maps
    private GoogleMap mMap;

    // Chart
    private BarChart mChart;

    private ServersInterfaceWrapper.IWeatherServerManager weatherServerManager;
    private static final String PREF_NAME = "lastGroupSelected";
    private static final String KEY_INTEGER_VALUE = "integerValue";

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        this.mRepo = AirPowerRepository.getInstance(getContext());
        this.mGroups = mRepo.getGroups();
        this.mCurrentGroup = mRepo.getGroupById(getInteger(getContext()));
        weatherServerManager = WeatherServerManagerImpl.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.group_map);
        findViewsByIds(view);
        groupSpinnerSetup();
        mButtonAddGroup.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), GroupActivity.class);
            intent.setAction(AirPowerConstants.ACTION_NEW_GROUP);
            startActivity(intent);
        });
        getForecast();

        return view;
    }

    private void getForecast() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "weatherSetup");

        if (mCurrentGroup.getLocalization() == null
                || mCurrentGroup.getLocalization().isEmpty()) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG,"Group localization is null");
            mWeatherTitle.setText("Can't get forecast");
            mWeatherPlaceName.setText("Group localization not set");
            return;
        }
        weatherServerManager.getForecast(mCurrentGroup.getLocalization(),
                new ServersInterfaceWrapper.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                WeatherDetail weatherDetail = weather.getWeather().get(0);
                WeatherInfo weatherInfo = weather.getMain();
                String placeName = weather.getName();
                String iconName = weatherDetail.getIcon();
                String humidity = weatherInfo.getHumidity() + "%";
                String temp = AirPowerUtil.kelvinToCelsius(weatherInfo.getTemp());
                String weatherDesc = weatherDetail.getDescription();
                Drawable iconDrawable = AirPowerUtil.getDrawable(WEATHER_ICON_PREFIX + iconName,
                        getContext());
                mWeatherCardIcon.setImageDrawable(iconDrawable);
                mWeatherTempValue.setText(temp);
                mWeatherHumidityValue.setText(humidity);
                mWeatherDetail.setText(weatherDesc);
                mWeatherPlaceName.setText(placeName);
            }

            @Override
            public void onFailure(String msg) {
                mWeatherCardIcon.setVisibility(View.GONE);
                mWeatherTempValue.setVisibility(View.GONE);
                mWeatherHumidityValue.setVisibility(View.GONE);
                mWeatherDetail.setVisibility(View.GONE);
                mWeatherPlaceName.setVisibility(View.GONE);
                mWeatherTitle.setText("Can't get forecast");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mGroupAdapter.notifyDataSetChanged();
        mGroups = mRepo.getGroups();
    }

    private void setupMapFragment() {
        if (mapFragment == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "maps fragment is null");
            return;
        }
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            String loc = mCurrentGroup.getLocalization();
            if (loc == null || loc.isEmpty()) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.w(TAG, "group localization is null");
                Toast.makeText(getContext(), "Localization not set", Toast.LENGTH_SHORT).show();
                mMap.clear();
                return;
            }
            String[] split = loc.split(",");
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


    public static void saveInteger(Context context, int value) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_INTEGER_VALUE, value);
        editor.apply();
    }

    public static int getInteger(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        AirPowerLog.e(TAG, String.valueOf(sharedPreferences.getInt(KEY_INTEGER_VALUE, 0)));
        return sharedPreferences.getInt(KEY_INTEGER_VALUE, 0);
    }

    private void groupSpinnerSetup() {
        mGroupAdapter = new GroupsAdapter(getContext());
        mGroupsSpinner.setAdapter(mGroupAdapter);
        mGroupsSpinner.setSelection(getLastSelectedGroup());
        mGroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long l) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.d(TAG, "group selection: onItemSelected: pos.:" + position);
                mCurrentGroup = mGroups.get(position);
                saveInteger(getContext(), mCurrentGroup.getId());
                updateAllFields();
                AirPowerServerManagerImpl.getInstance().getMeasurementByGroup(mCurrentGroup.getDevices(),
                        new ServersInterfaceWrapper.MeasurementCallback() {
                            @Override
                            public void onSuccess(List<DeviceMeasurement> measurements) {
                                generateChart(mChart, measurements);
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getLastSelectedGroup() {
        int savedId = getInteger(getContext());
        for (int i = 0; i < mGroups.size(); i++) {
            if (mGroups.get(i).getId() == savedId) {
                AirPowerLog.w(TAG, "saved group:" + mGroups.get(i).getName()); // TODO remover
                return i;
            }
        }
        return 0;
    }

    private void updateAllFields() {
        if (mCurrentGroup == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "current group is null");
            return;
        }
        // TODO show a progress bar dialog
        mBannerTitleConsumption.setText(mCurrentGroup.getName() + " Consumption");
        mBannerTitleLocalization.setText(mCurrentGroup.getName() + " Localization");
        setupMapFragment();
        getForecast();
    }

    private void generateChart(View view, List<DeviceMeasurement> measurements) {
        new ChartAdapter
                .Builder(view.findViewById(R.id.home_consumption_overview_chart), measurements)
                .build();
    }

    private void findViewsByIds(View view) {
        mButtonAddGroup = view.findViewById(R.id.home_button_add_group);
        mGroupsSpinner = view.findViewById(R.id.home_spinner_groups);
        mBannerTitleConsumption = view.findViewById(R.id.tv_ddetail_consumption_label);
        mBannerTitleLocalization = view.findViewById(R.id.tv_group_localization_label);
        mChart = view.findViewById(R.id.home_consumption_overview_chart);
        mWeatherCardIcon = view.findViewById(R.id.card_weather_icon);
        mWeatherTempValue = view.findViewById(R.id.card_weather_temp_value);
        mWeatherHumidityValue = view.findViewById(R.id.card_weather_humidity_value);
        mWeatherDetail = view.findViewById(R.id.card_weather_icon_label);
        mWeatherPlaceName = view.findViewById(R.id.card_weather_local_name);
        mWeatherTitle = view.findViewById(R.id.card_weather_title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}