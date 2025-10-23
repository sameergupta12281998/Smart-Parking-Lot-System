package com.smartparking.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ParkingTransaction {
    private final String id = UUID.randomUUID().toString();
    private final String vehiclePlate;
    private final String spotId;
    private final Instant entryTs;
    private Instant exitTs;
    private long feeCents;

    public ParkingTransaction(String vehiclePlate, String spotId, Instant entryTs) {
        this.vehiclePlate = Objects.requireNonNull(vehiclePlate);
        this.spotId = Objects.requireNonNull(spotId);
        this.entryTs = Objects.requireNonNull(entryTs);
    }

    public String getId() { return id; }
    public String getVehiclePlate() { return vehiclePlate; }
    public String getSpotId() { return spotId; }
    public Instant getEntryTs() { return entryTs; }
    public Instant getExitTs() { return exitTs; }
    public long getFeeCents() { return feeCents; }

    public void checkOut(Instant exitTs, long feeCents) {
        this.exitTs = Objects.requireNonNull(exitTs);
        this.feeCents = feeCents;
    }

    @Override
    public String toString() {
        return "ParkingTransaction{" + id + ", plate=" + vehiclePlate + ", spot=" + spotId + ", entry=" + entryTs + ", exit=" + exitTs + ", fee=" + feeCents + '}';
    }
}
