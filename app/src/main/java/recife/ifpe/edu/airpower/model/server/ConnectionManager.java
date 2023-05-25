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

    private static final String BASE_URL = "http://192.168.11.3:8080";
    private static ConnectionManager instance;
    private static Retrofit connection;

    private ConnectionManager() {
        Gson gson = new GsonBuilder().create();
        connection = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(AirPowerLog.getLoggerClient().build()) // log interceptor
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Retrofit getConnection() {
        return connection;
    }
}