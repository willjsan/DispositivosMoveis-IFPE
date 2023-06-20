package recife.ifpe.edu.airpower.model.repo.model.device;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public class DeviceStatus {

    private String statusMessage;
    private int issuesCount;
    private boolean isActivated = true; // TODO remover "= true"
    private int failLevel;

    public DeviceStatus() {
    }

    public DeviceStatus(String statusMessage,
                        int issuesValue,
                        boolean isActivated,
                        int failLevel) {
        this.statusMessage = statusMessage;
        this.issuesCount = issuesValue;
        this.isActivated = isActivated;
        this.failLevel = failLevel;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getIssuesCount() {
        return issuesCount;
    }

    public void setIssuesCount(int issuesCount) {
        this.issuesCount = issuesCount;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
        isActivated = true;  // TODO remover
    }

    public int getFailLevel() {
        return failLevel;
    }

    public void setFailLevel(int failLevel) {
        this.failLevel = failLevel;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "statusMessage='" + statusMessage + '\'' +
                ", issuesCount=" + issuesCount +
                ", isActivated=" + isActivated +
                ", failLevel=" + failLevel +
                '}';
    }
}
