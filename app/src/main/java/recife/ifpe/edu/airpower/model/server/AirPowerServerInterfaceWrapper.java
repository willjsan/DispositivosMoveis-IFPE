package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;

public abstract class AirPowerServerInterfaceWrapper {

    public interface AirPowerServerConnectionManager {
        AirPowerURLHTTPSConnection buildServerConnection (String deviceToken);
        void sendServerConnection(AirPowerURLHTTPSConnection connection, MeasureCallback callback);
    }

    public interface MeasureCallback {
        void onReceive(DeviceMeasurement measurement);
    }
}
