package recife.ifpe.edu.airpower.model.repo.database;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

public class AirPowerTypeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<AirPowerDevice> getDevicesFromString(String value) {
        Type listType = new TypeToken<List<Object>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String getStringFromDevices(List<AirPowerDevice> devices) {
        return gson.toJson(devices);
    }
}
