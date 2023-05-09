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

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

@Dao
public interface DeviceDAO {

    @Insert
    void insert(AirPowerDevice device);

    @Update
    void update(AirPowerDevice device);

    @Delete
    void delete(AirPowerDevice device);

    @Query("SELECT * from AIR_POWER_DEVICE")
    List<AirPowerDevice> getDevices();
}