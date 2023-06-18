package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.DeviceStatus;

public abstract class ServerInterfaceWrapper {

    public interface IServerManager {
        void registerDevice(AirPowerDevice device, RegisterCallback callback);

        void unregisterDevice(AirPowerDevice device, UnregisterCallback callback);

        void getDeviceMeasurement(AirPowerDevice device, MeasurementCallback callback);

        void getDeviceStatus(AirPowerDevice device, DeviceStatusCallback callback);

        void enableDisableDevice(AirPowerDevice device, boolean enable,
                                 ServerInterfaceWrapper.DeviceEnableDisableCallback callback);

        void getMeasurementByGroup(List<AirPowerDevice> devices,
                                   ServerInterfaceWrapper.MeasurementCallback callback);
    }

    public interface RegisterCallback {
        void onSuccess(AirPowerDevice device);

        void onFailure(String message);
    }

    public interface UnregisterCallback {
        void onSuccess(String message);

        void onFailure(String message);
    }

    public interface MeasurementCallback {
        void onSuccess(List<DeviceMeasurement> measurements);

        void onFailure(String message);
    }

    public interface DeviceStatusCallback {
        void onSuccess(DeviceStatus deviceStatus);

        void onFailure(String message);
    }

    public interface DeviceEnableDisableCallback {
        void onResult(boolean result);
    }

}
