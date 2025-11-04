
# Vehicle Rental Agency System

Command‑line application that manages a small vehicle‑rental agency with employee and manager interfaces. It supports quoting prices, creating and canceling reservations, checking vehicles in/out, and maintaining transaction history using current and quoted rates.

## 🌟 Overview

- **Main entry point:** `AgencyRentalProgram` (loops, allows switching between Employee and Manager UIs).
- **System bootstrap:** Initializes **CurrentRates**, **Vehicles** (via a `populate(...)` helper), and **Transactions**, then hands control to a selected **UserInterface**.
- **Two roles:** `EmployeeInterface` and `ManagerInterface` (selected at runtime).
- **Facade:** `SystemInterface` centralizes operations over agency data once `initSystem(...)` is called.

> The day‑to‑day flow is: select UI → interact (quote, reserve, rent/return, manage rates/fleet) → optionally switch UI or quit.

## ✅ Features

- Price **quotes** using **current** rates (daily/weekly/monthly + per‑mile + optional daily insurance).
- **Reservations** (stores quoted rates at time of booking).
- **Pick‑up / Return** processing with **actual** charges computed from **quoted** rates.
- **Vehicle inventory** lookup by VIN and iteration over fleet.
- **Transactions** audit trail (linked list) with iterators.
- **Manager tools** to view/adjust standard rates and manage fleet entries.

## 🧱 Key Components (Design)

### User Interfaces
- `UserInterface` (contract for a `start(...)` method).
- `EmployeeInterface`, `ManagerInterface` implement user interactions.

### Core Domain
- **Abstract `Vehicle`** (subclasses: `Car`, `SUV`, `Minivan`)
  - `description`, `mpg`, `vin`
  - `ReservationDetails resv` (0..1, composition)
  - `VehicleRates rates` (quoted at reservation time)
  - `isReserved()`, `setReservation(...)`, `cancelReservation()`, `toString()` (abstract)
- **Value/Info classes**
  - `VehicleRates` (daily, weekly, monthly, per‑mile, daily insurance) + copy ctor
  - `TimePeriod` (`unit` ∈ {`'d'`, `'w'`, `'m'`}, `quantity`)
  - `ReservationDetails` (customer, card, `TimePeriod`, insurance flag, VIN)
  - `RentalDetails` (vehicle type, `TimePeriod`, miles driven, insurance flag)
  - `Transaction` (immutable record for the log; `toString()` summaries)
- **Aggregations**
  - `CurrentRates` (holds three `VehicleRates`: Car/SUV/Minivan; computes **estimated** and **actual** costs)
  - `Vehicles` (collection/iterator over `Vehicle`; `getVehicle(vin)`)
  - `Transactions` (singly linked list of `TransactionNode`; add + iterator)

### Exceptions (suggested)
- `ReservedVehicleException`, `UnreservedVehicleException`, `VINNotFoundException`

### System Facade
- `SystemInterface`
  - `initialized()`; `initSystem(CurrentRates, Vehicles, Transactions)`
  - Methods invoked by UIs for quoting, reserving, renting/returning, listing, and reporting
## 🗺️ Execution Flow

1. **Select role**: Employee or Manager.
2. **System init** (once): `CurrentRates`, `Vehicles`, `Transactions` registered in `SystemInterface`.
3. **Interact** via chosen UI:
   - Quote → Reserve → Pick‑up → Return
   - View lists (fleet, reservations, transactions)
   - Manager may **update standard rates** and **manage fleet**
4. **Loop** back to role selection or **quit**.

## 🧪 Typical Operations

- **Quote**: `CurrentRates.calcEstimatedCost(vehicleType, TimePeriod, estMiles, insurance)`
- **Reserve**: attach `ReservationDetails` + snapshot **quoted `VehicleRates`** to the `Vehicle`
- **Return**: `CurrentRates.calcActualCost(quotedRates, daysUsed, miles, insurance)`
- **Log**: append `Transaction` to `Transactions` (linked list)

## 📦 Project Layout (Idea)

```
/src
 ├─ system/           # SystemInterface (facade)
 ├─ ui/               # UserInterface, EmployeeInterface, ManagerInterface
 ├─ domain/           # Vehicle (abstract), Car, SUV, Minivan
 ├─ rates/            # VehicleRates, CurrentRates
 ├─ value/            # TimePeriod, ReservationDetails, RentalDetails, Transaction
 ├─ data/             # Vehicles, Transactions (+ TransactionNode)
 ├─ exceptions/       # ReservedVehicleException, UnreservedVehicleException, VINNotFoundException
 └─ main/             # AgencyRentalProgram, populate(...)
```

## 🚀 How to Run

```bash
# Compile (example; adjust paths/packages as needed)
javac -d out $(find src -name "*.java")

# Run
java -cp out main.AgencyRentalProgram
```
---

This README documents the structure and behavior expected by the provided specification so it can accompany your finished code on GitHub.
