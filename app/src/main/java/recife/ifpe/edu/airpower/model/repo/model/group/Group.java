package recife.ifpe.edu.airpower.model.repo.model.group;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;

@Entity(tableName = "AIR_POWER_GROUP")
public class Group {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "GROUP_ID")
    private int id;
    @ColumnInfo(name = "GROUP_DEVICES")
    private List<AirPowerDevice> devices;
    @ColumnInfo(name = "GROUP_NAME")
    private String name;
    @ColumnInfo(name = "GROUP_DESCRIPTION")
    private String description;
    @ColumnInfo(name = "GROUP_LOCALIZATION")
    private String localization;
    @ColumnInfo(name = "GROUP_ICON")
    private String icon;

    public Group() {
    }

    @Ignore
    public Group(int id,
                 List<AirPowerDevice> devices,
                 String name,
                 String description,
                 String localization,
                 String icon) {
        this.id = id;
        this.devices = devices;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.icon = icon;
    }

    @Ignore
    public Group(List<AirPowerDevice> devices,
                 String name,
                 String description,
                 String localization,
                 String icon) {
        this.devices = devices;
        this.name = name;
        this.description = description;
        this.localization = localization;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<AirPowerDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<AirPowerDevice> devices) {
        this.devices = devices;
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
        return this.icon;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", devices=" + devices +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", localization='" + localization + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
