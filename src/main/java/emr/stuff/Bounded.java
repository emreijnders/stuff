package emr.stuff;

public interface Bounded
{
	public Location getTopLeft();
	public double getWidth();
	public double getHeight();
	public boolean intersects( Bounded other );
}