package emr.stuff;

import java.awt.Rectangle;

public class Bounds implements Bounded
{
	private Location topleft;
	private int width;
	private int height;
	
	public Bounds( Location loc , int w , int h )
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
	public int getWidth()
	{
		return width;
	}
	
	@Override
	public int getHeight()
	{
		return height;
	}
	
	public boolean intersects( Bounds other )
	{
		Rectangle a = new Rectangle( topleft.X , topleft.Y , width , height );
		Rectangle b = new Rectangle( other.getTopLeft().X , other.getTopLeft().Y , other.getWidth() , other.getHeight() );
		return a.intersects( b );
	}
	
	@Override
	public String toString()
	{
		return "loc: " + topleft + " w: " + width + " h: " + height;
		
	}
}