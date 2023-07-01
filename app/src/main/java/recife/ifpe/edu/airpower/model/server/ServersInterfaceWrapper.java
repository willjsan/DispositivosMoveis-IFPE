package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceStatus;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.model.repo.model.weather.Weather;

public abstract class ServersInterfaceWrapper {

    public interface IAirPowerServerManager {
        void registerDevice(AirPowerDevice device, RegisterCallback callback);

        void unregisterDevice(AirPowerDevice device, UnregisterCallback callback);

        void getDeviceMeasurement(AirPowerDevice device, MeasurementCallback callback);

        void getDeviceStatus(AirPowerDevice device, DeviceStatusCallback callback);

        void getMeasurementByGroup(List<AirPowerDevice> devices,
                                   ServersInterfaceWrapper.MeasurementCallback callback);

        void enableDisableDevice(AirPowerDevice device,
                                 ServersInterfaceWrapper.DeviceEnableDisableCallback callback);

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
        void onSuccess();

        void onFailure(String message);
    }

    public interface IWeatherServerManager {
        void getForecast(String localization, WeatherCallback callback);
    }

    public interface WeatherCallback {
        void onSuccess(Weather weather);

        void onFailure(String msg);
    }

    public interface IAirPowerAuthManager {
        void register(AirPowerUser user, IUserAuthCallback callback);
        void signIn(AirPowerUser user, IUserAuthCallback callback);
        void signOut(IUserAuthCallback callback);
        void unRegister(AirPowerUser user, IUserAuthCallback callback);
    }

    public interface IUserAuthCallback {
        void onSuccess(String msg);
        void onFailure(String msg);
    }
}
