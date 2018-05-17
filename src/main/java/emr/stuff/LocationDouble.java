package emr.stuff;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LocationDouble extends Point2D.Double implements Comparable<LocationDouble>, Bounded
{
	private final int width , height;
	
	public LocationDouble( double x , double y )
	{
		super( x , y );
		width = 1;
		height = 1;
	}
	
	public Direction getRelativeDirection( LocationDouble other )
	{
		double xdif = other.getX() - getX();
		double ydif = other.getY() - getY();
		double xdir = xdif / Math.abs( xdif );
		double ydir = ydif / Math.abs( ydif );
		return Direction.getDirectionByValue( xdir , ydir );
	}
	
	public LocationDouble getNextLocation( Direction dir )
	{
		return new LocationDouble( getX() + dir.X , getY() + dir.Y );
	}
	
	/*
	public List<Location> getNeighbours()
	{
		return getNeighbours( 1 );
	}
	
	//distance here is 1 on diagonals as well
	public List<Location> getNeighbours( int distance )
	{
		List<Location> result = new ArrayList<>();
		if( distance > 0 )
		{
			//this method does NOT check the validity of the locations returned! do that yourself			
			for( int y = getY() - distance; y <= getY() + distance; y++ )
			{
				for( int x = getX() - distance; x <= getX() + distance; x++ )
				{
					if( x == 0 && y == 0 ) continue; //the original location
					result.add( new Location( x , y ) );
				}
			}
		}
		return result;
	}
	*/
	
	@Override
	public LocationDouble getTopLeft()
	{
		return this;
	}
	
	@Override
	public double getWidth()
	{
		return width;
	}
	
	@Override
	public double getHeight()
	{
		return height;
	}
	
	@Override
	public Rectangle2D getBoundingRectangle()
	{
		return new Rectangle2D.Double( getX() , getY() , width , height );
	}
	
	@Override
	public int compareTo( LocationDouble other )
	{
		int answer = 1;
		if( getX() == other.getX() && getY() == other.getY() )
		{
			answer = 0;
		}
		else if( getY() < other.getY() || ( getY() == other.getY() && getX() < other.getX() ) )
		{
			answer = -1;
		}
		return answer;
	}
	
	@Override
	public boolean equals( Object o )
	{		
		boolean result = false;
		if( o != null )
		{
			if( o == this )
			{
				result = true;
			}
			else if( o instanceof LocationDouble )
			{
				LocationDouble other = (LocationDouble) o;
				if( other.getX() == getX() && other.getY() == getY() )
				{
					result = true;
				}
			}
		}		
		return result;
	}
	
	@Override
	public String toString()
	{
		return "(" + getX() + "," + getY() + ")";
	}
}