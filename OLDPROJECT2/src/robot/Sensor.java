package robot;

public class Sensor {

	public static final int SHORT_RANGE = 3;
	public static final int LONG_RANGE = 5;

	private int range;

	public Sensor(int range) 
	{
		this.range = range;
	}

	public int getRange() 
	{
		return range;
	}
}
