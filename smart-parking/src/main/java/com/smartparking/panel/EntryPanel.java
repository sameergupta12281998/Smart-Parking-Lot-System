package com.smartparking.panel;

import com.smartparking.model.Vehicle;
import com.smartparking.service.ParkingService;
import com.smartparking.model.ParkingTransaction;

import java.util.Optional;

public class EntryPanel {
    private final ParkingService parkingService;

    public EntryPanel(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * Processes vehicle entry and returns the created ParkingTransaction if a spot was assigned.
     */
    public Optional<ParkingTransaction> processVehicleEntry(Vehicle vehicle) {
        Optional<ParkingTransaction> txOpt = parkingService.checkIn(vehicle);
        if (txOpt.isPresent()) {
            ParkingTransaction tx = txOpt.get();
            System.out.println("[ENTRY PANEL] Vehicle " + vehicle.getPlate() +
                    " assigned to Spot: " + tx.getSpotId() +
                    " (Transaction ID: " + tx.getId() + ")");
            return txOpt;
        } else {
            System.out.println("[ENTRY PANEL] No available spot for vehicle " + vehicle.getPlate());
            return Optional.empty();
        }
    }
}
