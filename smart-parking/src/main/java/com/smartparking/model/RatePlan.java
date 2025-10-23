package com.smartparking.model;

public class RatePlan {
    public final int baseMinutes;
    public final long baseCents;
    public final long perHourCents;
    public final int graceMinutes;

    public RatePlan(int baseMinutes, long baseCents, long perHourCents, int graceMinutes) {
        this.baseMinutes = baseMinutes;
        this.baseCents = baseCents;
        this.perHourCents = perHourCents;
        this.graceMinutes = graceMinutes;
    }
}
