public class Transaction 
{

    private String creditcard_num;
    private String customer_name;
    private String vehicle_type;   // "Car", "SUV", "Minivan"
    private String rental_period;  // textual (e.g., "5 days")
    private String miles_driven;   // textual (e.g., "150 mi")
    private String rental_cost;    // textual (e.g., "$123.45")

    public Transaction(String creditcard_num, String customer_name,
                       String vehicle_type, String rental_period,
                       String miles_driven, String rental_cost) 
    {
        this.creditcard_num = creditcard_num;
        this.customer_name = customer_name;
        this.vehicle_type = vehicle_type;
        this.rental_period = rental_period;
        this.miles_driven = miles_driven;
        this.rental_cost = rental_cost;
    }

    @Override
    public String toString() 
    {
        // Example format in spec:
        // Customer Name (card #3212546453245879), Car: Toyota Prius, 3 days, 540 mi (insur declined) $120.54
        return customer_name + " (card #" + creditcard_num + "), " +
               vehicle_type + ", " + rental_period + ", " +
               miles_driven + " " + rental_cost;
    }
}