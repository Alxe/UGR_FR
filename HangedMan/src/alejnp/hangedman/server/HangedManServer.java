package alejnp.hangedman.server;

/**
 * Verbs:
 * <ul>
 * 	<li><code>STARTED {id}</code> - as a response to START</li>
 * 	<li><code>PLAYING {tries} {word}</code> - as a response to GUESS, with a on-going game</li>
 * 	<li><code>WIN {word}</code> - as a response to GUESS, with a finished, won game</li>
 * 	<li><code>LOSS {word}</code> - as a response to GUESS, with a finished, lost game</li>
 * 	<li><code>OVER {id}</code> - as a response to GUESS, with an already finished game</li>
 * </ul>
 * @author alejnp
 *
 */
public interface HangedManServer {
	public void start(int port);
}
