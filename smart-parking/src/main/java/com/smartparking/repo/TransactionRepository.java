package com.smartparking.repo;

import com.smartparking.model.ParkingTransaction;
import java.util.*;

public interface TransactionRepository {
    void save(ParkingTransaction tx);
    Optional<ParkingTransaction> findById(String id);
    List<ParkingTransaction> findAll();

    // NEW: find the active (not checked-out) transaction for a plate, if any
    Optional<ParkingTransaction> findActiveByPlate(String plate);
}
