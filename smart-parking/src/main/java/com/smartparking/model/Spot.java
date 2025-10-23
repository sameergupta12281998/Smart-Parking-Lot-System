package com.smartparking.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Spot {
    private final String id;
    private final int floor;
    private final SpotSize size;
    private final AtomicReference<SpotStatus> status = new AtomicReference<>(SpotStatus.AVAILABLE);

    public Spot(String id, int floor, SpotSize size) {
        this.id = Objects.requireNonNull(id);
        this.floor = floor;
        this.size = Objects.requireNonNull(size);
    }

    public String getId() { return id; }
    public int getFloor() { return floor; }
    public SpotSize getSize() { return size; }

    public SpotStatus getStatus() { return status.get(); }

    // Try to atomically occupy the spot. Returns true on success.
    public boolean tryOccupy() {
        return status.compareAndSet(SpotStatus.AVAILABLE, SpotStatus.OCCUPIED);
    }

    // Release back to available (idempotent)
    public void release() {
        status.set(SpotStatus.AVAILABLE);
    }

    public void setOutOfService() { status.set(SpotStatus.OUT_OF_SERVICE); }

    @Override
    public String toString() {
        return "Spot{" + id + ", floor=" + floor + ", size=" + size + ", status=" + status.get() + '}';
    }
}
