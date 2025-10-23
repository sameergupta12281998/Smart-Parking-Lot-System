package com.smartparking.service;

import com.smartparking.model.Vehicle;
import com.smartparking.model.Spot;

import java.util.Optional;

public interface AllocationService {
    Optional<Spot> allocate(Vehicle vehicle);
}
