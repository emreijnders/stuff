package emr.stuff;

import java.util.ArrayList;
import java.util.List;

public class Location implements Comparable<Location>, Bounded
{
	public final int X, Y;
	
	public Location( int x , int y )
	{
		X = x;
		Y = y;
	}
	
	public Direction getRelativeDirection( Location other )
	{
		double xdif = other.X - X;
		double ydif = other.Y - Y;
		int xdir = (int) ( xdif / Math.abs( xdif ) );
		int ydir = (int) ( ydif / Math.abs( ydif ) );
		return Direction.getDirectionByValue( xdir , ydir );
	}
	
	public Location getNewLocation( Direction dir )
	{
		return new Location( X + dir.X , Y + dir.Y );
	}
	
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
			for(int y=Y-distance;y<=Y+distance;y++)
			{
				for(int x=X-distance;x<=X+distance;x++)
				{
					if( x == 0 && y == 0 ) continue; //the original location
					result.add( new Location( x , y ) );
				}
			}
		}
		return result;
	}
	
	@Override
	public Location getTopLeft()
	{
		return this;
	}
	
	@Override
	public int getWidth()
	{
		return 1;
	}
	
	@Override
	public int getHeight()
	{
		return 1;
	}
	
	@Override
	public int compareTo( Location other )
	{
		int answer = 1;
		if( X == other.X && Y == other.Y )
		{
			answer = 0;
		}
		else if( Y < other.Y || ( Y == other.Y && X < other.X ) )
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
			else if( o instanceof Location )
			{
				Location other = (Location) o;
				if( other.X == this.X && other.Y == this.Y )
				{
					result = true;
				}
			}
		}		
		return result;
	}
	
	@Override
	public int hashCode()
	{
		int result = 13;
		result = 23 * result + X;
		result = 23 * result + Y;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "(" + X + "," + Y + ")";
	}
}