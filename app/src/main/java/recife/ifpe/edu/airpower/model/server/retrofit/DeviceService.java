package recife.ifpe.edu.airpower.model.server.retrofit;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import retrofit2.Call;
import retrofit2.http.GET;

public interface DeviceService {

    @GET("api/endpoint")
    Call<String> getHelloWorld();
}
