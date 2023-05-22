package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;

class AirPowerConnectionManager implements
        AirPowerServerInterfaceWrapper.IConnectionManager {

    public static final String TAG = AirPowerConnectionManager.class.getSimpleName();
    public static final int DEVICE_REGISTRY = 1;
    public static final int SERVER_UNREGISTER = 2;
    public static final int SERVER_MEASURE = 3;

    private static final String NOTEBOOK = "100.67.85.226";
    private static final String DESKTOP = "192.168.11.3";
    private static String API_URL = "http://" + NOTEBOOK + ":8080/api/endpoint";

    @Override
    public AirPowerConnection getAirPowerConnection(String deviceToken, int requestType) {
        AirPowerConnection connection = null;
        int connectionTimeOut = 3000;
        switch (requestType) {
            case DEVICE_REGISTRY:
                try {
                    URL url = new URL(API_URL);
                    connection = new AirPowerConnection.Builder(url)
                            .setConnectionTimeout(connectionTimeOut)
                            .setRequestType("GET")
                            .build();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case SERVER_UNREGISTER:
                break;
        }

        return connection;
    }

    @Override
    public void writeAirPowerConnection(
            AirPowerConnection connection,
            AirPowerServerInterfaceWrapper.IServerResponseCallback callback) {
        handleServerResponse(connection, callback);
    }

    void closeConnection(AirPowerConnection connection) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.w(TAG, "closeConnection()");
        connection.getHttpURLConnection().disconnect();
    }

    private void handleServerResponse(
            AirPowerConnection airPowerConnection,
            AirPowerServerInterfaceWrapper.IServerResponseCallback callback) {
        try {
            HttpURLConnection connection = airPowerConnection.getHttpURLConnection();
            int respStatus = connection.getResponseCode();
            if (respStatus == HttpsURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String msg = AirPowerUtil.inputStreamToString(inputStream);
                callback.onSuccess(msg);
            } else {
                callback.onFailure("error: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Error while getting server response");
            callback.onFailure("error");
        }
    }
}