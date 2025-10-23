package com.smartparking.service;

import com.smartparking.model.Spot;
import com.smartparking.model.SpotSize;
import com.smartparking.model.Vehicle;
import com.smartparking.model.VehicleType;
import com.smartparking.repo.SpotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SimpleAllocationService implements AllocationService {
    private final SpotRepository spotRepo;

    public SimpleAllocationService(SpotRepository spotRepo) { this.spotRepo = spotRepo; }

    @Override
    public Optional<Spot> allocate(Vehicle vehicle) {
        List<SpotSize> candidates = allowedSizes(vehicle.getType());
        for (SpotSize size : candidates) {
            List<Spot> spots = spotRepo.findAvailableBySize(size);
            for (Spot s : spots) {
                if (s.tryOccupy()) {
                    return Optional.of(s);
                }
            }
        }
        return Optional.empty();
    }

    private List<SpotSize> allowedSizes(VehicleType type) {
        List<SpotSize> l = new ArrayList<>();
        switch (type) {
            case MOTORCYCLE:
                l.add(SpotSize.MOTORCYCLE);
                l.add(SpotSize.COMPACT);
                l.add(SpotSize.REGULAR);
                break;
            case CAR:
                l.add(SpotSize.COMPACT);
                l.add(SpotSize.REGULAR);
                l.add(SpotSize.LARGE);
                break;
            case BUS:
                l.add(SpotSize.LARGE);
                break;
        }
        return l;
    }
}
