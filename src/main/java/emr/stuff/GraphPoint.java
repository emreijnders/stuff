package emr.stuff;

public class GraphPoint implements Comparable<GraphPoint>
{
	private final double X;
	private final double Y;
	
	public GraphPoint( double x , double y )
	{
		X = x;
		Y = y;
	}
	
	public Double getX()
	{
		return X;
	}
	
	public Double getY()
	{
		return Y;
	}
	
	@Override
	public int compareTo(GraphPoint other)
	{
		return getX().compareTo( other.getX() );
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean answer = false;
		if( o != null )
		{
			if( o == this )
			{
				answer = true;
			}
			else if( o instanceof GraphPoint )
			{
				GraphPoint other = (GraphPoint) o;
				if( this.getX().equals( other.getX() ) )
				{
					answer = true;
				}
			}
		}
		return answer;
	}
	
	@Override
	public String toString()
	{
		return "(" + getX() + "," + getY() + ")";
	}
	
	@Override
	public int hashCode()
	{
		return getX().hashCode();
	}
}