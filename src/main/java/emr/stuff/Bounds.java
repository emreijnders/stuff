package emr.stuff;

import java.awt.geom.Rectangle2D;

public class Bounds extends Rectangle2D.Double implements Bounded
{
	public Bounds( Location loc , double w , double h )
	{
		super( loc.X , loc.Y , w , h );		
	}
	
	@Override
	public Location getTopLeft()
	{
		return new Location( (int) getX() , (int) getY() );
	}
	
	@Override
	public boolean intersects( Bounded other )
	{
		return intersects( other.getTopLeft().X , other.getTopLeft().Y , other.getWidth() , other.getHeight() );
	}
	
	@Override
	public String toString()
	{
		return "loc: " + getTopLeft() + " w: " + getWidth() + " h: " + getHeight();
		
	}
}