package emr.stuff.json;

public interface JSONElement
{	
	public String getJSON( int depth );
	public String getJSON();
	@Override
	public boolean equals( Object other );
	@Override
	public int hashCode();
}