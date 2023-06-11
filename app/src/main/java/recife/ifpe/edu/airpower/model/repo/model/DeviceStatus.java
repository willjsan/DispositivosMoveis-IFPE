package recife.ifpe.edu.airpower.model.repo.model;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public class DeviceStatus {

    private String statusMessage;
    private String issuesValue;
    private boolean isActivated;
    private int failLevel;

    public DeviceStatus() {
    }

    public DeviceStatus(String statusMessage,
                        String issuesValue,
                        boolean isActivated,
                        int failLevel) {
        this.statusMessage = statusMessage;
        this.issuesValue = issuesValue;
        this.isActivated = isActivated;
        this.failLevel = failLevel;
    }

    public int getFailLevel() {
        return failLevel;
    }

    public void setFailLevel(int failLevel) {
        this.failLevel = failLevel;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getIssuesValue() {
        return issuesValue;
    }

    public void setIssuesValue(String issuesValue) {
        this.issuesValue = issuesValue;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
