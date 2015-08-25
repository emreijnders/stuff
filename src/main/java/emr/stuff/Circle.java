package emr.stuff;

public class Circle implements Comparable<Circle>
{
	public final Location CENTER;
	public final int RADIUS;
	
	public Circle(Location center, int radius)
	{
		CENTER = center;
		RADIUS = radius;
	}
	
	@Override
	public int compareTo(Circle other)
	{
		int answer = -1;
		if(RADIUS == other.RADIUS && CENTER.equals(other.CENTER)) answer = 0;
		else if(RADIUS > other.RADIUS) answer = 1;
		else if(RADIUS == other.RADIUS) answer = CENTER.compareTo(other.CENTER);
		return answer;
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean result = false;
		if(o != null)
		{
			if( o == this) result = true;
			else if( o instanceof Circle)
			{
				Circle other = (Circle) o;
				if(RADIUS == other.RADIUS && CENTER.equals(other.CENTER)) result = true;
			}
		}
		return result;
	}
	
	@Override
	public int hashCode()
	{
		int result = 13;
		result = (result*23) + CENTER.hashCode();
		result = (result*23) + RADIUS;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "Circle at " + CENTER + " with radius " + RADIUS + ".";
	}
}