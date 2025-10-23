package com.smartparking.service;

import com.smartparking.model.ParkingTransaction;

public interface FeeCalculator {
    long calculateFeeCents(ParkingTransaction tx);
}
