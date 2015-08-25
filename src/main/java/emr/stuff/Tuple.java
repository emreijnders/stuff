package emr.stuff;

public class Tuple
{
	private Object first;
	private Object second;
	
	public <A,B> Tuple( A first , B second )
	{
		this.first = first;
		this.second = second;
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T getFirst()
	{
		return (T) first;
	}
	
	@SuppressWarnings( "unchecked" )
	public <T> T getSecond()
	{
		return (T) second;
	}
}