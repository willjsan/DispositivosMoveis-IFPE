package recife.ifpe.edu.airpower.model.repo.database;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.DeviceTelemetry;

@Dao
public interface TelemetryDAO {

    @Insert
    void insert(DeviceTelemetry device);

    @Update
    void update(DeviceTelemetry device);

    @Delete
    void delete(DeviceTelemetry device);

    @Query("SELECT * from DEVICE_TELEMETRY")
    List<DeviceTelemetry> getAllTelemetry();
}