package com.smartparking.service;

import com.smartparking.model.ParkingTransaction;
import com.smartparking.model.Spot;
import com.smartparking.model.Vehicle;
import com.smartparking.repo.TransactionRepository;
import com.smartparking.repo.VehicleRepository;

import java.time.Instant;
import java.util.Optional;

public class ParkingService {
    private final AllocationService allocationService;
    private final FeeCalculator feeCalculator;
    private final TransactionRepository txRepo;
    private final VehicleRepository vehicleRepo;

    public ParkingService(AllocationService allocationService,
                          FeeCalculator feeCalculator,
                          TransactionRepository txRepo,
                          VehicleRepository vehicleRepo) {
        this.allocationService = allocationService;
        this.feeCalculator = feeCalculator;
        this.txRepo = txRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public Optional<ParkingTransaction> checkIn(Vehicle v) {
        // save vehicle if new
        vehicleRepo.findByPlate(v.getPlate()).or(() -> {
            vehicleRepo.save(v);
            return Optional.of(v);
        });

        Optional<Spot> allocated = allocationService.allocate(v);
        if (allocated.isEmpty()) return Optional.empty();

        Spot spot = allocated.get();
        ParkingTransaction tx = new ParkingTransaction(v.getPlate(), spot.getId(), Instant.now());
        txRepo.save(tx);
        return Optional.of(tx);
    }

    public Optional<ParkingTransaction> checkOut(String txId) {
        Optional<ParkingTransaction> opt = txRepo.findById(txId);
        if (opt.isEmpty()) return Optional.empty();
        ParkingTransaction tx = opt.get();
        tx.checkOut(java.time.Instant.now(), 0L);
        long fee = feeCalculator.calculateFeeCents(tx);
        tx.checkOut(tx.getExitTs(), fee);
        txRepo.save(tx);
        return Optional.of(tx);
    }

    // NEW: checkout using vehicle plate (find active tx then delegate)
    public Optional<ParkingTransaction> checkOutByPlate(String plate) {
        Optional<ParkingTransaction> active = txRepo.findActiveByPlate(plate);
        if (active.isEmpty()) return Optional.empty();
        return checkOut(active.get().getId());
    }
}
