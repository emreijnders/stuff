package emr.stuff;

import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;

public class TextFileIO
{
	public static String readFile(String filename)
	{		
		List<String> lines = null;
		try
		{
			lines = Files.readAllLines( Paths.get(filename), Charset.forName("UTF-8") );
		}
		catch(IOException ioe)
		{
			System.err.println("Problem with reading file from " + filename);
			ioe.printStackTrace();
		}		
		StringBuilder sb = new StringBuilder();
		if( lines.size() > 1 )
		{
			for( String string : lines )
			{
				sb.append(string);
				sb.append( System.getProperty("line.separator") );
			}
		}
		else
		{
			for( String string : lines )
			{
				sb.append(string);
			}
		}
		return sb.toString();
	}
	
	public static void writeFile(String text, String filename)
	{
		List<String> temp = new ArrayList<>();
		temp.add(text);
		writeFile(temp, filename);
	}
	
	public static void writeFile(Collection<String> text, String filename)
	{
		try(Writer out = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(filename), "UTF-8" ) ))
		{
			for(String line : text)
			{
				out.write(line);
			}
			out.flush();
		}
		catch(IOException ioe)
		{
			System.err.println("Problem with writing string to " + filename);
			ioe.printStackTrace();
		}		
	}
}