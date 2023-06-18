package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceEnableDisable;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.DeviceStatus;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerManagerImpl implements ServerInterfaceWrapper.IServerManager {

    private static final String TAG = ServerManagerImpl.class.getSimpleName();
    public static final String SERVICE_UNAVAILABLE = "service unavailable";
    private static ServerManagerImpl instance;
    private ConnectionManager mConnectionManager = ConnectionManager.getInstance();

    private ServerManagerImpl() {

    }

    public static ServerManagerImpl getInstance() {
        if (instance == null) {
            instance = new ServerManagerImpl();
        }
        return instance;
    }

    @Override
    public void registerDevice(AirPowerDevice device,
                               ServerInterfaceWrapper.RegisterCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "registerDevice");
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .registerDevice(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<AirPowerDevice>() {
                    @Override
                    public void onResponse(@NonNull Call<AirPowerDevice> call,
                                           @NonNull Response<AirPowerDevice> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.w(TAG, "status:" + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AirPowerDevice> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    //@Override
//    public void enableDisableDevice(AirPowerDevice device, boolean enable,
//                                    ServerInterfaceWrapper.DeviceEnableDisableCallback callback) {
//        final DeviceEnableDisable enableDisableDevice = new DeviceEnableDisable(device, enable);
//        if (AirPowerLog.ISLOGABLE)
//            AirPowerLog.d(TAG, "after set: " + enableDisableDevice.toString()); // TODO remover
//        if (AirPowerLog.ISLOGABLE)
//            AirPowerLog.d(TAG, "enableDisableDevice: " + enable); // TODO remover
//
//        String s = new Gson().toJson(enableDisableDevice);
//        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "after Gson transform: " + s); // TODO remover
//        mConnectionManager
//                .getConnection()
//                .create(ServicesInterfaceWrapper.DeviceService.class)
//                .enableDisableDevice(RequestBody.create(MediaType.parse("application/json"),
//                        new Gson().toJson(enableDisableDevice)))
//                .enqueue(new Callback<Boolean>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Boolean> call,
//                                           @NonNull Response<Boolean> resp) {
//
//                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
//                            callback.onSuccess(Boolean.TRUE.equals(resp.body()));
//                            AirPowerLog.w(TAG, "server response: " + resp.body()); // TODO remover
//                        } else {
//                            if (AirPowerLog.ISLOGABLE)
//                                AirPowerLog.w(TAG, "status:" + resp.code());
//                            callback.onSuccess(false); // TODO maybe this is wrong
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Boolean> call,
//                                          @NonNull Throwable t) {
//                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
//                        callback.onSuccess(false);
//                    }
//                });
//
//    }

    @Override
    public void getDeviceStatus(AirPowerDevice device,
                                ServerInterfaceWrapper.DeviceStatusCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "getDeviceStatus");
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .getDeviceStatus(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<DeviceStatus>() {
                    @Override
                    public void onResponse(@NonNull Call<DeviceStatus> call,
                                           @NonNull Response<DeviceStatus> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            AirPowerLog.w(TAG, "server response:" + resp.body()); // TODO remover
                            callback.onSuccess(resp.body());
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.w(TAG, "status:" + resp.code());
                            callback.onFailure(SERVICE_UNAVAILABLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DeviceStatus> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        callback.onFailure(SERVICE_UNAVAILABLE);
                    }
                });
    }

    @Override
    public void unregisterDevice(AirPowerDevice device,
                                 ServerInterfaceWrapper.UnregisterCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "unregisterDevice");
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .unregisterDevice(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<AirPowerDevice>() {
                    @Override
                    public void onResponse(@NonNull Call<AirPowerDevice> call,
                                           @NonNull Response<AirPowerDevice> resp) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onResponse");
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess("success");
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.w(TAG, "status:" + resp.code());
                            callback.onFailure("status:" + resp.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AirPowerDevice> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        callback.onFailure(t.getMessage());
                    }
                });

    }

    @Override
    public void getDeviceMeasurement(AirPowerDevice device,
                                     ServerInterfaceWrapper.MeasurementCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "getDeviceMeasurement");
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .getDeviceMeasurement(RequestBody
                        .create(MediaType.parse("application/json"),
                                new Gson().toJson(device)))
                .enqueue(new Callback<List<DeviceMeasurement>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DeviceMeasurement>> call,
                                           @NonNull Response<List<DeviceMeasurement>> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.w(TAG, "status:" + resp.code());
                            callback.onFailure("status:" + resp.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DeviceMeasurement>> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        callback.onFailure(t.getMessage());
                    }
                });

    }

    @Override
    public void getMeasurementByGroup(
            List<AirPowerDevice> devices,
            ServerInterfaceWrapper.MeasurementCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "getMeasurementByGroup");
        mConnectionManager.getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .getMeasurementByGroup(RequestBody
                        .create(MediaType.parse("application/json"),
                                new Gson().toJson(devices)))
                .enqueue(new Callback<List<DeviceMeasurement>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DeviceMeasurement>> call,
                                           @NonNull Response<List<DeviceMeasurement>> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess(resp.body());
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.d(TAG, "status" + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DeviceMeasurement>> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.d(TAG, "onFailure");
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void enableDisableDevice(AirPowerDevice device,
                                    ServerInterfaceWrapper.DeviceEnableDisableCallback callback) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "enableDisableDevice: " + device.toString());
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .enableDisableDevice(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call,
                                           @NonNull Response<Boolean> resp) {
                        if (resp.isSuccessful() && resp.code() == AirPowerConstants.HTTP_OK) {
                            callback.onSuccess();
                        } else {
                            if (AirPowerLog.ISLOGABLE)
                                AirPowerLog.d(TAG, "status: " + resp.code());
                            callback.onFailure(String.valueOf(resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.d(TAG, "onFailure:" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                });
    }
}
