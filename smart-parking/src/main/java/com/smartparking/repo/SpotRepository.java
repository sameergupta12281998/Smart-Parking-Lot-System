package com.smartparking.repo;

import com.smartparking.model.Spot;
import com.smartparking.model.SpotSize;

import java.util.List;
import java.util.Optional;

public interface SpotRepository {
    Optional<Spot> findById(String id);
    List<Spot> findAvailableBySize(SpotSize size);
    void save(Spot spot);
}
