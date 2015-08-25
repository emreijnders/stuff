package emr.stuff.json;

public class JSONString implements JSONValue
{
	private String value;
	
	public JSONString( String value ) throws JSONException
	{
		if( JSON.stringHasValidEscapes( value ) )
		{
			this.value = value;
		}
		else
		{
			throw new JSONException( "Error: invalid escape character in " + value + " in JSONString constructor" );
		}
	}
	
	public String getString()
	{
		return value;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getValue()
	{
		return (T) value;
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
		for(int i = 0; i < depth; i++ )
		{
			answer.append( "\t" );
		}		
		answer.append( "\"" );
		answer.append( value );
		answer.append( "\"" );
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
			else if( o instanceof JSONString )
			{
				JSONString other = (JSONString) o;
				if( other.getString().equals( getString() ) ) answer = true;
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