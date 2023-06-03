package recife.ifpe.edu.airpower.model.repo.model;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public class DeviceMeasurement {
    private int x;
    private int y;

    public DeviceMeasurement() {
    }

    public DeviceMeasurement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}