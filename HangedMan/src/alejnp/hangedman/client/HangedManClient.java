package alejnp.hangedman.client;

public abstract class HangedManClient {
	protected final String host;
	protected final int port;
	
	public HangedManClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public abstract HangedManResult start();
	
	public abstract HangedManResult guess(String guess);
}
