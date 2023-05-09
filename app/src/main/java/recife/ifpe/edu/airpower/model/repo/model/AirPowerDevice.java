package recife.ifpe.edu.airpower.model.repo.model;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AIR_POWER_DEVICE")
public class AirPowerDevice {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "DEVICE_VOLTAGE")
    private String mVoltage;
    @ColumnInfo(name = "DEVICE_CURRENT")
    private String mCurrent;
    @ColumnInfo(name = "DEVICE_LOCALIZATION")
    private String mLocalization;
    @ColumnInfo(name = "DEVICE_ICON")
    private String mIcon;
    @ColumnInfo(name = "DEVICE_NAME")
    private String mName;
    @ColumnInfo(name = "DEVICE_DESCRIPTION")
    private String mDescription;

    public AirPowerDevice() {
    }

    public AirPowerDevice(String name, String description, String localization, String icon){
        this.mName = name;
        this.mDescription = description;
        this.mLocalization = localization;
        this.mIcon = icon;
    }

    public AirPowerDevice(String mVoltage, String mCurrent, String mLocalization,
                          String mIcon, String mName, String description) {
        this.mVoltage = mVoltage;
        this.mCurrent = mCurrent;
        this.mLocalization = mLocalization;
        this.mIcon = mIcon;
        this.mName = mName;
        this.mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
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

    public String getLocalization() {
        return mLocalization;
    }

    public void setLocalization(String mLocalization) {
        this.mLocalization = mLocalization;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
