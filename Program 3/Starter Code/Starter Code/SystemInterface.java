public class SystemInterface {

	private static CurrentRates agency_rates;
	private static Vehicles agency_vehicles;
	private static Transactions transactions_history;

	// used to init static variables (in place of a constructor)
	public static void initSystem(CurrentRates r, Vehicles v, Transactions t) 
	{
		agency_rates = r;
		agency_vehicles = v;
		transactions_history = t;
	}
	
	// used to check if SystemInterface initialized
	public boolean initialized() 
	{
		return agency_rates != null;
	}
	
	private static String[] makeSingleLine(String line) {
        return new String[] { line };
    }

    private static String vehicleTypeString(Vehicle v) {
        if (v instanceof Car) {
            return "Car";
        } else if (v instanceof SUV) {
            return "SUV";
        } else if (v instanceof Minivan) {
            return "Minivan";
        } else {
            return "Vehicle";
        }
    }

    private static VehicleRates getCurrentRatesForVehicle(Vehicle v) {
        if (v instanceof Car) {
            return agency_rates.getCarRates();
        } else if (v instanceof SUV) {
            return agency_rates.getSUVRates();
        } else {
            return agency_rates.getMinivanRates();
        }
    }

    private static int vehicleTypeCodeFromString(String type) {
        if (type == null) {
            return 1; // default
        }
        String t = type.trim().toLowerCase();
        if (t.equals("car")) {
            return 1;
        } else if (t.equals("suv")) {
            return 2;
        } else if (t.equals("minivan")) {
            return 3;
        }
        return 1; // default to car
    }

    private static String formatRatesLine(String label, VehicleRates vr) {
        return String.format(
            "%s Rates  Daily: $%.2f   Weekly: $%.2f   Monthly: $%.2f   Per Mile: $%.2f   Daily Insur: $%.2f",
            label,
            vr.getDailyRate(),
            vr.getWeeklyRate(),
            vr.getMonthlyRate(),
            vr.getMileageChrg(),
            vr.getDailyInsurRate()
        );
    }

	// Note that methods updateXXXRates, makeReservation and cancelReservation return an
	// acknowledgement of successful completion of the requested action (e.g. “Vehicle ABC123
	// successfully reserved”). Method processReturnedVehicle returns the final cost for the returned 
	// vehicle (e.g., “Total charge for VIN ABC123 for 3 days, 233 miles @  0.15/mile and daily
	// insurance @ 14.95/day = $xxx.xx.)

	// Current Rates Related Methods
	public static String[] getCarRates() 
	{
		VehicleRates vr = agency_rates.getCarRates();
        return makeSingleLine(formatRatesLine("Car", vr));	
	}
	public static String[] getSUVRates() 
	{
		VehicleRates vr = agency_rates.getSUVRates();
        return makeSingleLine(formatRatesLine("SUV", vr));
	}
	public static String[] getMinivanRates() 
	{
		VehicleRates vr = agency_rates.getMinivanRates();
        return makeSingleLine(formatRatesLine("Minivan", vr));
	}

	public static String[] updateCarRates(VehicleRates rates) 
	{
		agency_rates.setCarRates(rates);
        return makeSingleLine("Car rates successfully updated.");
	}
	public static String[] updateSUVRates(VehicleRates rates) 
	{ 
		agency_rates.setSUVRates(rates);
        return makeSingleLine("SUV rates successfully updated.");
	}
	public static String[] updateMinivanRates(VehicleRates rates) 
	{
		agency_rates.setMinivanRates(rates);
        return makeSingleLine("Minivan rates successfully updated.");
	}

    public static String[] calcEstimatedRentalCost(RentalDetails rental_details) 
	{ 
		int vehicleTypeCode = vehicleTypeCodeFromString(rental_details.getVehicleType());
        double cost = agency_rates.calcEstimatedCost(
            vehicleTypeCode,
            rental_details.getRentalPeriod(),
            rental_details.getNumMilesDriven(),
            rental_details.isInsuranceSelected()
        );

        String insuranceText = rental_details.isInsuranceSelected()
            ? "with daily insurance"
            : "without daily insurance";

        String line = String.format(
            "Estimated cost for %s, %s, %d miles, %s = $%.2f",
            rental_details.getVehicleType(),
            rental_details.getRentalPeriod().toString(),
            rental_details.getNumMilesDriven(),
            insuranceText,
            cost
        );

        return makeSingleLine(line);
	}
	public static String[ ] processReturnedVehicle(String vin, int num_days_used, int num_miles_driven) 
	{ 
		try 
		{
            Vehicle v = agency_vehicles.getVehicle(vin);

            if (!v.isReserved()) 
			{
                return makeSingleLine("ERROR: Vehicle with VIN " + vin + " is not currently reserved.");
            }

            ReservationDetails resv = v.getReservation();
            VehicleRates quotedRates = v.getQuotedRates();

            boolean dailyInsur = resv.isInsuranceSelected();

            double totalCost = agency_rates.calcActualCost(
                quotedRates,
                num_days_used,
                num_miles_driven,
                dailyInsur
            );

            // Build descriptive line like assignment example
            String insurPart = dailyInsur
                ? String.format(" and daily insurance @ $%.2f/day", quotedRates.getDailyInsurRate())
                : "";
            String line = String.format(
                "Total charge for VIN %s for %d days, %d miles @ $%.2f/mile%s = $%.2f",
                vin,
                num_days_used,
                num_miles_driven,
                quotedRates.getMileageChrg(),
                insurPart,
                totalCost
            );

            // Add a transaction record
            String creditCard = resv.getCreditCardNum();
            String customerName = resv.getCustomerName();
            String vehicleType = vehicleTypeString(v);
            String rentalPeriodText = num_days_used + " days";
            String milesDrivenText = num_miles_driven + " mi";
            String rentalCostText = String.format("$%.2f", totalCost);

            Transaction tran = new Transaction(
                creditCard,
                customerName,
                vehicleType,
                rentalPeriodText,
                milesDrivenText,
                rentalCostText
            );
            transactions_history.add(tran);

            // Clear reservation
            try 
			{
                v.cancelReservation();
            } 
			catch (UnreservedVehicleException e) 
			{
                // should not happen, ignore
            }

            return makeSingleLine(line);

        } 
		catch (VINNotFoundException e) 
		{
            return makeSingleLine("ERROR: " + e.getMessage());
        }
    }

	// Note that the rates to be used are retrieved from the VehicleRates object stored in the specific rented
	// vehicle object, and the daily insurance option is retrieved from the Reservation object of the rented
	// vehicle

	// Vehicle Related Methods
	
    public static String[] getAvailCars() 
    {
        // count
        int num = 0;
        Vehicle v;
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof Car && !v.isReserved()) 
            {
                num = num + 1;
            }
        }

        String[] lines = new String[num];
        int idx = 0;

        // collect
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof Car && !v.isReserved())
            {
                lines[idx] = v.toString();
                idx = idx + 1;
            }
        }

        return lines;
    }

    public static String[] getAvailSUVs() 
    {
        int num = 0;
        Vehicle v;
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof SUV && !v.isReserved()) 
            {
                num = num + 1;
            }
        }

        String[] lines = new String[num];
        int idx = 0;

        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof SUV && !v.isReserved()) 
            {
                lines[idx] = v.toString();
                idx = idx + 1;
            }
        }

        return lines;
    }

	public static String[] getAvailMinivans() 
    {
        int num = 0;
        Vehicle v;
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof Minivan && !v.isReserved()) 
            {
                num = num + 1;
            }
        }

        String[] lines = new String[num];
        int idx = 0;

        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v instanceof Minivan && !v.isReserved()) 
            {
                lines[idx] = v.toString();
                idx = idx + 1;
            }
        }

        return lines;
    }

	public static String[] getAllVehicles() 
    {
        // count
        int num = 0;
        Vehicle v;
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            num = num + 1;
        }

        String[] lines = new String[num];
        int idx = 0;

        // collect
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            lines[idx] = v.toString();
            idx = idx + 1;
        }

        return lines;
    }

	public static String[] makeReservation(ReservationDetails resv)
    {
        String vin = resv.getVIN();

        try 
        {
            Vehicle v = agency_vehicles.getVehicle(vin);

            if (v.isReserved()) 
            {
                return makeSingleLine("ERROR: Vehicle with VIN " + vin + " is already reserved.");
            }

            // attach reservation
            v.setReservation(resv);

            // attach QUOTED rates copy for this vehicle
            VehicleRates currentRates = getCurrentRatesForVehicle(v);
            v.setQuotedRates(currentRates);

            return makeSingleLine("Vehicle " + vin + " successfully reserved for "
                    + resv.getCustomerName() + ".");

        } 
        catch (VINNotFoundException e) 
        {
            return makeSingleLine("ERROR: " + e.getMessage());
        } 
        catch (ReservedVehicleException e) 
        {
            return makeSingleLine("ERROR: " + e.getMessage());
        }
    }

	public static String[] cancelReservation(String vin) 
    {
        try 
        {
            Vehicle v = agency_vehicles.getVehicle(vin);

            v.cancelReservation();

            return makeSingleLine("Reservation for vehicle " + vin + " successfully cancelled.");

        } 
        catch (VINNotFoundException e) 
        {
            return makeSingleLine("ERROR: " + e.getMessage());
        } 
        catch (UnreservedVehicleException e) 
        {
            return makeSingleLine("ERROR: " + e.getMessage());
        }
    }

	public static String[] getReservation(String vin) 
    {
        try 
        {
            Vehicle v = agency_vehicles.getVehicle(vin);

            if (!v.isReserved()) 
            {
                return makeSingleLine("No reservation found for vehicle with VIN " + vin + ".");
            }

            ReservationDetails r = v.getReservation();
            String[] lines = new String[2];
            lines[0] = "Reservation for VIN " + vin + ":";
            lines[1] = r.toString();
            return lines;

        } 
        catch (VINNotFoundException e) 
        {
            return makeSingleLine("ERROR: " + e.getMessage());
        }
    }
	public static String[] getAllReservations() 
    {
        // count
        int num = 0;
        Vehicle v;
        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v.isReserved()) 
            {
                num = num + 1;
            }
        }

        if (num == 0) 
        {
            return makeSingleLine("There are currently no reservations.");
        }

        String[] lines = new String[num];
        int idx = 0;

        agency_vehicles.reset();
        while (agency_vehicles.hasNext()) 
        {
            v = agency_vehicles.getNext();
            if (v.isReserved()) 
            {
                ReservationDetails r = v.getReservation();
                lines[idx] = "VIN " + v.getVIN() + " -> " + r.toString();
                idx = idx + 1;
            }
        }

        return lines;
    }

	// transactions-related methods
	public static String[] addTransaction(String credit_card,
    String customer_name,
    String vehicle_type,
    String rental_period,
    String miles_driven,
    String rental_cost) 
    {

        Transaction tran = new Transaction(
            credit_card,
            customer_name,
            vehicle_type,
            rental_period,
            miles_driven,
            rental_cost
        );
        transactions_history.add(tran);

        return makeSingleLine("Transaction successfully added.");
    }
	public static String[] getAllTransactions()
    {
        // count
        int num = 0;
        transactions_history.reset();
        while (transactions_history.hasNext()) 
        {
            transactions_history.getNext();
            num = num + 1;
        }

        if (num == 0) 
        {
            return makeSingleLine("There are currently no transactions.");
        }

        String[] lines = new String[num];
        int idx = 0;

        transactions_history.reset();
        while (transactions_history.hasNext()) 
        {
            Transaction t = transactions_history.getNext();
            lines[idx] = t.toString();
            idx = idx + 1;
        }

        return lines;
    }
}