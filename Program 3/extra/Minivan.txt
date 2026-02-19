public class Minivan extends Vehicle 
{

    private int seating;
    private int cargoCapacity; // cubic feet

    public Minivan(String description, int mpg, String vin,
                   int seating, int cargoCapacity) 
    {
        super(description, mpg, vin);
        this.seating = seating;
        this.cargoCapacity = cargoCapacity;
    }

    public int getSeating() 
    {
        return seating;
    }

    public int getCargoCapacity() 
    {
        return cargoCapacity;
    }

    @Override
    public String toString() 
    {
        return getDescription() + " (Minivan) MPG: " + getMpg() +
               " Seating: " + seating +
               " Cargo Storage: " + cargoCapacity + " cu. ft. " +
               "VIN: " + getVIN();
    }
}