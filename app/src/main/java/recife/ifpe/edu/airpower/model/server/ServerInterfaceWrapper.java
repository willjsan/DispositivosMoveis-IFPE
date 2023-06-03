package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;

public abstract class ServerInterfaceWrapper {

    public interface IServerManager {
        void registerDevice(AirPowerDevice device, RegisterCallback callback);

        void unregisterDevice(AirPowerDevice device, UnregisterCallback callback);

        void getDeviceMeasurement(AirPowerDevice device, MeasurementCallback callback);

    }

    public interface RegisterCallback {
        void onResult(AirPowerDevice device);

        void onFailure(String message);
    }

    public interface UnregisterCallback {
        void onSuccess(String message);

        void onFailure(String message);
    }

    public interface MeasurementCallback {
        void onResult(List<DeviceMeasurement> measurements);

        void onFailure(String message);
    }

}
