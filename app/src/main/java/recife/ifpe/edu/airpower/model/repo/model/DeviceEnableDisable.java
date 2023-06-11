package recife.ifpe.edu.airpower.model.repo.model;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public class DeviceEnableDisable {
    private AirPowerDevice device;
    private boolean isActivated;

    public DeviceEnableDisable() {
    }

    public DeviceEnableDisable(AirPowerDevice device, boolean isActivated) {
        this.device = device;
        this.isActivated = isActivated;
    }

    public AirPowerDevice getDevice() {
        return device;
    }

    public void setDevice(AirPowerDevice device) {
        this.device = device;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
