package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerManagerImpl implements ServerInterfaceWrapper.IServerManager {

    private static ServerManagerImpl instance;
    private ConnectionManager cm = ConnectionManager.getInstance();

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
        cm.getConnection()
                .create(ServicesInterfaceWrapper.DeviceService.class)
                .registerDevice(RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(device)))
                .enqueue(new Callback<AirPowerDevice>() {
                    @Override
                    public void onResponse(@NonNull Call<AirPowerDevice> call,
                                           @NonNull Response<AirPowerDevice> response) {
                        callback.onResult(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<AirPowerDevice> call,
                                          @NonNull Throwable t) {
                        callback.onFailure(t.toString());
                    }
                });
    }

    @Override
    public void unregisterDevice(AirPowerDevice device,
                                 ServerInterfaceWrapper.UnregisterCallback callback) {

    }

    @Override
    public void getMeasurementByDeviceId(AirPowerDevice device,
                                         ServerInterfaceWrapper.MeasurementCallback callback) {

    }
}