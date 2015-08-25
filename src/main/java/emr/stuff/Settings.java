package emr.stuff;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import emr.stuff.json.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Settings
{
	private Set<Setting> settings;
	
	public Settings()
	{
		settings = new HashSet<>();
	}
	
	public Settings addSettings( Collection<Setting> collection )
	{
		for( Setting setting : collection )
		{
			addSetting( setting );
		}
		return this;
	}
	
	public Settings parseCommandlineInput( String[] input )
	{
		for( String parameter : input )
		{
			int index = parameter.indexOf( ':' );
			if( index > 0 )
			{
				try
				{
					addSetting( parameter.substring( 0 , index ) , parameter.substring( index + 1 ) );
				}
				catch( ArrayIndexOutOfBoundsException aioobe )
				{
					System.out.println( "Error: bad parameter format " + parameter );
					System.exit( 0 );
				}
			}
			else
			{
				if( parameter.startsWith( "!" ) )
				{
					try
					{
						addSetting( parameter.substring( 1 ) , "false" );
					}
					catch(ArrayIndexOutOfBoundsException aioobe)
					{
						System.out.println( "Error: bad parameter format " + parameter );
						System.exit( 0 );
					}					
				}
				else
				{
					addSetting( parameter , "true" );
				}
			}
		}
		return this;
	}
	
	public Settings parseJSONInput( JSON json )
	{
		List<JSONValue> list = json.getArrays()
								.get(0)
								.<List<JSONValue>>getValue();
		for( JSONValue value : list )
		{
			List<JSONPair> pairs = value.<List<JSONPair>>getValue();
			String name = "";
			String valuestring = "";
			name = pairs.get(0).getName();
			valuestring = pairs.get(0)
					.getValue()
					.toString();
			addSetting(name, valuestring);
		}
		return this;
	}
	
	public void saveAsSettingsFile( String filename )
	{
		List<String> output = new ArrayList<>();
		//header
		output.add( "# this settings file was generated automatically" );
		output.add( System.lineSeparator() );
		output.add( "# format is @name on one line then value on the next line, # for comments" );
		output.add( System.lineSeparator() );
		output.add( "#" );
		output.add( System.lineSeparator() );
		output.add( System.lineSeparator() );
		//settings
		for( Setting setting : this.getSettings() )
		{
			output.add( "@" + setting.getName() );
			output.add( System.lineSeparator() );
			output.add( setting.getValue().toString() );
			output.add( System.lineSeparator() );
			output.add( System.lineSeparator() );
		}
		TextFileIO.writeFile( output, filename );
	}
	
	public void saveAsJSON( String filename )
	{
		JSONArray array = new JSONArray();
		for( Setting setting : this.getSettings() )
		{
			JSONPair pair = null;
			try
			{
				pair = new JSONPair( setting.getName() , getJSONValue( setting.getValue() ) );
			}
			catch( JSONException je )
			{
				System.err.println( je.getMessage() );
			}
			JSONObject object = new JSONObject();
			object.addPair( pair );
			array.addValue( object );
		}
		JSON json = new JSON();
		json.addArray( array );
		
		try( BufferedWriter out = new BufferedWriter( new FileWriter(filename) ) )
		{
			out.write( json.getJSON() );			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	private <T> JSONValue getJSONValue( T value )
	{
		JSONValue answer = null;
		if( value instanceof String )
		{
			try
			{
				answer = new JSONString( (String) value );
			}
			catch( JSONException je )
			{
				System.err.println( je.getMessage() );
			}
		}
		else if( value instanceof Boolean )
		{
			answer = new JSONBoolean( (Boolean) value );
		}
		else
		{
			answer = new JSONNumber( (Number) value );
		}
		return answer;
	}
	
	/*
	public Settings parseXMLInput(String xml)
	{
		String currentchar = "";
		String name = "";
		String value = "";
		String tag = "";		
		int i = 0;
		while(i < xml.length())
		{
			currentchar = xml.substring(i,i+1);
			if(currentchar.equals("<"))
			{
				i++;
				int index = xml.substring(i).indexOf(">");
				if(xml.substring(i).startsWith("/") || xml.substring(i).startsWith("?"))
				{
					tag = "";
				}
				else
				{
					tag = xml.substring(i,i+index);
				}
				i += index + 1;
			}
			else
			{
				int index = xml.substring(i).indexOf("<");
				if(tag.startsWith("name"))
				{
					name = xml.substring(i,i+index);
				}
				else if(tag.startsWith("value"))
				{
					value = xml.substring(i,i+index);
				}
				if(!name.equals("") && !value.equals(""))
				{
					addSetting(name, value);
					name = "";
					value = "";
				}
				i += index;
			}
		}
		return this;
	}
	*/
	
	public boolean hasSetting( String name )
	{		
		boolean answer = false;
		for( Setting setting : settings )
		{
			if( setting.getName().equals( name ) )
			{
				answer = true;
				break;
			}
		}
		return answer;
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T getSetting( String name )
	{
		for( Setting setting : settings )
		{
			if( setting.getName().equals( name ) )
			{
				return setting.getValue();
			}
		}
		return null;
		//if you don't want a null value
		//don't call getSetting without calling hasSetting first
		//or use getSetting with a default value
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T getSetting( String name, T defaultvalue )
	{		
		if( !hasSetting( name ) )
		{
			addSetting( name, defaultvalue.toString() );
		}
		return getSetting(name);
	}
	
	public <T> void addSetting( String name, T value )
	{
		addSetting( new Setting( name, value.toString() ) );
	}
	
	public void removeSetting( String name )
	{
		settings.remove( new Setting( name , "" ) );
	}
	
	public void addSetting( Setting setting )
	{
		//only the newest instance of a setting is used
		//so we remove the previous instance, if it exists
		removeSetting( setting.getName() );
		settings.add( setting );
	}
	
	public Set<Setting> getSettings()
	{
		return new HashSet<Setting>( settings );
	}
}