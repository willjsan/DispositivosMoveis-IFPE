package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.net.MalformedURLException;
import java.net.URL;

public class AirPowerServerConnectionManagerImpl implements
        AirPowerServerInterfaceWrapper.AirPowerServerConnectionManager {

    public void getMeasurementByDeviceToken(String token) {

    }

    @Override
    public AirPowerURLHTTPSConnection buildServerConnection(String deviceToken) {
        AirPowerURLHTTPSConnection connection = null;
        try {
            URL url = new URL("www.airpower.com/api/get-consumption");
            connection = new AirPowerURLHTTPSConnection.Builder(url)
                    .setConnectionTimeout(3000)
                    .setRequestBody(deviceToken)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void sendServerConnection(AirPowerURLHTTPSConnection connection,
                                     AirPowerServerInterfaceWrapper.MeasureCallback callback) {

    }
}
