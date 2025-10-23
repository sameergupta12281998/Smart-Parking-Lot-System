package com.smartparking.service;

import com.smartparking.model.ParkingTransaction;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFeeCalculatorTest {

    private final SimpleFeeCalculator calc = new SimpleFeeCalculator();

    @Test
    void upToOneHourShouldCharge80Rupees() {
        Instant in = Instant.parse("2025-10-11T08:00:00Z");
        Instant out = Instant.parse("2025-10-11T08:45:00Z"); // 45 minutes
        ParkingTransaction tx = new ParkingTransaction("KA01AB1234", "F1-C01", in);
        tx.checkOut(out, 0L); // set exit time and placeholder fee

        long fee = calc.calculateFeeCents(tx);
        assertEquals(80L, fee, "45 minutes should cost ₹80");
    }

    @Test
    void exactOneHourShouldCharge80Rupees() {
        Instant in = Instant.parse("2025-10-11T08:00:00Z");
        Instant out = Instant.parse("2025-10-11T09:00:00Z");
        ParkingTransaction tx = new ParkingTransaction("KA01AB1234", "F1-C01", in);
        tx.checkOut(out, 0L);

        long fee = calc.calculateFeeCents(tx);
        assertEquals(80L, fee, "Exactly 60 minutes should cost ₹80");
    }

    @Test
    void overOneHourShouldChargePerExtraHourRoundedUp() {
        Instant in = Instant.parse("2025-10-11T08:00:00Z");
        Instant out = Instant.parse("2025-10-11T09:20:00Z");
        ParkingTransaction tx = new ParkingTransaction("KA01AB1234", "F1-C01", in);
        tx.checkOut(out, 0L);

        long fee = calc.calculateFeeCents(tx);
        // ₹80 + ₹40 = ₹120 
        assertEquals(120L, fee, "1h20m should cost ₹120");
    }
}
