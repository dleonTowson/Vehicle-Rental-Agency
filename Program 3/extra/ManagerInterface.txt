import java.util.Scanner;

public class ManagerInterface implements UserInterface 
{

    private boolean quit = false;

    public void start(Scanner input) 
    {
        int selection;

        while (!quit) 
        {
            displayMenu();
            selection = getSelection(input);
            execute(selection, input);
        }
    }

    private void displayMenu() 
    {
        System.out.println();
        System.out.println("===== Manager Menu =====");
        System.out.println(" 1. View car rates");
        System.out.println(" 2. View SUV rates");
        System.out.println(" 3. View minivan rates");
        System.out.println(" 4. Update car rates");
        System.out.println(" 5. Update SUV rates");
        System.out.println(" 6. Update minivan rates");
        System.out.println(" 7. View all vehicles");
        System.out.println(" 8. View all reservations");
        System.out.println(" 9. View all transactions");
        System.out.println("10. Return to main user selection (Quit Manager menu)");
        System.out.print  ("Enter selection (1-10): ");
    }

    private int getSelection(Scanner input) 
    {
        while (true) 
        {
            String line = input.nextLine().trim();
            try 
            {
                int sel = Integer.parseInt(line);
                if (sel >= 1 && sel <= 10) 
                {
                    return sel;
                }
                System.out.print("Invalid selection – please reenter (1-10): ");
            } 
            catch (NumberFormatException e) 
            {
                System.out.print("Invalid selection – please reenter (1-10): ");
            }
        }
    }

    private void execute(int selection, Scanner input) 
    {
        String[] results;

        switch (selection) 
        {
            case 1:
                results = SystemInterface.getCarRates();
                System.out.println();
                displayResults(results);
                break;

            case 2:
                results = SystemInterface.getSUVRates();
                System.out.println();
                displayResults(results);
                break;

            case 3:
                results = SystemInterface.getMinivanRates();
                System.out.println();
                displayResults(results);
                break;

            case 4:
                VehicleRates newCarRates = getNewRates(input, "car");
                results = SystemInterface.updateCarRates(newCarRates);
                System.out.println();
                displayResults(results);
                break;

            case 5:
                VehicleRates newSUVRates = getNewRates(input, "SUV");
                results = SystemInterface.updateSUVRates(newSUVRates);
                System.out.println();
                displayResults(results);
                break;

            case 6:
                VehicleRates newVanRates = getNewRates(input, "minivan");
                results = SystemInterface.updateMinivanRates(newVanRates);
                System.out.println();
                displayResults(results);
                break;

            case 7:
                results = SystemInterface.getAllVehicles();
                System.out.println();
                System.out.println("All Vehicles:");
                displayResults(results);
                break;

            case 8:
                results = SystemInterface.getAllReservations();
                System.out.println();
                System.out.println("All Reservations:");
                displayResults(results);
                break;

            case 9:
                results = SystemInterface.getAllTransactions();
                System.out.println();
                System.out.println("All Transactions:");
                displayResults(results);
                break;

            case 10:
                quit = true;
                System.out.println("Exiting Manager menu...");
                break;

            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    // ask user for new rate values
    private VehicleRates getNewRates(Scanner input, String label) 
    {
        System.out.println();
        System.out.println("Enter new " + label + " rates:");

        double daily   = getDouble(input, "Daily rate: ");
        double weekly  = getDouble(input, "Weekly rate: ");
        double monthly = getDouble(input, "Monthly rate: ");
        double perMile = getDouble(input, "Per-mile charge: ");
        double dailyIns = getDouble(input, "Daily insurance rate: ");

        return new VehicleRates(daily, weekly, monthly, perMile, dailyIns);
    }

    private double getDouble(Scanner input, String prompt) {
        while (true) 
        {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try 
            {
                return Double.parseDouble(line);
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void displayResults(String[] lines) 
    {
        if (lines == null || lines.length == 0) 
        {
            System.out.println("(No results to display.)");
            return;
        }
        for (int i = 0; i < lines.length; i = i + 1) 
        {
            System.out.println(lines[i]);
        }
    }
}