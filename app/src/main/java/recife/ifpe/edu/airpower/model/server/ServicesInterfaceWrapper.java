package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

abstract class ServicesInterfaceWrapper {

    interface DeviceService {
        @POST("/api/endpoint")
        Call<AirPowerDevice> registerDevice(@Body RequestBody requestBody);

        @DELETE("/api/endpoint")
        Call<AirPowerDevice> unregisterDevice(@Body RequestBody requestBody);
    }

    interface UserService{

    }
}