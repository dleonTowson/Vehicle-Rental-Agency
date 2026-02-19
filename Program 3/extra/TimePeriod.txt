public class TimePeriod 
{

    private char unit;     // 'd' = days, 'w' = weeks, 'm' = months
    private int quantity;  // how many days/weeks/months

    public TimePeriod(char unit, int quantity) 
    {
        this.unit = unit;
        this.quantity = quantity;
    }

    public char getUnit() 
    {
        return unit;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    @Override
    public String toString() 
    {
        String word;
        switch (unit) 
        {
            case 'd': word = "day(s)"; break;
            case 'w': word = "week(s)"; break;
            case 'm': word = "month(s)"; break;
            default:  word = "unit(s)";  break;
        }
        return quantity + " " + word;
    }


    public int toDays() 
    {
        if (unit == 'd') 
        {
            return quantity;
        } 
        else if (unit == 'w') 
        {
            return quantity * 7;
        } 
        else if (unit == 'm') 
        {
            return quantity * 31;
        } 
        else 
        {
            return quantity; // fallback
        }
    }
}