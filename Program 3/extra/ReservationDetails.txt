public class ReservationDetails 
{

    private String customer_name;
    private String credit_card_num;
    private TimePeriod rental_period;
    private boolean insurance_selected;
    private String VIN;

    public ReservationDetails(String customer_name, String credit_card_num,
                              TimePeriod rental_period, boolean insurance_selected,
                              String VIN) {
        this.customer_name = customer_name;
        this.credit_card_num = credit_card_num;
        this.rental_period = rental_period;
        this.insurance_selected = insurance_selected;
        this.VIN = VIN;
    }

    public String getCustomerName() 
    {
        return customer_name;
    }

    public String getCreditCardNum() 
    {
        return credit_card_num;
    }

    public TimePeriod getRentalPeriod() 
    {
        return rental_period;
    }

    public boolean isInsuranceSelected() 
    {
        return insurance_selected;
    }

    public String getVIN() 
    {
        return VIN;
    }

    @Override
    public String toString() 
    {
        return "Reservation for " + customer_name +
               " (card " + credit_card_num + "), VIN " + VIN +
               ", period: " + rental_period +
               ", insurance: " + (insurance_selected ? "yes" : "no");
    }
}