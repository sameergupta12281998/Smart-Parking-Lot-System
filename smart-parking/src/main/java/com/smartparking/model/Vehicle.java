package com.smartparking.model;

import java.util.Objects;

public final class Vehicle {
    private final String plate;
    private final VehicleType type;

    public Vehicle(String plate, VehicleType type) {
        this.plate = Objects.requireNonNull(plate);
        this.type = Objects.requireNonNull(type);
    }

    public String getPlate() { return plate; }
    public VehicleType getType() { return type; }

    @Override
    public String toString() { return "Vehicle{" + plate + "," + type + '}'; }
}
