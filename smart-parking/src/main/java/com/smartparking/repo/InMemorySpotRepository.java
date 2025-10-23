package com.smartparking.repo;

import com.smartparking.model.Spot;
import com.smartparking.model.SpotSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InMemorySpotRepository implements SpotRepository {
    private final ConcurrentMap<String, Spot> map = new ConcurrentHashMap<>();

    @Override
    public java.util.Optional<Spot> findById(String id) { return java.util.Optional.ofNullable(map.get(id)); }

    @Override
    public List<Spot> findAvailableBySize(SpotSize size) {
        Objects.requireNonNull(size);
        List<Spot> result = new ArrayList<>();
        for (Spot s : map.values()) {
            if (s.getSize() == size && s.getStatus().name().equals("AVAILABLE")) {
                result.add(s);
            }
        }
        return result.stream().collect(Collectors.toList());
    }

    @Override
    public void save(Spot spot) { map.put(spot.getId(), spot); }

    // convenience for startup
    public void saveAll(List<Spot> spots) { for (Spot s : spots) save(s); }
}
