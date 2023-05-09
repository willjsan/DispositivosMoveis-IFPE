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
                new AirPowerDevice("220", "4.5",
                        "-19483738378,-475637467", "air_conditioner_icon",
                        "Ar Condicionado", "Um ar"),
                new AirPowerDevice("110", "3.7",
                        "-19483738378,-475637467", "air_conditioner_icon",
                        "Chuveiro", "Um chuveiro"),
                new AirPowerDevice( "127", "7.0",
                        "-19483738378,-475637467", "air_conditioner_icon",
                        "Condicionado 2", "Um outro Ar"),
                new AirPowerDevice( "127", "7.0",
                        "-19483738378,-475637467", "air_conditioner_icon",
                        "Condicionado 3", "Mais um Ar"),
                new AirPowerDevice( "127", "7.0",
                        "-19483738378,-475637467", "air_conditioner_icon",
                        "Condicionador 4", "Outro Ar")
        ));
    }
}