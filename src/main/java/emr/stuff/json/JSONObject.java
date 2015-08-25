package emr.stuff.json;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class JSONObject implements JSONValue
{
	private List<JSONPair> pairs;
	
	public JSONObject()
	{
		pairs = new ArrayList<>();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue()
	{
		return (T) pairs;
	}
	
	public List<JSONPair> getPairs()
	{
		return pairs;
	}
	
	public JSONPair getPair( String name )
	{
		JSONPair pair = null;
		try
		{
			pair = pairs.get( pairs.indexOf( new JSONPair( name , null ) ) );
		}
		catch( JSONException je )
		{
			//do nothing, if the input is an invalid pairname
			//just return null, same as if the pair isn't found
		}
		return pair;
	}
	
	public void addPair( JSONPair pair )
	{
		pairs.add( pair );
	}
	
	public void addPairs( List<JSONPair> pairlist )
	{
		pairs.addAll( pairlist );
	}
	
	public void removePair( JSONPair pair )
	{
		pairs.remove( pair );
	}
	
	public void removePair( String pairname )
	{
		try
		{
			pairs.remove( new JSONPair( pairname , null ) );
		}
		catch( JSONException je )
		{
			//do nothing, if the name is not valid, nothing is removed
			//and we don't need to return anything
		}
	}
	
	public void removePairs( List<JSONPair> pairlist )
	{
		pairs.removeAll( pairlist );
	}
	
	@Override
	public String toString()
	{
		return pairs.toString();
	}
	
	@Override
	public String getJSON( int depth )
	{
		StringBuilder answer = new StringBuilder();
		if( depth > 0 )
		{			
			for( int i = 0; i < depth; i++ )
			{
				answer.append( "\t" );
			}
		}
		else
		{
			depth = Math.abs( depth );
		}
		answer.append( "{" );
		answer.append( System.getProperty( "line.separator" ) );
		if( pairs.size() > 0 )
		{
			Iterator<JSONPair> it = pairs.iterator();
			answer.append( it.next().getJSON( depth + 1 ) );
			while( it.hasNext() )
			{
				answer.append( "," );
				answer.append( System.getProperty( "line.separator" ) );
				answer.append( it.next().getJSON( depth + 1 ) );
			}
			answer.append( System.getProperty( "line.separator" ) );
		}
		for( int i = 0; i < depth; i++ )
		{
			answer.append( "\t" );
		}
		answer.append( "}" );
		return answer.toString();
	}
	
	@Override
	public String getJSON()
	{
		return getJSON( 0 );
	}
	
	@Override
	public boolean equals( Object o )
	{
		boolean answer = false;
		if( o != null )
		{
			if( o == this ) answer = true;
			else if( o instanceof JSONObject )
			{
				JSONObject other = (JSONObject) o;
				if( other.getPairs().equals( getPairs() ) ) answer = true;
			}
		}
		return answer;
	}
	
	@Override
	public int hashCode()
	{
		return getPairs().hashCode();
	}
}