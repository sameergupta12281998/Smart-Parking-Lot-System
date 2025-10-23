package com.smartparking;

import com.smartparking.model.*;
import com.smartparking.repo.*;
import com.smartparking.service.*;
import com.smartparking.panel.*;

import java.util.*;

public class Main {
    private static final int MAX_FLOORS = 4;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Initialize repositories ---
        InMemorySpotRepository spotRepo = new InMemorySpotRepository();
        InMemoryTransactionRepository txRepo = new InMemoryTransactionRepository();
        InMemoryVehicleRepository vehicleRepo = new InMemoryVehicleRepository();

        // --- Initialize services and panels ---
        AllocationService alloc = new SimpleAllocationService(spotRepo);
        FeeCalculator feeCalc = new SimpleFeeCalculator();
        ParkingService parkingService = new ParkingService(alloc, feeCalc, txRepo, vehicleRepo);
        EntryPanel entryPanel = new EntryPanel(parkingService);
        ExitPanel exitPanel = new ExitPanel(parkingService);
        DisplayPanel displayPanel = new DisplayPanel(spotRepo);

        // --- Default: create 1 floor at startup ---
        int totalFloors = 1;
        seedFloorSpots(spotRepo, 1);
        System.out.println("Initialized Parking Lot with 1 Floor.");

        // --- Main simulation loop ---
        boolean running = true;
        while (running) {
            System.out.println("\n=== SMART PARKING SYSTEM MENU ===");
            System.out.println("1. Show available spots");
            System.out.println("2. Vehicle entry");
            System.out.println("3. Vehicle exit");
            System.out.println("4. Show active transactions");
            System.out.println("5. Add new floor");
            System.out.println("0. Exit system");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayPanel.showAvailableSpots();
                    break;

                case "2":
                    System.out.print("Enter Vehicle Plate: ");
                    String plate = scanner.nextLine().trim();
                    System.out.print("Enter Vehicle Type (CAR, MOTORCYCLE, BUS): ");
                    String typeStr = scanner.nextLine().trim().toUpperCase();

                    try {
                        VehicleType type = VehicleType.valueOf(typeStr);
                        Vehicle vehicle = new Vehicle(plate, type);
                        entryPanel.processVehicleEntry(vehicle);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid vehicle type. Try again.");
                    }
                    break;

                 case "3":
                    System.out.println("Exit by: 1) Transaction ID  2) Vehicle Plate");
                    System.out.print("Choose (1 or 2): ");
                    String exitChoice = scanner.nextLine().trim();
                    if ("1".equals(exitChoice)) {
                        System.out.print("Enter Transaction ID to exit: ");
                        String txId = scanner.nextLine().trim();
                        Optional<ParkingTransaction> checkedOutOpt = exitPanel.processVehicleExit(txId);
                        if (checkedOutOpt.isPresent()) {
                            ParkingTransaction tx = checkedOutOpt.get();
                            spotRepo.findById(tx.getSpotId()).ifPresent(Spot::release);
                            System.out.println("Spot released for ID: " + tx.getSpotId());
                        }
                    } else if ("2".equals(exitChoice)) {
                        System.out.print("Enter Vehicle Plate to exit: ");
                        String plateValue = scanner.nextLine().trim();
                        Optional<ParkingTransaction> checkedOutOpt = exitPanel.processVehicleExitByPlate(plateValue);
                        if (checkedOutOpt.isPresent()) {
                            ParkingTransaction tx = checkedOutOpt.get();
                            spotRepo.findById(tx.getSpotId()).ifPresent(Spot::release);
                            System.out.println("Spot released for ID: " + tx.getSpotId());
                        }
                    } else {
                        System.out.println("Invalid choice. Returning to main menu.");
                    }
                    break;
                

                case "4":
                    System.out.println("=== ACTIVE TRANSACTIONS ===");
                    txRepo.findAll().stream()
                            .filter(tx -> tx.getExitTs() == null)
                            .forEach(tx -> System.out.println(
                                    tx.getId() + " | Plate: " + tx.getVehiclePlate() +
                                            " | Spot: " + tx.getSpotId() +
                                            " | Entry: " + tx.getEntryTs()));
                    break;

                case "5":
                    if (totalFloors >= MAX_FLOORS) {
                        System.out.println("⚠️ Cannot add more than " + MAX_FLOORS + " floors.");
                    } else {
                        totalFloors++;
                        seedFloorSpots(spotRepo, totalFloors);
                        System.out.println("✅ Added Floor " + totalFloors + " with default spots.");
                    }
                    break;

                case "0":
                    running = false;
                    System.out.println("Exiting Smart Parking System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void seedFloorSpots(InMemorySpotRepository spotRepo, int floorNumber) {
        List<Spot> spots = new ArrayList<>();
    
        // 20 motorcycle spots (change number as needed)
        for (int i = 1; i <= 20; i++) {
            spots.add(new Spot("F" + floorNumber + "-M" + String.format("%02d", i),
                    floorNumber, SpotSize.MOTORCYCLE));
        }
    
        // 80 compact spots
        for (int i = 1; i <= 80; i++) {
            spots.add(new Spot("F" + floorNumber + "-C" + String.format("%02d", i),
                    floorNumber, SpotSize.COMPACT));
        }
    
        // 40 regular spots
        for (int i = 1; i <= 40; i++) {
            spots.add(new Spot("F" + floorNumber + "-R" + String.format("%02d", i),
                    floorNumber, SpotSize.REGULAR));
        }
    
        // 30 large spots
        for (int i = 1; i <= 30; i++) {
            spots.add(new Spot("F" + floorNumber + "-L" + String.format("%02d", i),
                    floorNumber, SpotSize.LARGE));
        }
    
        spotRepo.saveAll(spots);
    }
    
}
