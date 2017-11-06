package alejnp.hangedman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
			final HangedManGame game = new HangedManGame("Placeholder");
			
			// Introducimos la instancia en el mapa.
			games.put(id, game);
			
			// TODO Definir un sistema de Logging
			System.out.println("Started session with id " + id);
			
			try(PrintWriter out = new PrintWriter(new OutputStreamWriter(os))) {
				out.printf("STARTED %s %s\n", id, game.getMaskedWord());
			}
		}
		
		private void guess(OutputStream os, String[] input) {
			if(input.length != 2) {
				// TODO malformed request
			}
			
			final String id = input[0];
			final String guess = input[1];
			
			final HangedManGame game = games.get(id);
			
			if(game == null) {
				// TODO invalid game session
				
			} else try(PrintWriter out = new PrintWriter(new OutputStreamWriter(os))) {
				if(game.isGameOver()) {
					out.printf("OVER %s\n", id);

				} else {
					// TODO Maybe this explodes?
					HangedManGame.State state = game.doGuess(guess.charAt(0));
					
					if(state.equals(HangedManGame.State.WIN)) {
						out.printf("WIN %s\n", game.getWord());
					} else if(state.equals(HangedManGame.State.LOSS)) {
						out.printf("LOSS %s\n", game.getWord());
					} else { 
						// GameState is PLAYING!
						out.printf("PLAYING %d %s\n", game.getRemainingTries(), game.getMaskedWord());
					}
					
				}
			}
		}

		private void error(OutputStream os, int errorCode) {
			// TODO fill this method
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
					if(input.length != 3) error(connection.getOutputStream(), /* TODO replace errorCode */ 100);
					else guess(connection.getOutputStream(), Arrays.copyOfRange(input, 1, input.length));
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
				
				// Enviamos el socket de la conexión establecida al handler
				new Thread(new ConnectionHandler(connection)).start();
				
			} while(true);
		} catch (IOException e) {
			System.err.println("Error while listening at port=" + port);
		}
	}
}
