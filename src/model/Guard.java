package model;

public class Guard {

    private int guardId;
    private String fullName;
    private String badgeNumber;

    public Guard(int guardId, String fullName, String badgeNumber) {
        this.guardId = guardId;
        this.fullName = fullName;
        this.badgeNumber = badgeNumber;
    }
    
    public Guard(int guardId, String fullName) {
        this.guardId = guardId;
        this.fullName = fullName;
        this.badgeNumber = "OFFICER-ACTIVE";
    }

    public int getGuardId() {
        return guardId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }
}