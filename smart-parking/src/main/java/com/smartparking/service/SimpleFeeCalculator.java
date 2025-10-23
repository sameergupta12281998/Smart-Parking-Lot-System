package com.smartparking.service;

import com.smartparking.model.ParkingTransaction;
import java.time.Duration;
import java.time.Instant;

public class SimpleFeeCalculator implements FeeCalculator {

    @Override
    public long calculateFeeCents(ParkingTransaction tx) {
        Instant in = tx.getEntryTs();
        Instant out = tx.getExitTs();
        if (out == null) out = Instant.now();

        long minutes = Duration.between(in, out).toMinutes();

        if (minutes <= 60) {
            return 80; 
        } else {
            long extraMinutes = minutes - 60;
            long extraHours = (extraMinutes + 59) / 60; // round up to next hour
            return 80 + (extraHours * 40); // ₹40 per extra hour
        }
    }
}
