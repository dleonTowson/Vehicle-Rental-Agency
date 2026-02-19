import java.util.Scanner;

public class EmployeeGUI_Interface implements UserInterface 
{
	
	// No constructor needed, calls static methods of the SystemInterface.
	// Method start begins a command loop that repeatedly: (a) displays a menu of options, 
      // (b) gets the selected option from the user, and (c) executes the corresponding command.

	private boolean quit = false;
 	public void start(Scanner input) 
	{

		int selection;

		// command loop
		while(!quit) 
		{
		displayMenu();
		selection = getSelection(input);
		execute(selection, input);
		}
    }
	
     // ------- private methods
	private int getPositiveInt(Scanner input)
	{
    	while (true)
    	{
        	String line = input.nextLine().trim();

        	try
        	{
            	int value = Integer.parseInt(line);

            	if (value >= 0)
            	{
                	return value;
        		}
        	}
        	catch (NumberFormatException e)
        	{
            	// fall through
        	}

        System.out.print("Please enter a non-negative integer: ");
    	}
	}

	 private void execute(int selection, Scanner input) 
	{
        String[] results;

        switch (selection) 
		{
            case 1:
                // show all rates
                String[] carRates  = SystemInterface.getCarRates();
                String[] suvRates  = SystemInterface.getSUVRates();
                String[] vanRates  = SystemInterface.getMinivanRates();
                System.out.println();
                System.out.println("Current Agency Rates:");
                displayResults(carRates);
                displayResults(suvRates);
                displayResults(vanRates);
                break;

            case 2:
                results = SystemInterface.getAvailCars();
                System.out.println();
                System.out.println("Available Cars:");
                displayResults(results);
                break;

            case 3:
                results = SystemInterface.getAvailSUVs();
                System.out.println();
                System.out.println("Available SUVs:");
                displayResults(results);
                break;

            case 4:
                results = SystemInterface.getAvailMinivans();
                System.out.println();
                System.out.println("Available Minivans:");
                displayResults(results);
                break;

            case 5:
                results = SystemInterface.getAllVehicles();
                System.out.println();
                System.out.println("All Vehicles:");
                displayResults(results);
                break;

            case 6:
                // estimate rental cost
                RentalDetails rentalDetails = getRentalDetails(input);
                results = SystemInterface.calcEstimatedRentalCost(rentalDetails);
                System.out.println();
                System.out.println("Estimated Rental Cost:");
                displayResults(results);
                break;

            case 7:
                // make reservation
                ReservationDetails resvDetails = getReservationDetails(input);
                results = SystemInterface.makeReservation(resvDetails);
                System.out.println();
                displayResults(results);
                break;

            case 8:
                // cancel reservation
                System.out.print("Enter VIN to cancel reservation: ");
                String vinToCancel = input.nextLine().trim();
                results = SystemInterface.cancelReservation(vinToCancel);
                System.out.println();
                displayResults(results);
                break;

            case 9:
                // view specific reservation
                System.out.print("Enter VIN to view reservation: ");
                String vinToView = input.nextLine().trim();
                results = SystemInterface.getReservation(vinToView);
                System.out.println();
                displayResults(results);
                break;

            case 10:
                // view all reservations
                results = SystemInterface.getAllReservations();
                System.out.println();
                System.out.println("All Reservations:");
                displayResults(results);
                break;

            case 11:
                // process returned vehicle (return, compute final charge)
                System.out.print("Enter VIN of returned vehicle: ");
                String vinReturn = input.nextLine().trim();

                System.out.print("Enter number of days used: ");
                int daysUsed = getPositiveInt(input);

                System.out.print("Enter number of miles driven: ");
                int milesDriven = getPositiveInt(input);

                results = SystemInterface.processReturnedVehicle(vinReturn, daysUsed, milesDriven);
                System.out.println();
                System.out.println("Return Processed:");
                displayResults(results);
                break;

            case 12:
                quit = true;
                System.out.println("Exiting Employee menu...");
                break;

            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

	private void displayMenu() 
	{
        System.out.println();
        System.out.println("===== Employee Menu =====");
        System.out.println(" 1. Display current rates (Car / SUV / Minivan)");
        System.out.println(" 2. Display available Cars");
        System.out.println(" 3. Display available SUVs");
        System.out.println(" 4. Display available Minivans");
        System.out.println(" 5. Display all vehicles");
        System.out.println(" 6. Estimate rental cost");
        System.out.println(" 7. Make a reservation");
        System.out.println(" 8. Cancel a reservation");
        System.out.println(" 9. View a specific reservation");
        System.out.println("10. View all reservations");
        System.out.println("11. Process returned vehicle");
        System.out.println("12. Return to main user selection (Quit Employee menu)");
        System.out.print  ("Enter selection (1-12): ");
    }
 	// displays the user options

	private int getSelection(Scanner input) 
	{
        while (true) 
		{
            String line = input.nextLine().trim();
            try 
			{
                int sel = Integer.parseInt(line);
                if (sel >= 1 && sel <= 12) 
				{
                    return sel;
                }
                System.out.print("Invalid selection - please reenter (1-12): ");
            } 
			catch (NumberFormatException e) 
			{
                System.out.print("Invalid selection - please reenter (1-12): ");
            }
        }
	}
 	// prompts user for selection from menu (continues to prompt if selection < 1 or selection > 8)

	private String getVIN(Scanner input)
	{
		System.out.print("Enter vehicle VIN: ");
    	String vin = input.nextLine().trim();
    	return vin;
	}
	// prompts user to enter VIN for a given vehicle (does not do any error checking on the input) {    }

	private int getVehicleType(Scanner input)
	{
		int type;
	
		while (true)
		{
			System.out.print("Enter vehicle type (1 = Car, 2 = SUV, 3 = Minivan): ");
			String line = input.nextLine().trim();
	
			try
			{
				type = Integer.parseInt(line);
	
				if (type >= 1 && type <= 3)
				{
					return type;
				}
			}
			catch (NumberFormatException e)
			{
				// fall through to error message
			}
	
			System.out.println("Invalid input. Please enter 1, 2, or 3.");
		}
	}
	// prompts user to enter 1, 2, or 3, and returns (continues to prompt user if invalid input given) {    }

	private RentalDetails getRentalDetails(Scanner input)
	{
		int vehicleTypeCode = getVehicleType(input);
	
		String vehicleType;
		if (vehicleTypeCode == 1)
		{
			vehicleType = "Car";
		}
		else if (vehicleTypeCode == 2)
		{
			vehicleType = "SUV";
		}
		else
		{
			vehicleType = "Minivan";
		}
	
		System.out.print("Enter estimated number of miles: ");
		int miles = Integer.parseInt(input.nextLine().trim());
	
		System.out.print("Enter rental unit (d = days, w = weeks, m = months): ");
		char unit = input.nextLine().trim().toLowerCase().charAt(0);
	
		System.out.print("Enter number of rental units: ");
		int quantity = Integer.parseInt(input.nextLine().trim());
	
		System.out.print("Add daily insurance (y/n)? ");
		String ins = input.nextLine().trim().toLowerCase();
		boolean insurance = ins.startsWith("y");
	
		TimePeriod period = new TimePeriod(unit, quantity);
	
		return new RentalDetails(vehicleType, period, miles, insurance);
	}
	// prompts user to enter required information for an estimated rental cost (vehicle type, estimated  
 	// number of miles expected to be driven, expected rental period and optional insuranc, returning the
 	// result packaged as a RentalDetails object (to pass in method calls to the SystemInterface) {   }

	private ReservationDetails getReservationDetails(Scanner input)
	{
		String vin = getVIN(input);
	
		System.out.print("Enter customer name: ");
		String custName = input.nextLine().trim();
	
		System.out.print("Enter credit card number: ");
		String cardNum = input.nextLine().trim();
	
		System.out.print("Enter rental unit (d = days, w = weeks, m = months): ");
		char unit = input.nextLine().trim().toLowerCase().charAt(0);
	
		System.out.print("Enter number of rental units: ");
		int quantity = Integer.parseInt(input.nextLine().trim());
	
		System.out.print("Add daily insurance (y/n)? ");
		String ins = input.nextLine().trim().toLowerCase();
		boolean insurance = ins.startsWith("y");
	
		TimePeriod period = new TimePeriod(unit, quantity);
	
		return new ReservationDetails(custName, cardNum, period, insurance, vin);
	}
	// prompts user to enter required information for making a reservation (VIN of vehicle to reserve, 
 	// credit card num, rental period, and optional insurance), returning the result packaged as a 
 	// ReservationDetails object (to pass in method calls to the SystemInterface)  {    }

	private void displayResults(String[] lines)
	{
		if (lines == null || lines.length == 0)
		{
			System.out.println("(No results to display)");
			return;
		}
	
		for (int i = 0; i < lines.length; i = i + 1)
		{
			System.out.println(lines[i]);
		}
	}
	// displays the array of strings passed, one string per screen line {    }
}

