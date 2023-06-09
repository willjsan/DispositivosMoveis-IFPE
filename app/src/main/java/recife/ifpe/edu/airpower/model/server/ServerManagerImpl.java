package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerManagerImpl implements ServerInterfaceWrapper.IServerManager {

    private static final String TAG = ServerManagerImpl.class.getSimpleName();
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
    public void registerDevice(AirPowerDevice device, ServerInterfaceWrapper.RegisterCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "registerDevice");
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .registerDevice(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<AirPowerDevice>() {
                    @Override
                    public void onResponse(@NonNull Call<AirPowerDevice> call,
                                           @NonNull Response<AirPowerDevice> response) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onResponse");
                        callback.onResult(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<AirPowerDevice> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        // callback.onFailure(t.toString()); // TODO uncomment after tests
                        callback.onResult(new AirPowerDevice());// TODO remove it after tests
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
                                           @NonNull Response<AirPowerDevice> response) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onResponse");
                        callback.onSuccess("success");
                    }

                    @Override
                    public void onFailure(@NonNull Call<AirPowerDevice> call,
                                          @NonNull Throwable t) {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "onFailure");
                        // callback.onFailure(t.getMessage()); // TODO uncomment after tests
                        callback.onSuccess("this is a fake success"); // TODO remove it after tests
                    }
                });

    }

    @Override
    public void getDeviceMeasurement(AirPowerDevice device,
                                     ServerInterfaceWrapper.MeasurementCallback callback) {
        mConnectionManager
                .getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .getDeviceMeasurement(RequestBody
                        .create(MediaType.parse("application/json"),
                                new Gson().toJson(device)))
                .enqueue(new Callback<List<DeviceMeasurement>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DeviceMeasurement>> call,
                                           @NonNull Response<List<DeviceMeasurement>> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body());
                        } else {
                            callback.onFailure("Fail to retrieve device measurement");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DeviceMeasurement>> call,
                                          @NonNull Throwable t) {
                        // callback.onFailure(t.getMessage());// TODO uncomment after tests
                        callback.onResult(getMockMeasurement()); // TODO remove it after tests
                    }
                });

    }

    private List<DeviceMeasurement> getMockMeasurement() {
        return new ArrayList<>(Arrays.asList(
                new DeviceMeasurement(1, 112),
                new DeviceMeasurement(2, 123),
                new DeviceMeasurement(3, 155),
                new DeviceMeasurement(4, 145),
                new DeviceMeasurement(5, 112),
                new DeviceMeasurement(6, 162),
                new DeviceMeasurement(7, 14),
                new DeviceMeasurement(8, 156),
                new DeviceMeasurement(9, 14),
                new DeviceMeasurement(10, 13),
                new DeviceMeasurement(11, 123),
                new DeviceMeasurement(12, 156),
                new DeviceMeasurement(13, 143),
                new DeviceMeasurement(14, 341),
                new DeviceMeasurement(15, 165),
                new DeviceMeasurement(16, 1),
                new DeviceMeasurement(17, 176),
                new DeviceMeasurement(18, 1),
                new DeviceMeasurement(19, 134),
                new DeviceMeasurement(20, 14),
                new DeviceMeasurement(22, 134),
                new DeviceMeasurement(23, 13),
                new DeviceMeasurement(24, 176),
                new DeviceMeasurement(25, 13),
                new DeviceMeasurement(26, 31),
                new DeviceMeasurement(27, 134),
                new DeviceMeasurement(28, 41),
                new DeviceMeasurement(29, 134),
                new DeviceMeasurement(30, 231)
        ));
    }
}