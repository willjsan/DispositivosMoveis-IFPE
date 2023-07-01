package recife.ifpe.edu.airpower.model.repo.database;

/*
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

import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;

@Dao
public interface EntitiesDAO {

    @Insert
    void insert(AirPowerDevice device);

    @Update
    void update(AirPowerDevice device);

    @Delete
    void delete(AirPowerDevice device);

    @Query("SELECT * from AIR_POWER_DEVICE")
    List<AirPowerDevice> getDevices();

    @Query("SELECT * from AIR_POWER_DEVICE WHERE DEVICE_ID = :id ")
    AirPowerDevice getDeviceById(int id);
    @Query("SELECT * from AIR_POWER_GROUP")
    List<Group> getGroups();
    @Insert
    void insert(Group group);
    @Update
    void update(Group group);
    @Delete
    void delete(Group group);
    @Query("SELECT * from AIR_POWER_GROUP WHERE GROUP_ID = :id")
    Group getGroupById(int id);

    @Insert
    void insert(AirPowerUser user);
    @Update
    void update(AirPowerUser user);
    @Delete
    void delete(AirPowerUser user);
    @Query("SELECT * from AIR_POWER_USER WHERE USER_ID = :id")
    AirPowerUser getUserById(int id);
    @Query("SELECT * from AIR_POWER_USER")
    List<AirPowerUser> getUsers();
}