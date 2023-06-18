package recife.ifpe.edu.airpower.model.repo;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.database.AirPowerDatabase;
import recife.ifpe.edu.airpower.model.repo.database.DeviceDAO;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class AirPowerRepository {
    private static String TAG = AirPowerRepository.class.getSimpleName();
    private static AirPowerRepository instance;
    private Group mCurrentGroup;
    private final DeviceDAO mDeviceDAO;

    private AirPowerRepository(Context context) {
        AirPowerDatabase mDb = AirPowerDatabase.getDataBaseInstance(context);
        mDeviceDAO = mDb.getDeviceDAOInstance();
        List<Group> groups = mDeviceDAO.getGroups();
        if (groups == null || groups.isEmpty()) {
            createDefaultGroup();
        }
        mCurrentGroup = mDeviceDAO.getGroups().get(0);
    }

    private void createDefaultGroup() {
        Group group = new Group();
        group.setName("Default");
        group.setDescription("My default group");
        group.setIcon("airpower_launcher_icon");
        group.setDevices(mDeviceDAO.getDevices());
        mDeviceDAO.insert(group);
    }

    private void setCurrentGroup(Group group) {
        this.mCurrentGroup = group;
    }

    private Group getCurrentGroup() {
        return this.mCurrentGroup;
    }

    public static AirPowerRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AirPowerRepository(context);
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.d(TAG, "AirPowerRepository instantiation");
        }
        return instance;
    }

    public void insert(AirPowerDevice device) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "insert AirPowerDevice");
        try {
            mDeviceDAO.insert(device);
            AirPowerLog.w(TAG, device.toString()); // TODO remove it
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't insert device on db");
            AirPowerLog.e(TAG, e.toString());
        }
    }

    public void insert(Group group) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "insert Group");
        try {
            mDeviceDAO.insert(group);
            AirPowerLog.w(TAG, group.toString()); // TODO remove it
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't Group on db");
            AirPowerLog.e(TAG, e.toString());
        }
    }

    public void update(AirPowerDevice device) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "update");
        try {
            mDeviceDAO.update(device);
            AirPowerLog.w(TAG, device.toString()); // TODO remove it
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't update device from db");
        }
    }

    public void delete(AirPowerDevice device) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "delete");
        try {
            mDeviceDAO.delete(device);
            AirPowerLog.w(TAG, device.toString()); // TODO remove it
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't delete device from db");
        }
    }

    public List<AirPowerDevice> getDevices() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getDevices");
        try {
            return mDeviceDAO.getDevices();
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get devices from db");
            AirPowerLog.e(TAG, e.getMessage());
        }
        return null;
    }

    public AirPowerDevice getDeviceById(int id) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getDeviceById");
        try {
            AirPowerLog.w(TAG, mDeviceDAO.getDeviceById(id).toString()); // TODO remove it
            return mDeviceDAO.getDeviceById(id);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get device by id from db");
            AirPowerLog.e(TAG, e.getMessage());
        }
        return null;
    }


    public Group getGroupById(int id) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getGroupById");
        try {
            AirPowerLog.w(TAG, mDeviceDAO.getGroupById(id).toString()); // TODO remove it
            return mDeviceDAO.getGroupById(id);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get group by id from db");
            AirPowerLog.e(TAG, e.getMessage());
        }
        return null;
    }
    public List<Group> getGroups() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getGroups");
        try {
            return mDeviceDAO.getGroups();
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Couldn't get groups from db");
            AirPowerLog.e(TAG, e.getMessage());
        }
        return null;
    }
}