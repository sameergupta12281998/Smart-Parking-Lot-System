package com.smartparking.service;

import com.smartparking.model.*;
import com.smartparking.repo.InMemorySpotRepository;
import com.smartparking.repo.InMemoryTransactionRepository;
import com.smartparking.repo.InMemoryVehicleRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParkingServiceTest {

    @Test
    void checkInCreatesTransactionAndOccupiesSpot() {
        InMemorySpotRepository spotRepo = new InMemorySpotRepository();
        spotRepo.saveAll(List.of(
                new Spot("F1-C01", 1, SpotSize.COMPACT)
        ));
        InMemoryTransactionRepository txRepo = new InMemoryTransactionRepository();
        InMemoryVehicleRepository vehicleRepo = new InMemoryVehicleRepository();

        AllocationService alloc = new SimpleAllocationService(spotRepo);
        FeeCalculator feeCalc = new SimpleFeeCalculator();
        ParkingService parkingService = new ParkingService(alloc, feeCalc, txRepo, vehicleRepo);

        Vehicle car = new Vehicle("KA01AB0001", VehicleType.CAR);
        Optional<ParkingTransaction> opt = parkingService.checkIn(car);
        assertTrue(opt.isPresent());
        ParkingTransaction tx = opt.get();

        // tx stored
        Optional<ParkingTransaction> stored = txRepo.findById(tx.getId());
        assertTrue(stored.isPresent());
        // spot occupied
        spotRepo.findById(tx.getSpotId()).ifPresent(spot -> assertEquals(SpotStatus.OCCUPIED, spot.getStatus()));
    }

    @Test
    void checkOutByPlateCalculatesFeeAndSetsExitTs() throws InterruptedException {
        InMemorySpotRepository spotRepo = new InMemorySpotRepository();
        spotRepo.saveAll(List.of(new Spot("F1-C01", 1, SpotSize.COMPACT)));
        InMemoryTransactionRepository txRepo = new InMemoryTransactionRepository();
        InMemoryVehicleRepository vehicleRepo = new InMemoryVehicleRepository();

        AllocationService alloc = new SimpleAllocationService(spotRepo);
        FeeCalculator feeCalc = new SimpleFeeCalculator();
        ParkingService parkingService = new ParkingService(alloc, feeCalc, txRepo, vehicleRepo);

        Vehicle car = new Vehicle("KA01AB0002", VehicleType.CAR);
        Optional<ParkingTransaction> cin = parkingService.checkIn(car);
        assertTrue(cin.isPresent());
        String plate = car.getPlate();

        // simulate some time passing by changing entryTs directly (for determinism)
        ParkingTransaction tx = txRepo.findActiveByPlate(plate).orElseThrow();
        // adjust entry time to 2 hours ago so fee > base
        Instant fakeEntry = Instant.now().minusSeconds(2 * 3600L);
    
        ParkingTransaction olderTx = new ParkingTransaction(plate, tx.getSpotId(), fakeEntry);
        txRepo.save(olderTx);

        Optional<ParkingTransaction> cout = parkingService.checkOutByPlate(plate);
        assertTrue(cout.isPresent());
        ParkingTransaction checked = cout.get();
        assertNotNull(checked.getExitTs(), "Exit timestamp should be set");
        assertTrue(checked.getFeeCents() >= 80L, "Fee should be at least base ₹80");
    }
}
