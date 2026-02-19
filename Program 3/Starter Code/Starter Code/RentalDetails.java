public class RentalDetails 
{

    // vehicle type will be "Car", "SUV", or "Minivan"
    private String vehicle_type;
    private TimePeriod rental_period;
    private int num_miles_driven;
    private boolean insurance_selected;

    public RentalDetails(String vehicle_type, TimePeriod rental_period,
                         int num_miles_driven, boolean insurance_selected) 
    {
        this.vehicle_type = vehicle_type;
        this.rental_period = rental_period;
        this.num_miles_driven = num_miles_driven;
        this.insurance_selected = insurance_selected;
    }

    public String getVehicleType() 
    {
        return vehicle_type;
    }

    public TimePeriod getRentalPeriod() 
    {
        return rental_period;
    }

    public int getNumMilesDriven() 
    {
        return num_miles_driven;
    }

    public boolean isInsuranceSelected() 
    {
        return insurance_selected;
    }

    @Override
    public String toString() 
    {
        return "Estimate: " + vehicle_type + ", " + rental_period +
               ", " + num_miles_driven + " miles, insurance: " +
               (insurance_selected ? "yes" : "no");
    }
}