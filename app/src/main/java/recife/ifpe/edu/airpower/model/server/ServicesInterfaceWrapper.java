package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.util.List;

import okhttp3.RequestBody;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceMeasurement;
import recife.ifpe.edu.airpower.model.repo.model.device.DeviceStatus;
import recife.ifpe.edu.airpower.model.repo.model.weather.Weather;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

abstract class ServicesInterfaceWrapper {

    interface DeviceService {
        @POST("/api/endpoint")
        Call<AirPowerDevice> registerDevice(@Body RequestBody requestBody);

        @HTTP(method = "DELETE", path = "/api/endpoint", hasBody = true)
        Call<AirPowerDevice> unregisterDevice(@Body RequestBody requestBody);

        @POST("/api/device/measurement")
        Call<List<DeviceMeasurement>> getDeviceMeasurement(@Body RequestBody requestBody);

        @POST("/api/device/status")
        Call<DeviceStatus> getDeviceStatus(@Body RequestBody requestBody);

        @POST("/api/device/enabledisable")
        Call<Boolean> enableDisableDevice(@Body RequestBody requestBody);

        @POST("/api/device/measurement/group")
        Call<List<DeviceMeasurement>> getMeasurementByGroup(@Body RequestBody requestBody);
    }

    interface WeatherService {
        @GET("weather")
        Call<Weather> getWeather(@Query("lat") String latitude,
                                 @Query("lon") String longitude,
                                 @Query("appid") String appid);
    }

    interface UserService {

    }
}