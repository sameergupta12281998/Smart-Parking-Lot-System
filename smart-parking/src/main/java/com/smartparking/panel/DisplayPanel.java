package com.smartparking.panel;

import com.smartparking.model.Spot;
import com.smartparking.repo.SpotRepository;
import com.smartparking.model.SpotSize;

import java.util.List;

public class DisplayPanel {
    private final SpotRepository spotRepository;

    public DisplayPanel(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public void showAvailableSpots() {
        System.out.println("======== DISPLAY PANEL ========");
        for (SpotSize size : SpotSize.values()) {
            List<Spot> available = spotRepository.findAvailableBySize(size);
            System.out.println(size + ": " + available.size() + " available");
        }
        System.out.println("===============================");
    }
}
