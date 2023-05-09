package recife.ifpe.edu.airpower.model.repo.model;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DEVICE_TELEMETRY")
public class DeviceTelemetry {

    @PrimaryKey(autoGenerate = true)
    private int mID;
    @ColumnInfo(name = "TELEMETRY_TOKEN")
    private String mToken;
    @ColumnInfo(name = "TELEMETRY_VOLTAGE")
    private String mVoltage;
    @ColumnInfo(name = "TELEMETRY_CURRENT")
    private String mCurrent;

    public DeviceTelemetry() {
    }

    public DeviceTelemetry(int mID, String mToken, String mVoltage, String mCurrent) {
        this.mID = mID;
        this.mToken = mToken;
        this.mVoltage = mVoltage;
        this.mCurrent = mCurrent;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getVoltage() {
        return mVoltage;
    }

    public void setVoltage(String mVoltage) {
        this.mVoltage = mVoltage;
    }

    public String getCurrent() {
        return mCurrent;
    }

    public void setCurrent(String mCurrent) {
        this.mCurrent = mCurrent;
    }
}