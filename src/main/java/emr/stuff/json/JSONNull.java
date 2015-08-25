package emr.stuff.json;

public class JSONNull implements JSONValue
{
	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T getValue()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		return "null";
	}
	
	@Override
	public String getJSON( int depth )
	{
		StringBuilder answer = new StringBuilder();
		for( int i = 0; i < depth; i++ )
		{
			answer.append( "\t" );
		}
		answer.append( "null" );
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
			if( o == this || o instanceof JSONNull ) answer = true;
		}
		return answer;
	}
	
	@Override
	public int hashCode()
	{
		return 13;
	}
}