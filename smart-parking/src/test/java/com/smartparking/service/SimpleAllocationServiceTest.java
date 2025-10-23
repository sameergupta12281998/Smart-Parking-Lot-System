package com.smartparking.service;

import com.smartparking.model.*;
import com.smartparking.repo.InMemorySpotRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimpleAllocationServiceTest {

    @Test
    void motorcyclePrefersMotorcycleSpot() {
        InMemorySpotRepository repo = new InMemorySpotRepository();
        // seed: one motorcycle spot and one compact
        repo.save(new Spot("F1-M01", 1, SpotSize.MOTORCYCLE));
        repo.save(new Spot("F1-C01", 1, SpotSize.COMPACT));

        SimpleAllocationService alloc = new SimpleAllocationService(repo);
        Vehicle m = new Vehicle("MOTO-1", VehicleType.MOTORCYCLE);

        Optional<Spot> spotOpt = alloc.allocate(m);
        assertTrue(spotOpt.isPresent(), "Motorcycle should be allocated a spot");
        assertEquals(SpotSize.MOTORCYCLE, spotOpt.get().getSize(), "Motorcycle must get a MOTORCYCLE-sized spot first");
        assertEquals(SpotStatus.OCCUPIED, spotOpt.get().getStatus());
    }

    @Test
    void carGetsCompactIfAvailable() {
        InMemorySpotRepository repo = new InMemorySpotRepository();
        repo.save(new Spot("F1-C01", 1, SpotSize.COMPACT));
        repo.save(new Spot("F1-R01", 1, SpotSize.REGULAR));

        SimpleAllocationService alloc = new SimpleAllocationService(repo);
        Vehicle car = new Vehicle("CAR-1", VehicleType.CAR);

        Optional<Spot> spotOpt = alloc.allocate(car);
        assertTrue(spotOpt.isPresent());
        assertEquals(SpotSize.COMPACT, spotOpt.get().getSize());
        assertEquals(SpotStatus.OCCUPIED, spotOpt.get().getStatus());
    }

    @Test
    void noSpotAvailableReturnsEmpty() {
        InMemorySpotRepository repo = new InMemorySpotRepository(); // empty
        SimpleAllocationService alloc = new SimpleAllocationService(repo);
        Vehicle v = new Vehicle("V1", VehicleType.CAR);
        Optional<Spot> spotOpt = alloc.allocate(v);
        assertFalse(spotOpt.isPresent());
    }
}
