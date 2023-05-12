package recife.ifpe.edu.airpower.model;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;

public class AirPowerDeviceDAO implements Serializable {

    public List<AirPowerDevice> getDevices() {
        return new ArrayList<>(Arrays.asList(
                new AirPowerDevice("Device 1", "Device1", "111,1111", "air_conditioner_icon", "01010"),
                new AirPowerDevice("Device 2", "Device2", "111,1111", "air_conditioner_icon", "01010"),
                new AirPowerDevice("Device 3", "Device3", "111,1111", "air_conditioner_icon", "01010"),
                new AirPowerDevice("Device 4", "Device4", "111,1111", "air_conditioner_icon", "01010"),
                new AirPowerDevice("Device 5", "Device5", "111,1111", "air_conditioner_icon", "01010")
        ));
    }
}