package recife.ifpe.edu.airpower.model;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public class AirPowerDevice {

    private int mId;
    private String mVoltage;
    private String mCurrent;
    private String mLocalization;
    private String mIcon;
    private String mName;
    private String mDescription;

    public AirPowerDevice() {
    }

    public AirPowerDevice(int mId, String mVoltage, String mCurrent, String mLocalization,
                          String mIcon, String mName, String description) {
        this.mId = mId;
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
