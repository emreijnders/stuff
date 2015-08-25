package emr.stuff;

public class Setting implements Comparable<Setting>
{
	private final String NAME;
	private final String VALUE;
	
	public Setting( String name , String value )
	{
		NAME = name;
		VALUE = value;
	}
	
	public String getName()
	{
		return NAME;
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T getValue()
	{		
		Object answer = null;
		boolean notanumber = false;
		if( VALUE.equals( "true" ) )
		{
			answer = new Boolean( true );
		}
		else if( VALUE.equals( "false" ) )
		{
			answer = new Boolean( false );
		}
		else
		{
			try
			{
				if( VALUE.contains( "." ) || VALUE.toUpperCase().contains( "E" ) )
				{
					answer = Double.parseDouble( VALUE );
				}
				else
				{
					answer = Long.parseLong( VALUE );
				}
			}
			catch( NumberFormatException nfe )
			{
				notanumber = true;
			}
			if( notanumber )
			{
				answer = VALUE;
			}
		}
		return (T) answer;
	}
	
	@Override
	public int hashCode()
	{
		return NAME.hashCode();
	}
	
	@Override
	public String toString()
	{
		return NAME + "[" + VALUE + "]";
	}
	
	public String toShortenedString()
	{
		String answer = "";
		if( NAME.length() > 4 )
		{
			if( NAME.contains( "_" ) )
			{
				String[] parts = NAME.split( "_" );
				for( String part : parts )
				{
					if( part.length() > 2 )
					{
						answer += part.substring( 0 , 2 ) + "_";
					}
					else
					{
						answer += part;
					}
				}
				answer = answer.substring( 0 , answer.length() - 1 );
			}
			else
			{
				answer += NAME.substring( 0 , 4 );
			}
		}
		else
		{
			answer = NAME;
		}
		if( VALUE.equals( "false" ) )
		{
			answer = "!" + answer;
		}
		else if( !VALUE.equals( "true" ) )
		{
			answer += "[" + VALUE + "]";
		}
		return answer;
	}
	
	@Override
	public boolean equals( Object o )
	{
		boolean answer = false;
		if( o != null )
		{
			if( o == this ) answer = true;
			else if( o instanceof Setting )
			{
				Setting other = (Setting) o;
				if( other.NAME.equals(NAME) ) answer = true;
			}			
		}
		return answer;
	}
	
	@Override
	public int compareTo(Setting other)
	{
		return NAME.compareTo( other.NAME );		
	}
}