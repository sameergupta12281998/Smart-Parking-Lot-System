package com.smartparking.repo;

import com.smartparking.model.ParkingTransaction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<String, ParkingTransaction> txMap = new ConcurrentHashMap<>();

    @Override
    public void save(ParkingTransaction tx) {
        txMap.put(tx.getId(), tx);
    }

    @Override
    public Optional<ParkingTransaction> findById(String id) {
        return Optional.ofNullable(txMap.get(id));
    }

    @Override
    public List<ParkingTransaction> findAll() {
        return new ArrayList<>(txMap.values());
    }

    @Override
    public Optional<ParkingTransaction> findActiveByPlate(String plate) {
        if (plate == null) return Optional.empty();
        return txMap.values().stream()
                .filter(tx -> plate.equals(tx.getVehiclePlate()) && tx.getExitTs() == null)
                .findFirst();
    }
}
