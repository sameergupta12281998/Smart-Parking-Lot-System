package com.smartparking.repo;

import com.smartparking.model.ParkingTransaction;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionRepositoryTest {

    @Test
    void saveAndFindBehavior() {
        InMemoryTransactionRepository repo = new InMemoryTransactionRepository();
        ParkingTransaction tx = new ParkingTransaction("KA01ABC", "F1-C01", Instant.now());
        repo.save(tx);

        Optional<ParkingTransaction> f = repo.findById(tx.getId());
        assertTrue(f.isPresent());
        assertEquals(tx.getId(), f.get().getId());

        List<ParkingTransaction> all = repo.findAll();
        assertTrue(all.stream().anyMatch(t -> t.getId().equals(tx.getId())));
    }

    @Test
    void findActiveByPlateReturnsOnlyActive() {
        InMemoryTransactionRepository repo = new InMemoryTransactionRepository();
        ParkingTransaction tx1 = new ParkingTransaction("KA01ABC", "F1-C01", Instant.now());
        repo.save(tx1);

        // simulate checkout
        tx1.checkOut(Instant.now(), 100L);
        repo.save(tx1);

        Optional<ParkingTransaction> active = repo.findActiveByPlate("KA01ABC");
        assertFalse(active.isPresent(), "There should be no active transaction for plate after checkout");
    }
}
