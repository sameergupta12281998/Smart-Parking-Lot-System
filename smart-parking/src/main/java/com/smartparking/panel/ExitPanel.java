package com.smartparking.panel;

import com.smartparking.model.ParkingTransaction;
import com.smartparking.service.ParkingService;
import java.util.Optional;

public class ExitPanel {
    private final ParkingService parkingService;

    public ExitPanel(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    public Optional<ParkingTransaction> processVehicleExit(String transactionId) {
        Optional<ParkingTransaction> txOpt = parkingService.checkOut(transactionId);
        if (txOpt.isPresent()) {
            ParkingTransaction tx = txOpt.get();
            System.out.println("[EXIT PANEL] Vehicle " + tx.getVehiclePlate() +
                    " exited. Fee: ₹" + (tx.getFeeCents() ) +
                    " (txId=" + tx.getId() + ")");
            return txOpt;
        } else {
            System.out.println("[EXIT PANEL] Invalid transaction ID: " + transactionId);
            return Optional.empty();
        }
    }

    // NEW: allow exit by plate
    public Optional<ParkingTransaction> processVehicleExitByPlate(String plate) {
        Optional<ParkingTransaction> txOpt = parkingService.checkOutByPlate(plate);
        if (txOpt.isPresent()) {
            ParkingTransaction tx = txOpt.get();
            System.out.println("[EXIT PANEL] Vehicle " + tx.getVehiclePlate() +
                    " exited. Fee: ₹" + (tx.getFeeCents()) +
                    " (txId=" + tx.getId() + ")");
            return txOpt;
        } else {
            System.out.println("[EXIT PANEL] No active transaction for plate: " + plate);
            return Optional.empty();
        }
    }
}
