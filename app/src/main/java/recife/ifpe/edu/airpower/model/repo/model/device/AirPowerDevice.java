package recife.ifpe.edu.airpower.model.repo.model.device;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AIR_POWER_DEVICE")
public class AirPowerDevice {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "DEVICE_ID")
    private int id;
    @ColumnInfo(name = "DEVICE_NAME")
    private String name;
    @ColumnInfo(name = "DEVICE_DESCRIPTION")
    private String description;
    @ColumnInfo(name = "DEVICE_LOCALIZATION")
    private String localization;
    @ColumnInfo(name = "DEVICE_ICON")
    private String icon;
    @ColumnInfo(name = "DEVICE_TOKEN")
    private String token;
    @ColumnInfo(name = "DEVICE_SSID")
    private String deviceSSID;
    @ColumnInfo(name = "DEVICE_PASSWORD")
    private String devicePassword;
    @ColumnInfo(name = "DEVICE_URL")
    private String deviceURL;

    public AirPowerDevice() {
    }

    /**
     * NEVER use this constructor if
     * you will STORE the device on DB.
     * The db is set to AUTO MANAGE devices ids.
     *
     * @param id don't create new ids for devices
     * @param name of device
     * @param description of device
     * @param localization of device
     * @param icon of device
     * @param token of device
     * @param deviceSSID of device
     * @param devicePassword of device
     * @param deviceURL of device
     */
    @Ignore
    public AirPowerDevice(int id, String name, String description, String localization,
                          String icon, String token, String deviceSSID,
                          String devicePassword, String deviceURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.icon = icon;
        this.token = token;
        this.deviceSSID = deviceSSID;
        this.devicePassword = devicePassword;
        this.deviceURL = deviceURL;
    }

    @Ignore
    public AirPowerDevice(String name,
                          String description,
                          String localization,
                          String icon,
                          String token,
                          String deviceSSID,
                          String devicePassword,
                          String deviceURL) {
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.icon = icon;
        this.token = token;
        this.deviceSSID = deviceSSID;
        this.devicePassword = devicePassword;
        this.deviceURL = deviceURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceSSID() {
        return deviceSSID;
    }

    public void setDeviceSSID(String deviceSSID) {
        this.deviceSSID = deviceSSID;
    }

    public String getDevicePassword() {
        return devicePassword;
    }

    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    public String getDeviceURL() {
        return deviceURL;
    }

    public void setDeviceURL(String deviceURL) {
        this.deviceURL = deviceURL;
    }

    @Override
    public String toString() {
        return "AirPowerDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", localization='" + localization + '\'' +
                ", icon='" + icon + '\'' +
                ", token='" + token + '\'' +
                ", deviceSSID='" + deviceSSID + '\'' +
                ", devicePassword='" + devicePassword + '\'' +
                ", deviceURL='" + deviceURL + '\'' +
                '}';
    }
}