package emr.stuff;

import java.util.List;
import java.util.ArrayList;
import emr.stuff.LocationDouble;
import emr.stuff.Bounds;
import emr.stuff.Bounded;

public class QuadTree<T extends Bounded>
{
	private static final int MAX_LEAVES = 2;
	private static final int MAX_LEVEL = 5;
	
	private int level;
	private List<T> leaves;
	private Bounded bounds;
	private List<QuadTree<T>> nodes;
	
	public QuadTree( Bounded bounds )
	{
		this( 0 , bounds );
	}
	
	private QuadTree( int level , Bounded bounds )
	{
		this.level = level;
		this.bounds = bounds;
		leaves = new ArrayList<>();
		nodes = new ArrayList<>();
	}
	
	public void clearTree()
	{
		leaves.clear();
		for( QuadTree<T> node : nodes )
		{
			node.clearTree();
		}
		nodes.clear();
	}
	
	private void split()
	{
		int newwidth = (int) ( bounds.getWidth() / 2.0 );
		int newheight = (int) ( bounds.getHeight() / 2.0 );
		LocationDouble topleft = bounds.getTopLeft();
		int nextlevel = level + 1;
		
		nodes.add( new QuadTree<T>( nextlevel , new Bounds( new LocationDouble( topleft.getX() + newwidth , topleft.getY() ) , newwidth , newheight ) ) );
		nodes.add( new QuadTree<T>( nextlevel , new Bounds( new LocationDouble( topleft.getX() , topleft.getY() ) , newwidth , newheight ) ) );
		nodes.add( new QuadTree<T>( nextlevel , new Bounds( new LocationDouble( topleft.getX() , topleft.getY() + newheight ) , newwidth , newheight ) ) );
		nodes.add( new QuadTree<T>( nextlevel , new Bounds( new LocationDouble( topleft.getX() + newwidth , topleft.getY() + newheight ) , newwidth , newheight ) ) );
	}
	
	private int getIndex( Bounded item )
	{
		int index = -1;
		double hmiddle = bounds.getTopLeft().getX() + ( bounds.getWidth() / 2.0 );
		double vmiddle = bounds.getTopLeft().getY() + ( bounds.getHeight() / 2.0 );
		
		boolean top = item.getTopLeft().getY() < vmiddle && item.getTopLeft().getY() + item.getHeight() <= vmiddle;
		boolean bottom = item.getTopLeft().getY() >= vmiddle;
		boolean left = item.getTopLeft().getX() < hmiddle && item.getTopLeft().getX() + item.getWidth() <= hmiddle;
		boolean right = item.getTopLeft().getX() >= hmiddle;
		if( top )
		{
			if( left )
			{
				index = 1;
			}
			else if( right )
			{
				index = 0;
			}
		}
		else if( bottom )
		{
			if( left )
			{
				index = 2;
			}
			else if( right )
			{
				index = 3;
			}
		}
		return index;		
	}
	
	public void insert( T item )
	{
		if( !nodes.isEmpty() )
		{
			int index = getIndex( item );
			if( index != -1 )
			{
				nodes.get( index ).insert( item );
				return;
			}
		}
		leaves.add( item );
		if( leaves.size() > MAX_LEAVES && level < MAX_LEVEL )
		{
			if( nodes.isEmpty() )
			{
				split();
			}
			
			int i = 0;
			while( i < leaves.size() )
			{
				int index = getIndex( leaves.get( i ) );
				if( index != -1 )
				{
					nodes.get( index ).insert( leaves.remove( i ) );
				}
				else
				{
					i++;
				}
			}
		}
	}
	
	public List<T> retrieve( Bounded area )
	{
		return retrieve( new ArrayList<T>() , area );
	}
	
	private List<T> retrieve( List<T> returnlist , Bounded area )
	{
		int index = getIndex( area );
		if( !nodes.isEmpty() )
		{
			if( index != -1 )
			{
				nodes.get( index ).retrieve( returnlist , area );
			}
			else
			{
				for( QuadTree<T> node : nodes )
				{
					node.retrieve( returnlist , area );
				}
			}
		}
		returnlist.addAll( leaves );
		return returnlist;
	}
	
	/*
	public List<Bounded> getAllBoundsWithLeaves( List<Bounded> list )
	{
		for( QuadTree<T> node : nodes )
		{
			node.getAllBoundsWithLeaves( list );
		}
		if( hasLeaves() )
		{
			list.add( bounds );
		}
		return list;
	}
	*/
	
	public List<T> getAllLeaves( List<T> list )
	{
		for( QuadTree<T> node : nodes )
		{
			node.getAllLeaves( list );
		}
		list.addAll( leaves );
		return list;
	}
	
	public boolean hasLeaves()
	{
		return !leaves.isEmpty();
	}
	
	public Bounded getBounds()
	{
		return bounds;
	}
	
	@Override
	public String toString()
	{
		String result = " QT lvl " + level;
		result += " bounds " + bounds.getTopLeft() + " " + bounds.getWidth() + " " + bounds.getHeight();
		result += " has nodes: " + ( !nodes.isEmpty() );
		result += " has leaves: " + leaves;
		return result;
	}
}