package emr.stuff.json;

import java.util.List;
import java.util.ArrayList;

public class JSON
{
	private List<JSONObject> objects;
	private List<JSONArray> arrays;
	private int index;
	
	public JSON()
	{
		objects = new ArrayList<>();
		arrays = new ArrayList<>();
		index = 0;
	}
	
	public JSON parseJSON( String jsonstring ) throws JSONException
	{
		index = 0;
		char currentchar;
		try
		{
			while( index < jsonstring.length() )
			{
				currentchar = jsonstring.charAt( index );
				if( Character.isWhitespace( currentchar ) )
				{
					index++;
				}
				else
				{					
					if( currentchar == '{' )
					{
						JSONObject object = parseObject( jsonstring );
						objects.add( object );
					}
					else if( currentchar == '[' )
					{
						JSONArray array = parseArray( jsonstring );
						arrays.add( array );
					}
					else
					{
						index++;
					}
				}
			}
		}
		catch( StringIndexOutOfBoundsException sioobe )
		{
			throw new JSONException( "Error: JSON is incomplete" );
		}
		return this;
	}	
	
	private JSONObject parseObject( String jsonstring ) throws JSONException
	{
		int startindex = index;		
		if( jsonstring.charAt( index ) == '{' )
		{
			index++; //consume the first {			
		}
		else
		{
			throw new JSONException( "Error: missing { for object starting at index " + startindex + " in JSON.parseObject()" );
		}
		char currentchar = jsonstring.charAt( index );
		JSONObject object = new JSONObject();
		boolean commaparsed = true; //true for first pair
		while( currentchar != '}' && index < jsonstring.length() )
		{			
			if( Character.isWhitespace( currentchar ) )
			{
				index++;
			}
			else if( currentchar == ',' )
			{
				if( !commaparsed )
				{
					index++;
					commaparsed = true;
				}
				else
				{
					throw new JSONException( "Error: too many commas in object starting at " + startindex + " in JSON.parseObject()" );
				}
			}
			else
			{				
				if( commaparsed )
				{
					String name =  parseString(jsonstring).getValue();
					while( ( Character.isWhitespace( currentchar ) || currentchar == ':' ) && index < jsonstring.length() )
					{
						index++; //to skip ':' and any whitespace
						currentchar = jsonstring.charAt( index );
					}
					if( index >= jsonstring.length() )
					{
						throw new JSONException( "Error: missing value for pair in object starting at " + startindex + " in JSON.parseObject()");
					}
					else
					{
						JSONValue value = parseValue( jsonstring );
						object.addPair( new JSONPair( name , value ) );
					}
					commaparsed = false;
				}
				else
				{
					throw new JSONException( "Error: missing commas in object starting at " + startindex + " in JSON.parseObject()" );
				}
			}
			currentchar = jsonstring.charAt( index );
		}
		if( jsonstring.charAt( index ) == '}' )
		{
			index++; //consume the last }
		}
		else
		{
			throw new JSONException( "Error: missing } for object starting at index " + startindex + " in JSON.parseObject()" );
		}
		return object;
	}
	
	private JSONArray parseArray( String jsonstring ) throws JSONException
	{
		int startindex = index;
		if( jsonstring.charAt( index ) == '[' )
		{
			index++; //consume the first [
		}
		else
		{
			throw new JSONException( "Error: missing [ for array starting at index " + index + " in JSON.parseArray()" );
		}
		JSONArray array = new JSONArray();
		char currentchar = jsonstring.charAt( index );
		boolean commaparsed = true; //start at true for first value
		while( currentchar != ']' && index < jsonstring.length() )
		{			
			if( Character.isWhitespace( currentchar ) )
			{
				index++;
			}
			else if( currentchar == ',' )
			{
				if( !commaparsed )
				{
					index++;
					commaparsed = true;					
				}
				else
				{
					throw new JSONException("Error: too many commas in array starting at " + startindex + " in JSON.parseArray()");
				}
			}
			else
			{				
				if( commaparsed )
				{
					array.addValue( parseValue(jsonstring) );
					commaparsed = false;
				}
				else
				{
					throw new JSONException("Error: values without commas between them in array starting at " + startindex + " in JSON.parseArray()");
				}
			}
			currentchar = jsonstring.charAt( index );
		}
		if( currentchar == ']' )
		{
			index++; //consume the last ]
		}
		else
		{
			throw new JSONException( "Error: missing ] for array starting at " + startindex + " in JSON.parseArray()" );
		}
		return array;
	}
	
	private JSONString parseString( String jsonstring ) throws JSONException
	{
		int startindex = index;
		if( jsonstring.charAt( index ) == '\"' )
		{
			index++; //consume starting quote
		}
		else
		{
			throw new JSONException( "Error: missing starting quote for string starting at index " + index + " in JSON.parseString()" );
		}
		char currentchar = jsonstring.charAt( index );
		String parsed = "";
		while( !( currentchar == '\"' && jsonstring.charAt( index - 1 ) != '\\' ) && index < jsonstring.length() )
		{
			parsed += currentchar;
			index++;
			currentchar = jsonstring.charAt( index );
		}
		if( !JSON.stringHasValidEscapes( parsed ) )
		{
			throw new JSONException( "Error: invalid escape character in " + parsed + " at index " + startindex + " in JSON.parseString()" );
		}
		if( jsonstring.charAt( index ) == '\"' )
		{
			index++; //consume ending quote
		}
		else
		{			
			throw new JSONException( "Error: missing ending quote for string starting at " + startindex + " in JSON.parseString()" );
		}
		return new JSONString( parsed );
	}
	
	//this is seperated into a static method because this way the constructors for
	//JSONString and JSONPair can use it too without having the same code
	public static boolean stringHasValidEscapes( String string )
	{		
		boolean answer = true;
		for( int i = 0; i < string.length(); i++ )
		{
			if( string.charAt( i ) == '\\' && i + 1 < string.length() )
			{
				char nextchar = string.charAt( i + 1 );
				if( nextchar == '\"' || nextchar == '\\' || nextchar == '/' 
				|| nextchar == 'b' || nextchar == 'f' || nextchar == 'n' 
				|| nextchar == 'r' || nextchar == 't' || nextchar == 'u' )
				{
					if( nextchar == 'u' && i + 5 < string.length() )
					{
						try
						{
							//the next 4 characters should be hex digits
							for(int j = i + 2; j < i + 6; j++ )
							{
								Integer.parseInt( string.substring( j , j + 1 ) , 16 ); //substring instead of charAt because parseInt requires a string
							}
						}
						catch( NumberFormatException nfe )
						{
							//one of the 4 characters was not a hex value
							answer = false;
						}							
					}					
				}
				else
				{
					//the character following the slash was not a valid escape character
					answer = false;
				}
			}
		}
		return answer;
	}
	
	private JSONValue parseValue( String jsonstring ) throws JSONException
	{
		JSONValue value = null;
		char currentchar = jsonstring.charAt( index );
		switch( currentchar )
		{
			case '{':
				value = parseObject( jsonstring );
				break;
			case '[':
				value = parseArray( jsonstring );
				break;
			case '\"':
				value = parseString( jsonstring );
				break;
			case 't':
			case 'f':
				value = parseBoolean( jsonstring );
				break;
			case 'n':
				value = parseNull( jsonstring );
				break;
			default:
				value = parseNumber( jsonstring );
				break;			
		}
		return value;
	}
	
	private JSONBoolean parseBoolean( String jsonstring ) throws JSONException
	{
		JSONBoolean answer = null;
		if( jsonstring.substring( index , index + 4 ).equals( "true" ) )
		{
			answer = new JSONBoolean( true );
			index += 4;
		}
		else if( jsonstring.substring( index , index + 5 ).equals( "false" ) )
		{
			answer = new JSONBoolean( false );
			index += 5;
		}
		else
		{
			throw new JSONException( "Error: something wrong with the value at " + index + " in JSON.parseBoolean()" );
		}
		return answer;
	}
	
	private JSONNull parseNull( String jsonstring ) throws JSONException
	{
		JSONNull answer = null;
		if( jsonstring.substring( index , index + 4 ).equals( "null" ) )
		{
			answer = new JSONNull();
			index += 4;
		}
		else
		{
			throw new JSONException( "Error: something wrong with the value at " + index + " in JSON.parseNull()" );
		}
		return answer;
	}
	
	private JSONNumber parseNumber( String jsonstring ) throws JSONException
	{
		int startindex = index;
		char currentchar = jsonstring.charAt( index );
		String parsed = "";
		while( !( Character.isWhitespace( currentchar ) || currentchar == ',' || currentchar == ']' || currentchar == '}' ) && index < jsonstring.length() )
		{
			parsed += currentchar;
			index++;
			currentchar = jsonstring.charAt( index );
		}
		if( index >= jsonstring.length() )
		{
			throw new JSONException( "Error: JSON incomplete, last element was number starting at index " + startindex + " in JSON.parseNumber()" );
		}
		else
		{
			try
			{
				Double.parseDouble(parsed);
			}
			catch( NumberFormatException nfe )
			{
				throw new JSONException( "Error: number starting at index " + startindex + " is not valid in JSON.parseNumber()" );
			}
		}
		return new JSONNumber( parsed );
	}
	
	public List<JSONObject> getObjects()
	{
		return objects;
	}
	
	public void addObject( JSONObject object )
	{
		objects.add( object );
	}
	
	public List<JSONArray> getArrays()
	{
		return arrays;
	}
	
	public void addArray( JSONArray array )
	{
		arrays.add( array );
	}
	
	public String getJSON()
	{
		StringBuilder answer = new StringBuilder();
		for( JSONObject object : objects )
		{
			answer.append( object.getJSON( 0 ) + System.getProperty( "line.separator" ) );
		}
		for(JSONArray array : arrays)
		{
			answer.append( array.getJSON( 0 ) + System.getProperty( "line.separator" ) );
		}
		return answer.toString();
	}
}