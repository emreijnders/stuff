package emr.stuff.json;

public interface JSONValue extends JSONElement
{
	public <T> T getValue();
	@Override
	public String toString();
}