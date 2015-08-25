package emr.stuff.json;

public class JSONPair implements JSONElement
{
	private String name;
	private JSONValue value;
	
	public JSONPair( String name , JSONValue value ) throws JSONException
	{
		if( JSON.stringHasValidEscapes( name ) )
		{
			this.name = name;
		}
		else
		{
			throw new JSONException( "Error: invalid escape character in " + name + " in JSONPair constructor" );
		}
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public JSONValue getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{		
		return name + " : " + value;
	}
	
	@Override
	public String getJSON( int depth )
	{
		StringBuilder answer = new StringBuilder();
		for( int i = 0; i < depth; i++ )
		{
			answer.append( "\t" );
		}
		answer.append( "\"" );
		answer.append( name );
		answer.append( "\"" );
		answer.append( " : " );
		if( value instanceof JSONArray || value instanceof JSONObject )
		{
			answer.append( value.getJSON( -depth ) );
		}
		else
		{
			answer.append( value.getJSON( 0 ) );
		}
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
			else if( o instanceof JSONPair )
			{
				JSONPair other = (JSONPair) o;
				if( other.getName().equals( getName() ) ) answer = true;
			}
		}
		return answer;
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}
}