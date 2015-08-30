package emr.stuff;

import java.awt.geom.Rectangle2D;

public class Bounds implements Bounded
{
	private Location topleft;
	private double width;
	private double height;
	
	public Bounds( Location loc , double w , double h )
	{
		topleft = loc;
		width = w;
		height = h;
	}
	
	@Override
	public Location getTopLeft()
	{
		return topleft;
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
	public boolean intersects( Bounded other )
	{
		return new Rectangle2D.Double( topleft.X , topleft.Y , width , height )
			.intersects( other.getTopLeft().X , other.getTopLeft().Y , other.getWidth() , other.getHeight() );
	}
	
	@Override
	public String toString()
	{
		return "loc: " + topleft + " w: " + width + " h: " + height;
		
	}
}