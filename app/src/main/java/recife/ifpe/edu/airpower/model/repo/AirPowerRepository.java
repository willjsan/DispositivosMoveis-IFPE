package recife.ifpe.edu.airpower.model.repo;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.database.AirPowerDatabase;
import recife.ifpe.edu.airpower.model.repo.database.EntitiesDAO;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;
import recife.ifpe.edu.airpower.model.repo.model.group.Group;
import recife.ifpe.edu.airpower.util.AirPowerLog;
import recife.ifpe.edu.airpower.util.AirPowerUtil;

public class AirPowerRepository {
    private static String TAG = AirPowerRepository.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static AirPowerRepository instance;
    private Group mCurrentGroup;
    private final EntitiesDAO mDeviceDAO;
    private final Context mContext;
    static final String PREF_USER_STATE = "airpowerapp.prefuserstate";
    static final String KEY_USER_STATE = "isUserLoggedIn";
    static final String KEY_AUTH_TIMESTAMP = "authTimestamp";

    private AirPowerRepository(Context context) {
        AirPowerDatabase db = AirPowerDatabase.getDataBaseInstance(context);
        mDeviceDAO = db.getDeviceDAOInstance();
        mContext = context;
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

    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_USER_STATE, Context.MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean(KEY_USER_STATE, false);
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "isUserLoggedIn:" + result);
        return result;
    }

    public void setSessionStatus(boolean isLoggedIn) {
        String timestamp = AirPowerUtil.getCurrentDateTime();
        SharedPreferences sharedPreferences =
                mContext.getSharedPreferences(PREF_USER_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_STATE, isLoggedIn);
        editor.putString(KEY_AUTH_TIMESTAMP, timestamp);
        editor.apply();
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "setSessionStatus:" + isLoggedIn + " timestamp:" + timestamp);
    }
}