package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

abstract class ServicesInterfaceWrapper {

    interface DeviceService {
        @POST("/api/endpoint")
        Call<AirPowerDevice> registerDevice(@Body RequestBody requestBody);

        @HTTP(method = "DELETE", path = "/api/endpoint", hasBody = true)
        Call<AirPowerDevice> unregisterDevice(@Body RequestBody requestBody);

        @POST("/api/device/measurement")
        Call<List<DeviceMeasurement>> getDeviceMeasurement(@Body RequestBody requestBody);
    }

    interface UserService {

    }
}