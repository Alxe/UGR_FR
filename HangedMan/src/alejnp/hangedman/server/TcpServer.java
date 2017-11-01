package alejnp.hangedman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import alejnp.hangedman.HangedManGame;

/**
 * TCP implementation of {@link HangedManServer}
 * @author alejnp
 * @see HangedManServer 
 *
 */
public class TcpServer implements HangedManServer {
	/**
	 * Mapping of Session ID to Game instances.
	 */
	private final Map<String, HangedManGame> games = new HashMap<>();
	
	private class ConnectionHandler implements Runnable {
		private final Socket connection;
		
		public ConnectionHandler(Socket connection) {
			this.connection = connection;
		}
		
		private void start(OutputStream os) {
			final String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			final HangedManGame game = new HangedManGame("MotoBanana", HangedManGame.DEFAULT_TRIES);
			
			games.put(id, game);
			
			try(PrintWriter out = new PrintWriter(new OutputStreamWriter(os))) {
				out.printf("STARTED %s %s\n", id, game.getGuessedWord());
				out.flush();
			}
		}
		
		private void guess(OutputStream os, String input) {
			// TODO This WILL eventually explode!!
			final String id = input.split(" ")[0];
			final String guess = input.split(" ")[1];
			
			// TODO This WILL eventually explode too!!
			HangedManGame game = games.get(id);
			
			try(PrintWriter out = new PrintWriter(new OutputStreamWriter(os))) {
				if(game.isGameOver()) {
					// printf("Game (id=%s) is over", id);
					out.printf("OVER %s\n", id);

				} else {
					// TODO Maybe this explodes?
					HangedManGame.State state = game.doGuess(guess.charAt(0));
					
					if(state.equals(HangedManGame.State.WIN)) {
						// printf("Congratulations, you've won! The word was %s", game.getGuessedWord());
						out.printf("WIN %s\n", game.getGuessedWord());
					} else if(state.equals(HangedManGame.State.LOSS)) {
						// printf("That's too bad! You've lost. You only guessed %s", game.getGuessedWord());
						out.printf("LOSS %s\n", game.getGuessedWord());
					} else { 
						// GameState is PLAYING!
						// printf("Word is: %s (%d remaining tries)", game.getGuessedWord(), game.getRemainingTries());
						out.printf("PLAYING %s %s\n", game.getGuessedWord(), game.getRemainingTries());
					}
					
				}
			}
		}
		
		public void run() {
			try(Socket connection = this.connection) {
				final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				final String[] input = in.readLine().split(" ");
				
				switch(input[0]) {
				case "START":
					start(connection.getOutputStream());
					break;
				case "GUESS":
					guess(connection.getOutputStream(), String.join(" ", input[1], input[2]));
					break;
				default:
					// TODO wrongInput(connection.getOutputStream());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void start(int port) {
		try(ServerSocket serverSocket = new ServerSocket(port)) {
			do {
				// Esperamos una nueva conexión
				final Socket connection = serverSocket.accept();
				
				// Enviamos el socket de la conexión establecida al handler.
				new ConnectionHandler(connection).run();
				
			} while(true);
		} catch (IOException e) {
			System.err.println("Error while listening at port=" + port);
		}
	}
}
