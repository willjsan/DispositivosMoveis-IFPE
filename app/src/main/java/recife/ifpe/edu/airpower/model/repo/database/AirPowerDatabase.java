package recife.ifpe.edu.airpower.model.repo.database;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceTelemetry;
import recife.ifpe.edu.airpower.util.AirPowerLog;

@Database(entities = {AirPowerDevice.class, DeviceTelemetry.class}, version = 2, exportSchema = false)
public abstract class AirPowerDatabase extends RoomDatabase {

    public static final String TAG = AirPowerDatabase.class.getSimpleName();
    public static final String DATABASE_NAME = "AirPowerApp.db";
    private static AirPowerDatabase dbInstance;

    public static synchronized AirPowerDatabase getDataBaseInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context, AirPowerDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // TODO remove before release
                    .build();
            if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "Database instantiation");
        }
        return dbInstance;
    }

    public abstract DeviceDAO getDeviceDAOInstance();

    public abstract TelemetryDAO getTelemetryDAOInstance();
}