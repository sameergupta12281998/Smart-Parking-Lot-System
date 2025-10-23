package com.smartparking.repo;

import com.smartparking.model.Vehicle;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryVehicleRepository implements VehicleRepository {
    private final ConcurrentMap<String, Vehicle> map = new ConcurrentHashMap<>();

    @Override
    public Optional<Vehicle> findByPlate(String plate) { return Optional.ofNullable(map.get(plate)); }

    @Override
    public void save(Vehicle v) { map.put(v.getPlate(), v); }
}
