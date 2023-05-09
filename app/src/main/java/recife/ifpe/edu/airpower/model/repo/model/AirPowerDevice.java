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
    @ColumnInfo(name = "DEVICE_NAME")
    private String mName;
    @ColumnInfo(name = "DEVICE_DESCRIPTION")
    private String mDescription;
    @ColumnInfo(name = "DEVICE_LOCALIZATION")
    private String mLocalization;
    @ColumnInfo(name = "DEVICE_ICON")
    private String mIcon;
    @ColumnInfo(name = "DEVICE_TOKEN")
    private String mToken;

    public AirPowerDevice() {
    }

    public AirPowerDevice(String mName, String mDescription, String mLocalization,
                          String mIcon, String mToken) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mLocalization = mLocalization;
        this.mIcon = mIcon;
        this.mToken = mToken;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
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

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
