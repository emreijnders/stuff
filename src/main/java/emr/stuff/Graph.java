package emr.stuff;

import java.util.*;
import java.awt.Color;

public class Graph
{
	private SortedSet<GraphPoint> graph;
	private final String NAME;
	private final Color COLOUR;
	
	public Graph( String name , Color colour )
	{
		NAME = name;
		COLOUR = colour;
		graph = new TreeSet<>();		
	}
	
	public Graph( String name )
	{
		this( name , Color.BLACK );
	}
	
	public String getName()
	{
		return NAME;
	}
	
	public Color getColour()
	{
		return COLOUR;
	}
	
	public void addPoint( double x , double y )
	{
		addPoint( new GraphPoint( x , y ) );		
	}
	
	public void addPoint( GraphPoint point )
	{
		graph.add( point );
	}
	
	public void addPoints( Collection<GraphPoint> points )
	{
		graph.addAll( points );
	}
	
	public SortedSet<GraphPoint> getPoints()
	{		
		//to prevent concurrent modification we return a different set with the same elements
		return new TreeSet<>( graph );
	}
	
	//range is from startx (inclusive) to the last element of graph (inclusive)
	public Graph getSubGraph( double startx )
	{
		GraphPoint start = new GraphPoint( startx , 0.0 );
		Graph answer = new Graph( getName() , getColour() );
		answer.addPoints( graph.tailSet( start ) );
		return answer;
	}
	
	//range is from startx (inclusive) to endx (exclusive)
	public Graph getSubGraph( double startx , double endx )
	{
		GraphPoint start = new GraphPoint( startx , 0.0 );
		GraphPoint end = new GraphPoint( endx , 0.0 );
		Graph answer = new Graph( getName() , getColour() );
		answer.addPoints( graph.subSet( start , end ) );
		return answer;
	}
	
	public int getNumberOfPoints()
	{
		return graph.size();
	}
	
	public double getLowestX()
	{
		return graph.first().getX();
	}
	
	public double getHighestX()
	{
		return graph.last().getX();
	}
	
	public double getHighestY()
	{
		return graph.parallelStream()
				.max( Comparator.comparing( p -> p.getY() ) )
				.get()
				.getY();		
	}
	
	public double getLowestY()
	{
		return graph.parallelStream()
				.min( Comparator.comparing( p -> p.getY() ) )
				.get()
				.getY();		
	}
	
	@Override
	public String toString()
	{
		return "Name: " + getName() + " Colour: " + getColour() + " Points:" + graph.toString();
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
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
			else if( o instanceof Graph )
			{
				Graph other = (Graph) o;
				if( other.getName().equals( this.getName() ) )
				{
					answer = true;
				}
			}
		}
		return answer;
	}
}