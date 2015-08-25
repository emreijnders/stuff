package emr.stuff.json;

public class JSONNumber implements JSONValue
{
	private String value;
	
	public JSONNumber( String value )
	{
		this.value = value;
	}
	
	public JSONNumber( Number number )
	{
		value = number.toString();
	}
	
	public String getNumber()
	{
		return value;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getValue()
	{
		if( value.contains( "." ) || value.toLowerCase().contains( "e" ) )
		{
			return (T) Double.valueOf( value );
		}
		else
		{
			return (T) Long.valueOf( value );
		}
	}
	
	@Override
	public String toString()
	{
		return value;
	}
	
	@Override
	public String getJSON( int depth )
	{
		StringBuilder answer = new StringBuilder();
		for( int i = 0; i < depth; i++ )
		{
			answer.append( "\t" );
		}
		answer.append( value );
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
			if( o == this ) answer  = true;
			else if( o instanceof JSONNumber )
			{
				JSONNumber other = (JSONNumber) o;
				if( other.getNumber().equals( getNumber() ) ) answer = true;
			}
		}
		return answer;
	}
	
	@Override
	public int hashCode()
	{
		return value.hashCode();
	}
}