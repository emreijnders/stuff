package emr.stuff;

import java.awt.geom.Rectangle2D;

public interface Bounded
{
	public LocationDouble getTopLeft();
	public double getWidth();
	public double getHeight();
	public Rectangle2D getBoundingRectangle();
}