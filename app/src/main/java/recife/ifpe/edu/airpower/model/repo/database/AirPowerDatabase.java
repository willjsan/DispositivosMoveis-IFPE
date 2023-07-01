package recife.ifpe.edu.airpower.model.repo.database;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.util.AirPowerLog;

@Database(entities = {AirPowerDevice.class, Group.class, AirPowerUser.class}, version = 8,
        exportSchema = false)
@TypeConverters({AirPowerTypeConverter.class})
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

    public abstract EntitiesDAO getDeviceDAOInstance();
}