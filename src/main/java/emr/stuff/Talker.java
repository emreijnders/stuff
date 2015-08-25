package emr.stuff;

public interface Talker
{
	public void addListener( Listener listener );
	public void removeListener( Listener listener );
	public void removeAllListeners();
	public void sendMessage( Message message );
}