package emr.stuff;

import java.util.List;
import java.util.ArrayList;
import emr.stuff.Location;
import emr.stuff.Bounds;
import emr.stuff.Bounded;

public class QuadTree<T extends Bounded>
{
	private static final int MAX_LEAVES = 2;
	private static final int MAX_LEVEL = 5;
	
	private int level;
	private List<T> leaves;
	private Bounded bounds;
	private QuadTree[] nodes;
	
	public QuadTree( Bounded bounds )
	{
		this( 0 , bounds );
	}
	
	private QuadTree( int level , Bounded bounds )
	{
		this.level = level;
		this.bounds = bounds;
		leaves = new ArrayList<>();
		nodes = new QuadTree[ 4 ];
	}
	
	public void clearTree()
	{
		leaves.clear();
		for( int i = 0; i < nodes.length; i++ )
		{
			if( nodes[ i ] != null )
			{
				nodes[ i ].clearTree();
				nodes[ i ] = null;
			}
		}
	}
	
	private void split()
	{
		int newwidth = (int) ( bounds.getWidth() / 2.0 );
		int newheight = (int) ( bounds.getHeight() / 2.0 );
		Location topleft = bounds.getTopLeft();
		int nextlevel = level + 1;
		
		nodes[ 0 ] = new QuadTree<T>( nextlevel , new Bounds( new Location( topleft.X + newwidth , topleft.Y ) , newwidth , newheight ) );
		nodes[ 1 ] = new QuadTree<T>( nextlevel , new Bounds( new Location( topleft.X , topleft.Y ) , newwidth , newheight ) );
		nodes[ 2 ] = new QuadTree<T>( nextlevel , new Bounds( new Location( topleft.X , topleft.Y + newheight ) , newwidth , newheight ) );
		nodes[ 3 ] = new QuadTree<T>( nextlevel , new Bounds( new Location( topleft.X + newwidth , topleft.Y + newheight ) , newwidth , newheight ) );
	}
	
	private int getIndex( T item )
	{
		int index = -1;
		int hmiddle = bounds.getTopLeft().X + ( (int) ( bounds.getWidth() / 2.0 ) );
		int vmiddle = bounds.getTopLeft().Y + ( (int) ( bounds.getHeight() / 2.0 ) );
		
		boolean top = item.getTopLeft().Y < vmiddle && item.getTopLeft().Y + item.getHeight() <= vmiddle;
		boolean bottom = item.getTopLeft().Y >= vmiddle;
		boolean left = item.getTopLeft().X < hmiddle && item.getTopLeft().X + item.getWidth() <= hmiddle;
		boolean right = item.getTopLeft().X >= hmiddle;
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
		if( nodes[ 0 ] != null )
		{
			int index = getIndex( item );
			if( index != -1 )
			{
				nodes[ index ].insert( item );
				return;
			}
		}
		leaves.add( item );
		if( leaves.size() > MAX_LEAVES && level < MAX_LEVEL )
		{
			if( nodes[ 0 ] == null )
			{
				split();
			}
			
			int i = 0;
			while( i < leaves.size() )
			{
				int index = getIndex( leaves.get( i ) );
				if( index != -1 )
				{
					nodes[ index ].insert( leaves.remove( i ) );
				}
				else
				{
					i++;
				}
			}
		}
	}
	
	public List<T> retrieve( List<T> returnlist , Bounded item )
	{
		int index = getIndex( item );
		if( nodes[ 0 ] != null )
		{
			if( index != -1 )
			{
				nodes[ index ].retrieve( returnlist , item );
			}
			else
			{
				for(QuadTree<T> node : nodes )
				{
					node.retrieve( returnlist , item );
				}
			}
		}
		returnlist.addAll( leaves );
		return returnlist;
	}
	
	public List<Bounds> getAllBoundsWithLeaves( List<Bounds> list )
	{
		if( nodes[ 0 ] != null )
		{
			for( QuadTree<T> node : nodes )
			{
				node.getAllBoundsWithLeaves( list );
			}
		}
		if( hasLeaves() )
		{
			list.add( bounds );
		}
		return list;
	}
	
	public List<T> getAllLeaves( List<T> list )
	{
		if( nodes[ 0 ] != null )
		{
			for( QuadTree<T> node : nodes )
			{
				node.getAllLeaves( list );
			}
		}
		list.addAll( leaves );
		return list;
	}
	
	public boolean hasLeaves()
	{
		return !leaves.isEmpty();
	}
	
	public Bounds getBounds()
	{
		return bounds;
	}
	
	@Override
	public String toString()
	{
		String result = " QT lvl " + level;
		result += " bounds " + bounds.getTopLeft() + " " + bounds.getWidth() + " " + bounds.getHeight();
		result += " has nodes: " + ( nodes[ 0 ] != null );
		result += " has leaves: " + leaves;
		return result;
	}
}