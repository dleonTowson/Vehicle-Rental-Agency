public class CurrentRates 
{

    // 0 = Car, 1 = SUV, 2 = Minivan
    private VehicleRates[] rates = new VehicleRates[3];

    public CurrentRates(VehicleRates carRates,
                        VehicleRates suvRates,
                        VehicleRates minivanRates) 
    {
        rates[0] = carRates;
        rates[1] = suvRates;
        rates[2] = minivanRates;
    }

    public VehicleRates getCarRates() 
    {
        return rates[0];
    }

    public void setCarRates(VehicleRates r) 
    {
        rates[0] = r;
    }

    public VehicleRates getSUVRates() 
    {
        return rates[1];
    }

    public void setSUVRates(VehicleRates r) 
    {
        rates[1] = r;
    }

    public VehicleRates getMinivanRates() 
    {
        return rates[2];
    }

    public void setMinivanRates(VehicleRates r) 
    {
        rates[2] = r;
    }

    private VehicleRates getRatesByType(int vehicleType) {
        // 1 = car, 2 = suv, 3 = minivan
        if (vehicleType == 1) return rates[0];
        if (vehicleType == 2) return rates[1];
        return rates[2];
    }


    public double calcEstimatedCost(int vehicleType, TimePeriod estimatedRentalPeriod,
                                    int estimatedNumMiles, boolean dailyInsur) 
    {
        VehicleRates vr = getRatesByType(vehicleType);

        double base = 0.0;
        char unit = estimatedRentalPeriod.getUnit();
        int qty = estimatedRentalPeriod.getQuantity();

        if (unit == 'd') 
        {
            base = qty * vr.getDailyRate();
        } 
        else if (unit == 'w') 
        {
            base = qty * vr.getWeeklyRate();
        } 
        else if (unit == 'm') 
        {
            base = qty * vr.getMonthlyRate();
        }

        double mileageCost = estimatedNumMiles * vr.getMileageChrg();

        int totalDays = estimatedRentalPeriod.toDays();
        double insurCost = dailyInsur ? totalDays * vr.getDailyInsurRate() : 0.0;

        return base + mileageCost + insurCost;
    }


public double calcActualCost(VehicleRates rates,
        int num_days_used, int numMilesDriven,
        boolean dailyInsurSelected) 
    {
        double dailyRate = rates.getDailyRate();
        double weeklyRate = rates.getWeeklyRate();
        double monthlyRate = rates.getMonthlyRate();
        double perMile = rates.getMileageChrg();
        double dailyInsurRate = rates.getDailyInsurRate();

        double timeCost = 0.0;

        if (num_days_used >= 31) 
        {
            int months = num_days_used / 31;
            int leftoverDays = num_days_used % 31;

            timeCost = months * monthlyRate + leftoverDays * (monthlyRate / 31.0);
        } 
        else if (num_days_used >= 7) 
        {
            int weeks = num_days_used / 7;
            int leftoverDays = num_days_used % 7;

            timeCost = weeks * weeklyRate + leftoverDays * (weeklyRate / 7.0);
        } 
        else 
        {
            timeCost = num_days_used * dailyRate;
        }

        double mileageCost = numMilesDriven * perMile;
        double insurCost = dailyInsurSelected?num_days_used * dailyInsurRate: 0.0;

        return timeCost + mileageCost + insurCost;
    }
}