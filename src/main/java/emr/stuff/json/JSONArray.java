package emr.stuff.json;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class JSONArray implements JSONValue
{
	private List<JSONValue> values;
	
	public JSONArray()
	{
		values = new ArrayList<>();
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getValue()
	{
		return (T) values;
	}
	
	public List<JSONValue> getValues()
	{
		return values;
	}
	
	public void addValue( JSONValue value )
	{
		values.add( value );
	}
	
	public void addValues( List<JSONValue> valuelist )
	{
		values.addAll( valuelist );
	}
	
	public void removeValue( JSONValue value )
	{
		values.remove( value );
	}
	
	public void removeValues( List<JSONValue> valuelist )
	{
		values.removeAll( valuelist );
	}
	
	@Override
	public String toString()
	{
		return values.toString();
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
		answer.append( "[" );
		answer.append( System.getProperty( "line.separator" ) );
		if( values.size() > 0 )
		{
			Iterator<JSONValue> it = values.iterator();
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
		answer.append( "]" );
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
			else if( o instanceof JSONArray )
			{
				JSONArray other = (JSONArray) o;
				if( other.getValues().equals( getValues() ) ) answer = true;
			}
		}
		return answer;
	}
	
	@Override
	public int hashCode()
	{
		return getValues().hashCode();
	}
}