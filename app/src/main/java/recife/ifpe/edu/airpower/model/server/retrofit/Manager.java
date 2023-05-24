package recife.ifpe.edu.airpower.model.server.retrofit;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Manager {


    private static final String TAG = Manager.class.getSimpleName();
    private static final String API_URL = "http://192.168.0.107:8080/";

    public void performHelloWorld(Callback<String> stringCallback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        DeviceService service = retrofit.create(DeviceService.class);
        Call<String> call = service.getHelloWorld();
        call.enqueue(stringCallback);
    }
}
