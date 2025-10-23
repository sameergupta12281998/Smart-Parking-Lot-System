package com.smartparking.repo;

import com.smartparking.model.Vehicle;

import java.util.Optional;

public interface VehicleRepository {
    Optional<Vehicle> findByPlate(String plate);
    void save(Vehicle v);
}
