🚗 Smart Parking Lot System

This is a Smart Parking Lot Management System that helps manage parking spots, vehicles, floors, and fee calculations.
It automatically allocates parking spaces based on vehicle size, tracks entry and exit times, and calculates fees in real time.

Features

1. Parking Spot Allocation

Automatically assigns the nearest available parking spot to incoming vehicles.

Supports multiple vehicle types: Motorcycle, Car, Bus.

Spots are categorized by size: Motorcycle, Compact, Regular, and Large.

Efficient allocation algorithm ensures optimized space usage.

2. Floor Management

Start with 1 default floor on system initialization.

Add up to 3 more floors (maximum 4 floors total).

Each floor automatically contains:

20 Motorcycle spots

80 Compact spots

40 Regular spots

30 Large spots

3. Vehicle Entry & Exit Panels

Entry Panel: Records vehicle entry, assigns a parking spot, and generates a unique transaction ID.

Exit Panel: Allows vehicles to check out either by Transaction ID or License Plate Number.

Calculates the parking fee based on duration and vehicle type.

4. Fee Calculation

₹80 for the first hour.

₹40 per additional hour (rounded up).

Grace period: configurable for short stays.

5. Real-Time Display Panel

Shows available spots per floor and spot type in real time.

Updates automatically when vehicles enter or exit.

Helps users and staff quickly identify available parking.

6. Concurrency Handling

Thread-safe operations using ConcurrentHashMap.

Supports multiple simultaneous check-ins and check-outs safely.

7. Interactive Console Menu

Menu-driven console interface for users:

Show available spots.

Check-in vehicle.

Check-out vehicle.

Add new floor.

View all active transactions.

Exit the system.

How to Run the Application
1. Setup:

Make sure you have Java (JDK 17 or higher) installed.

Clone or download this project to your local machine.

2. Build the Project:

Open a terminal in the project folder.

Run the following command to compile and package the project:

mvn clean package

3. Run the Application:

After building, run the following command:

java -jar target/smart-parking-lot-1.0-SNAPSHOT.jar

4. Use the Menu:

You’ll see an interactive console menu like this:

=== SMART PARKING SYSTEM MENU ===
1. Show available spots
2. Vehicle entry
3. Vehicle exit
4. Show active transactions
5. Add new floor
0. Exit


Follow the on-screen instructions to check in/out vehicles, view spots, or add new floors.

Example Flow
======== DISPLAY PANEL ========
MOTORCYCLE: 20 available
COMPACT: 80 available
REGULAR: 40 available
LARGE: 30 available
===============================

[ENTRY PANEL] Vehicle KA01AB1234 assigned to Spot: F1-C02
[EXIT PANEL] Vehicle KA01AB1234 exited. Fee: ₹120.0

Testing

To run automated tests for fee calculation, allocation, and repository logic:

mvn test


Test coverage includes:

Fee calculation logic

Spot allocation rules

Vehicle entry/exit flow

Repository data consistency
