package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.annotation.NonNull;

import recife.ifpe.edu.airpower.model.repo.model.weather.Weather;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherServerManagerImpl implements
        ServersInterfaceWrapper.IWeatherServerManager {

    private static final String TAG = WeatherServerManagerImpl.class.getSimpleName();
    private static WeatherServerManagerImpl instance;
    private final ConnectionManager mConnectionManager;

    private WeatherServerManagerImpl() {
        mConnectionManager = ConnectionManager.getInstance();
    }

    public static WeatherServerManagerImpl getInstance() {
        if (instance == null) {
            instance = new WeatherServerManagerImpl();
        }
        return instance;
    }

    @Override
    public void getForecast(String localization, ServersInterfaceWrapper.WeatherCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "getForecast");
        //String API_KEY = "2da11e29e43bbbe18118edd3d591795c";
        String API_KEY = "a11ac945f2360e8cf7d496e7cb53dc00"; // TODO KEY do professor

        String[] split = localization.split(",");
        String lat = split[0];
        String lon = split[1];

        mConnectionManager.getWeatherConnection().create(
                        ServicesInterfaceWrapper.WeatherService.class)
                .getWeather(lat, lon, API_KEY)
                .enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(@NonNull Call<Weather> call,
                                           @NonNull Response<Weather> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.w(TAG, "getForecast: status:" + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Weather> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.e(TAG, "getForecast: onFailure");
                        callback.onFailure(t.getMessage());
                    }
                });
    }
}