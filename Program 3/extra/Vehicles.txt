public class Vehicles 
{

    private Vehicle[] vehicles;
    private int size;
    private int current;  

    public Vehicles() 
    {
        // enough capacity for all vehicles in the spec
        this.vehicles = new Vehicle[50];
        this.size = 0;
        this.current = 0;
    }

    public void add(Vehicle v) 
    {
        if (size == vehicles.length) 
            {
            Vehicle[] temp = new Vehicle[vehicles.length * 2];
            for (int i = 0; i < size; i++) 
            {
                temp[i] = vehicles[i];
            }
            vehicles = temp;
        }
        vehicles[size] = v;
        size++;
    }

    public Vehicle getVehicle(String vin) throws VINNotFoundException 
    {
        for (int i = 0; i < size; i++) 
            {
            if (vehicles[i].getVIN().equals(vin)) 
            {
                return vehicles[i];
            }
        }
        throw new VINNotFoundException("No vehicle with VIN " + vin + " found.");
    }

    // iterator methods

    public void reset() 
    {
        current = 0;
    }

    public boolean hasNext() 
    {
        return current < size;
    }

    public Vehicle getNext() 
    {
        if (!hasNext()) 
        {
            return null;
        }
        return vehicles[current++];
    }
}