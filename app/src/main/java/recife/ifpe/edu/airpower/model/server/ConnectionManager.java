package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import recife.ifpe.edu.airpower.util.AirPowerLog;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ConnectionManager {

    private static final String AIR_POWER_SERVER_DOMAIN = "http://192.168.11.3:8080";
    private static final String OPEN_WEATHER_DOMAIN = "https://api.openweathermap.org/data/2.5/";
    private static ConnectionManager instance;
    private static Retrofit mAirPowerServerConnection;
    private static Retrofit mOpenWeatherApiConnection;

    private ConnectionManager() {
        Gson gson = new GsonBuilder().create();
        mAirPowerServerConnection = new Retrofit.Builder()
                .baseUrl(AIR_POWER_SERVER_DOMAIN)
                .client(AirPowerLog.getLoggerClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mOpenWeatherApiConnection = new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_DOMAIN)
                .client(AirPowerLog.getLoggerClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Retrofit getAirPowerConnection() {
        return mAirPowerServerConnection;
    }

    public Retrofit getWeatherConnection() {
        return mOpenWeatherApiConnection;
    }
}