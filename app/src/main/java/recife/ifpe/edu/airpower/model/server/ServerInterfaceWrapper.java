package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

public abstract class ServerInterfaceWrapper {

    public interface IServerManager {
        void registerDevice(AirPowerDevice device, RegisterCallback callback);

        void unregisterDevice(AirPowerDevice device, UnregisterCallback callback);

        void getMeasurementByDeviceId(AirPowerDevice device, MeasurementCallback callback);

    }

    public interface RegisterCallback {
        void onResult(AirPowerDevice device);

        void onFailure(String message);
    }

    public interface UnregisterCallback {
        void onResult(String message);

        void onFailure(String message);
    }

    public interface MeasurementCallback {
        void onResult(List<AirPowerDevice> devices);

        void onFailure(String message);
    }

}
