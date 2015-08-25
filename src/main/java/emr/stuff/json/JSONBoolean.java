package emr.stuff.json;

public class JSONBoolean implements JSONValue
{
	private Boolean value;
	
	public JSONBoolean( boolean value )
	{
		this.value = value;
	}
	
	public Boolean getBoolean()
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
		return value.toString();
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
			if( o == this ) answer = true;
			else if( o instanceof JSONBoolean )
			{
				JSONBoolean other = (JSONBoolean) o;
				if( other.getBoolean().equals( getBoolean() ) ) answer = true;
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