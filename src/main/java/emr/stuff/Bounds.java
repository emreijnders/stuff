package emr.stuff;

import java.awt.geom.Rectangle2D;

public class Bounds extends Rectangle2D.Double implements Bounded
{
	public Bounds( LocationDouble loc , double w , double h )
	{
		super( loc.getX() , loc.getY() , w , h );		
	}
	
	@Override
	public LocationDouble getTopLeft()
	{
		return new LocationDouble( getX() , getY() );
	}
	
	@Override
	public Rectangle2D getBoundingRectangle()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return "loc: " + getTopLeft() + " w: " + getWidth() + " h: " + getHeight();
		
	}
}