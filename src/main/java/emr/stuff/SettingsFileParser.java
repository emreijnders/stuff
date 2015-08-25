package emr.stuff;

import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.InputStream;

public class SettingsFileParser
{
	private Scanner scanner;
	
	public SettingsFileParser( String filename )
	{
		try
		{
			scanner = new Scanner( Paths.get( filename ) , "UTF-8" );
		}
		catch( IOException ioe )
		{
			System.err.println( "Error reading file: " + filename );
			ioe.printStackTrace();
		}
	}
	
	public SettingsFileParser( InputStream stream )
	{
		scanner = new Scanner( stream , "UTF-8" );
	}
	
	public Settings parseFile()
	{
		Settings settings = new Settings();
		while( scanner.hasNextLine() )
		{
			String name = "";
			String value = "";
			String line = scanner.nextLine();
			if( line.startsWith( "@" ) )
			{
				name = line.substring(1); //skip the @
				while( scanner.hasNextLine() )
				{
					line = scanner.nextLine();
					if( !line.startsWith( "#" ) && !isWhitespace( line ) )
					{
						value = line;
						break;
					}
				}				
				if( !name.equals( "" ) && !value.equals( "" ) ) //neither is an empty string
				{
					settings.addSetting( name , value );
				}
			}
		}
		return settings;
	}
	
	private boolean isWhitespace( String line )
	{
		boolean answer = true;
		for( int i = 0; i < line.length(); i++ )
		{
			if( !Character.isWhitespace( line.charAt( i ) ) )
			{
				answer = false;
			}
		}
		return answer;
	}
}