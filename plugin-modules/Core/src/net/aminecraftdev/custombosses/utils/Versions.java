package net.aminecraftdev.custombosses.utils;

import lombok.Getter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public enum Versions {

    v1_7_R3(1, "1.7.9"),
    v1_7_R4(2, "1.7.10"),
    v1_8_R1(3, "1.8"),
    v1_8_R2(4, "1.8.3"),
    v1_8_R3(5, "1.8.9"),
    v1_9_R1(6, "1.9"),
    v1_9_R2(7, "1.9.4"),
    v1_10_R1(8, "1.10"),
    v1_11_R1(9, "1.11.2"),
    v1_12_R1(10, "1.12.1"),
    v1_13_R1(11, "1.13");

    @Getter private String displayVersion, bukkitVersion;
    private int weight;

    Versions(int weight, String displayVersion) {
        this.weight = weight;
        this.displayVersion = displayVersion;
        this.bukkitVersion = name();
    }

    public boolean isLessThan(Versions input) {
        return this.weight < input.weight;
    }

    public boolean isLessThanOrEqualTo(Versions input) {
        return this.weight <= input.weight;
    }

    public boolean isHigherThanOrEqualTo(Versions input) {
        return this.weight >= input.weight;
    }

    public boolean isHigherThan(Versions input) {
        return this.weight > input.weight;
    }

    public static Versions getVersion(String input) {
        for(Versions versions : values()) {
            if(versions.getBukkitVersion().equalsIgnoreCase(input)) {
                return versions;
            }
        }

        return null;
    }

}
