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

import androidx.cardview.widget.CardView;
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
    private SupportMapFragment mMapFragment;

    // Weather
    private ImageView mWeatherCardIcon;
    private TextView mWeatherTempValue;
    private TextView mWeatherHumidityValue;
    private TextView mWeatherDetail;
    private TextView mWeatherPlaceName;
    private TextView mWeatherTitle;
    private CardView mCardWeather;

    // Maps
    private GoogleMap mMap;
    private CardView mCardLocalization;

    // Chart
    private BarChart mChart;
    private CardView mCardConsumption;

    private ServersInterfaceWrapper.IWeatherServerManager mWeatherServerManager;
    private ServersInterfaceWrapper.IAirPowerServerManager mAirPowerServerManager;
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
        this.mCurrentGroup = mRepo.getGroupById(getGroupIdFromSP(getContext()));
        this.mWeatherServerManager = WeatherServerManagerImpl.getInstance();
        this.mAirPowerServerManager = AirPowerServerManagerImpl.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewsByIds(view);
        groupSpinnerSetup();
        newGroupButtonSetup();
        updateAllFields();
        return view;
    }

    private void newGroupButtonSetup() {
        mButtonAddGroup.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), GroupActivity.class);
            intent.setAction(AirPowerConstants.ACTION_NEW_GROUP);
            startActivity(intent);
        });
    }

    private void getForecast() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getForecast");
        if (mCurrentGroup.getLocalization() == null
                || mCurrentGroup.getLocalization().isEmpty()) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "Group localization is null");
            mCardWeather.setVisibility(View.GONE);
            return;
        }
        mWeatherServerManager.getForecast(mCurrentGroup.getLocalization(),
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
                        Drawable iconDrawable = AirPowerUtil
                                .getDrawable(WEATHER_ICON_PREFIX + iconName, getContext());
                        mCardWeather.setVisibility(View.VISIBLE);
                        mWeatherTitle.setText("Forecast");
                        mWeatherCardIcon.setImageDrawable(iconDrawable);
                        mWeatherTempValue.setText(temp);
                        mWeatherHumidityValue.setText(humidity);
                        mWeatherDetail.setText(weatherDesc);
                        mWeatherPlaceName.setText(placeName);
                    }

                    @Override
                    public void onFailure(String msg) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.w(TAG, "getForecast: onFailure");
                        mCardWeather.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mGroupAdapter.notifyDataSetChanged();
        mGroups = mRepo.getGroups();
    }

    private void getMap() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "getMap");
        if (mMapFragment == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "maps fragment is null");
            return;
        }
        if (mCurrentGroup.getLocalization() == null || mCurrentGroup.getLocalization().isEmpty()){
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "group localization is null");
            mCardLocalization.setVisibility(View.GONE);
            return;
        }
        mCardLocalization.setVisibility(View.VISIBLE);
        mMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            String loc = mCurrentGroup.getLocalization();
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

    public int getGroupIdFromSP(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_INTEGER_VALUE, 0);
        if (id == 0) {
            if (AirPowerLog.ISLOGABLE) {
                int id2 = mRepo.getGroups().get(0).getId();
                AirPowerLog.e(TAG, "Error, can't get id from shared pref:  id:" + id2);
                return id2;
            }
        }
        return id;
    }

    private void groupSpinnerSetup() {
        mGroupAdapter = new GroupsAdapter(getContext());
        mGroupsSpinner.setAdapter(mGroupAdapter);
        mGroupsSpinner.setSelection(getLastSelectedGroupIndex());
        mGroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long l) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.d(TAG, "group selection:pos.:" + position);
                mCurrentGroup = mGroups.get(position);
                saveInteger(getContext(), mCurrentGroup.getId());
                updateAllFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getMeasurement() {
        mAirPowerServerManager.getMeasurementByGroup(mCurrentGroup.getDevices(),
                new ServersInterfaceWrapper.MeasurementCallback() {
                    @Override
                    public void onSuccess(List<DeviceMeasurement> measurements) {
                        mCardConsumption.setVisibility(View.VISIBLE);
                        generateChart(mChart, measurements);
                    }

                    @Override
                    public void onFailure(String message) {
                        mCardConsumption.setVisibility(View.GONE);
                    }
                });
    }

    private int getLastSelectedGroupIndex() {
        int savedId = getGroupIdFromSP(getContext());
        for (int i = 0; i < mGroups.size(); i++) {
            if (mGroups.get(i).getId() == savedId) {
                if (AirPowerLog.ISLOGABLE)
                    AirPowerLog.d(TAG, "Saved group name:" + mGroups.get(i).getName());
                return i;
            }
        }
        return 0;
    }

    private void updateAllFields() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "updateAllFields");
        if (mCurrentGroup == null) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.w(TAG, "current group is null");
            return;
        }
        getMap();
        getForecast();
        getMeasurement();
    }

    private void generateChart(View view, List<DeviceMeasurement> measurements) {
        new ChartAdapter
                .Builder(view.findViewById(R.id.home_consumption_overview_chart), measurements)
                .build();
    }

    private void findViewsByIds(View view) {
        mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.group_map);
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
        mCardWeather = view.findViewById(R.id.card_weather);
        mCardLocalization = view.findViewById(R.id.card_localization);
        mCardConsumption = view.findViewById(R.id.card_consumption);
    }
}