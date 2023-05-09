package recife.ifpe.edu.airpower.model.repo;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.DeviceTelemetry;
import recife.ifpe.edu.airpower.model.repo.database.AirPowerDatabase;
import recife.ifpe.edu.airpower.model.repo.database.DeviceDAO;
import recife.ifpe.edu.airpower.model.repo.database.TelemetryDAO;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class AirPowerRepository {
    private static String TAG = AirPowerRepository.class.getSimpleName();
    private static AirPowerRepository instance;

    private final TelemetryDAO mTelemetryDAO;
    private final DeviceDAO mDeviceDAO;

    private AirPowerRepository(Context context) {
        AirPowerDatabase mDb = AirPowerDatabase.getDataBaseInstance(context);
        mDeviceDAO = mDb.getDeviceDAOInstance();
        mTelemetryDAO = mDb.getTelemetryDAOInstance();
    }

    public static AirPowerRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AirPowerRepository(context);
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.d(TAG, "AirPowerRepository instantiation");
        }
        return instance;
    }

    public void insert(DeviceTelemetry deviceTelemetry) {
        try {
            mTelemetryDAO.insert(deviceTelemetry);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't insert telemetry on db");
        }
    }

    public void insert(AirPowerDevice device) {
        try {
            mDeviceDAO.insert(device);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't insert device on db");
            AirPowerLog.e(TAG, e.toString());
        }
    }

    public void update(AirPowerDevice device) {
        try {
            mDeviceDAO.update(device);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't update device from db");
        }
    }

    public void update(DeviceTelemetry telemetry) {
        try {
            mTelemetryDAO.update(telemetry);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't update device from db");
        }
    }

    public void delete(DeviceTelemetry telemetry) {
        try {
            mTelemetryDAO.delete(telemetry);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't delete device from db");
        }
    }

    public void delete(AirPowerDevice device) {
        try {
            mDeviceDAO.delete(device);
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't delete device from db");
        }
    }

    public List<AirPowerDevice> getDevices() {
        try {
            return mDeviceDAO.getDevices();
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get devices from db");
        }
        return null;
    }

    public List<DeviceTelemetry> getTelemetries() {
        try {
            return mTelemetryDAO.getAllTelemetry();
        } catch (Exception e) {
            if(AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get telemetries from db");
        }
        return null;
    }
}