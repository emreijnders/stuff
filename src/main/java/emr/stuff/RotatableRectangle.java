package emr.stuff;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.awt.geom.AffineTransform;

public class RotatableRectangle implements Bounded
{
	private Path2D path;
	private LocationDouble center;
	private final int width;
	private final int height;
	private double angle;
	private List<LocationDouble> corners;
	private final double radius;	
	private Rectangle2D bounding_rectangle;
	
	public RotatableRectangle( LocationDouble center, int width , int height , double angle )
	{
		this.center = center;
		this.width = width;
		this.height = height;
		this.angle = angle;		
		update();
		double w2 = width / 2.0;
		double h2 = height / 2.0;
		radius = Math.sqrt( ( w2 * w2 ) + ( h2 * h2 ) );
	}
	
	@Override
	public LocationDouble getTopLeft()
	{
		return new LocationDouble( bounding_rectangle.getX() , bounding_rectangle.getY() );
	}
	
	@Override
	public double getWidth()
	{
		return bounding_rectangle.getWidth();
	}
	
	@Override
	public double getHeight()
	{
		return bounding_rectangle.getHeight();
	}
	
	@Override
	public Rectangle2D getBoundingRectangle()
	{
		return bounding_rectangle;
	}
	
	public Path2D getPath()
	{
		return path;
	}
	
	public void setAngle( double angle )
	{
		this.angle = angle;
		update();
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setLocationDouble( LocationDouble loc )
	{
		center = loc;		
		update();
	}
	
	private void update()
	{		
		path = createPath();
		bounding_rectangle = path.getBounds2D();		
	}
	
	public List<LocationDouble> getCorners()
	{
		return corners;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	public LocationDouble getCenter()
	{
		return center;
	}
	
	public boolean intersects( RotatableRectangle other )
	{
		boolean answer = true;
		double dx = Math.abs( other.getCenter().getX() - center.getX() );
		double dy = Math.abs( other.getCenter().getY() - center.getY() );
		double distance = Math.sqrt( ( dx * dx ) + ( dy * dy ) );
		if( distance > getRadius() + other.getRadius() )
		{
			answer = false;
		}
		else
		{
			//make axes
			List<LocationDouble> axes = new ArrayList<>();
			fillAxes( axes , corners );
			fillAxes( axes , other.getCorners() );
			
			//project stuff
			for( LocationDouble axis : axes )
			{
				//get projection from corners
				Set<Double> pvalues1 = new HashSet<>();
				Set<Double> pvalues2 = new HashSet<>();
				for( LocationDouble corner : corners )
				{
					LocationDouble projection = getProjection( axis , corner );
					double pvalue = getPValue( axis , projection );
					pvalues1.add( pvalue );
				}
				for( LocationDouble corner : other.getCorners() )
				{
					LocationDouble projection = getProjection( axis , corner );
					double pvalue = getPValue( axis , projection );
					pvalues2.add( pvalue );
				}
				
				//find min and max for both rects
				if( Collections.min( pvalues2 ) > Collections.max( pvalues1 ) || Collections.max( pvalues2 ) < Collections.min( pvalues1 ) )
				{
					answer = false;
					break;
				}			
			}
		}
		return answer;
	}
	
	private void fillAxes( List<LocationDouble> axes , List<LocationDouble> corners )
	{
		for( int i = 1; i < 4; i = i + 2 )
		{
			double x = corners.get( 0 ).getX() - corners.get( i ).getX();
			double y = corners.get( 0 ).getY() - corners.get( i ).getY();
			axes.add( new LocationDouble( x , y ) );
		}		
	}
	
	private double getPValue( LocationDouble axis , LocationDouble projection )
	{
		return ( axis.getX() * projection.getX() ) + ( axis.getY() * projection.getY() );
	}
	
	private LocationDouble getProjection( LocationDouble axis , LocationDouble corner )
	{
		double proj = ( ( corner.getX() * axis.getX() ) + ( corner.getY() * axis.getY() ) ) / ( ( axis.getX() * axis.getX() ) + ( axis.getY() * axis.getY() ) );
		double px = proj * axis.getX();
		double py = proj * axis.getY();
		return new LocationDouble( px , py );
	}
	
	public AffineTransform getTransform( int source_width , int source_height )
	{
		AffineTransform transform = new AffineTransform();
		
		transform.rotate( angle , center.getX() , center.getY() );
		
		double sx = center.getX() - ( width / 2.0 );
		double sy = center.getY() - ( height / 2.0 );
		transform.translate( sx , sy );
		
		double ws = ( width * 1.0 ) / source_width;
		double hs = ( height * 1.0 ) / source_height;
		transform.scale( ws , hs );
		
		return transform;
	}
	
	private Path2D createPath()
	{
		Path2D p = new Path2D.Double( Path2D.WIND_EVEN_ODD );
		double current_angle = angle;
		corners = new ArrayList<>();
		boolean flip = false;
		LocationDouble start = getStart();
		corners.add( start );
		p.moveTo( start.getX() , start.getY() );
		for( int i = 0; i < 4; i++ )
		{
			int length;
			if( !flip )
			{
				length = height;
			}
			else
			{
				length = width;
			}
			LocationDouble next = getEnd( length , current_angle , start );
			corners.add( next );
			p.lineTo( next.getX() , next.getY() );
			current_angle = current_angle + ( Math.PI / 2.0 );
			start = next;
			flip = !flip;
		}
		p.closePath();
		return p;
	}
	
	public LocationDouble getStart()
	{
		double a = width / 2.0;
		double b = height / 2.0;
		double c = Math.sqrt( ( a * a ) + ( b * b ) );
		double angle2 = Math.acos( b / c );
		double angle3 = angle + Math.PI + angle2;
		return getEnd( c , angle3 , center );		
	}
	
	private LocationDouble getEnd( double length , double angle , LocationDouble start )
	{
		double w = Math.sin( angle ) * length;
		double h = Math.cos( angle ) * length;
		double x = start.getX() + w;
		double y = start.getY() - h;
		return new LocationDouble( x , y );
	}
	
	@Override
	public boolean equals( Object o )
	{
		boolean answer = false;
		if( o != null )
		{
			if( o == this )
			{
				answer = true;
			}
			else if( o instanceof RotatableRectangle )
			{
				RotatableRectangle other = (RotatableRectangle) o;
				if( getCenter().equals( other.getCenter() ) 
					&& getAngle() == other.getAngle() 
					&& getWidth() == other.getWidth()
					&& getHeight() == other.getHeight() )
				{
					answer = true;
				}
			}
		}
		return answer;
	}
}