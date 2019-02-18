package org.firstinspires.ftc.teamcode.robot;

/**
 * This enumerator provides the autonomous functions
 * @author Henry
 * @version 1.1
 */
public enum AutoDrive {
    FORWARD("Forward"), BACKWARD("Backward"), SIDEWAYS("Sideways");

    private final String description;

    AutoDrive(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
