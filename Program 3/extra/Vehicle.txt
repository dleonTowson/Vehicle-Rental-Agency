public abstract class Vehicle 
{

    private String description;       // make-model or description
    private int mpg;                  // miles per gallon
    private String vin;               // unique vehicle identification number
    private ReservationDetails resv;  // null if not reserved
    private VehicleRates rates;       // quoted rates at time of reservation

    public Vehicle(String description, int mpg, String vin) 
    {
        this.description = description;
        this.mpg = mpg;
        this.vin = vin;
        this.resv = null;
        this.rates = null;
    }

    public String getDescription() 
    {
        return description;
    }

    public int getMpg() 
    {
        return mpg;
    }

    public String getVIN() 
    {
        return vin;
    }

    public ReservationDetails getReservation() 
    {
        return resv;
    }

    public VehicleRates getQuotedRates() 
    {
        return rates;
    }

    public boolean isReserved() 
    {
        return resv != null;
    }

    public void setReservation(ReservationDetails resv)
            throws ReservedVehicleException 
    {
        if (this.resv != null) {
            throw new ReservedVehicleException(
                "Vehicle " + vin + " is already reserved.");
        }
        this.resv = resv;
    }

    public void setQuotedRates(VehicleRates cost) {
        // store a copy of the rates so later rate changes don’t affect this rental
        this.rates = new VehicleRates(cost);
    }

    public void cancelReservation() throws UnreservedVehicleException {
        if (this.resv == null) {
            throw new UnreservedVehicleException(
                "Vehicle " + vin + " has no reservation to cancel.");
        }
        this.resv = null;
        this.rates = null;
    }

    @Override
    public abstract String toString();
}
