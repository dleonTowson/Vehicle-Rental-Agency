public class Car extends Vehicle 
{

    private int seating;

    public Car(String description, int mpg, String vin, int seating) 
    {
        super(description, mpg, vin);
        this.seating = seating;
    }

    public int getSeating() 
    {
        return seating;
    }

    @Override
    public String toString() 
    {
        return getDescription() + " (Car) MPG: " + getMpg() +
               " Seating: " + seating +
               " VIN: " + getVIN();
    }
}
