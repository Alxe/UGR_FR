package alejnp.hangedman.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import alejnp.hangedman.HangedManGame;

public class TcpClient extends HangedManClient {
	
	public TcpClient(String host, int port) {
		super(host, port);
	}
	
	private String gameId;
	
	@Override
	public HangedManResult start() {
		try(Socket socket = new Socket(super.host, super.port)) {
			
			try {
				// WRITE
				final PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				out.println("START");
				out.flush();
				
				// READ
				final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				final String[] input = in.readLine().split(" ");
				switch(input[0]) {
				case "STARTED":
					// input[1] = gameId; input[2] = guessedWord
					gameId = input[1];
					
					return new HangedManResult(HangedManGame.State.STARTED, input[2]);
				default:
					return new HangedManResult(HangedManGame.State.ERROR, "Could not start game");
				}
			} catch(IOException e) {
				return new HangedManResult(HangedManGame.State.ERROR,
						"Error while reading/writing");
			}
		} catch (IOException e) {
			return new HangedManResult(HangedManGame.State.ERROR,
					"Error while connecting");
		}
	}

	@Override
	public HangedManResult guess(String guess) {
		if(gameId == null || gameId.isEmpty()) {
			throw new IllegalStateException("Game never started");
		}
		
		try(Socket socket = new Socket(super.host, super.port)) {
			try {
				// WRITE
				final PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				out.printf("GUESS %s %s\n", gameId, guess.charAt(0));
				out.flush();
				
				// READ
				final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				final String[] input = in.readLine().split(" ");
				switch(input[0]) {
				case "OVER":
					return new HangedManResult(HangedManGame.State.OVER,
							String.format("Game (id=%s) is over\n", gameId));
				case "WIN":
					// input[1] = guessedWord
					return new HangedManResult(HangedManGame.State.WIN, 
							String.format("Congratulations, you've won! The word was %s\n", input[1]));
				case "LOSS":
					// input[1] = guessedWord
					return new HangedManResult(HangedManGame.State.LOSS, 
							String.format("That's too bad! You've lost. You only guessed %s\n", input[1]));
				case "PLAYING":
					// input[1] = guessedWord; input[2] = remainingTries
					return new HangedManResult(HangedManGame.State.PLAYING, 
							String.format("Word is: %s (%s remaining tries)\n", input[1], input[2]));
				case "ERROR":
				default:
					return new HangedManResult(HangedManGame.State.ERROR,
							String.format("Invalid response from server!! %s", input[1]));
				}
			} catch(IOException e) {
				return new HangedManResult(HangedManGame.State.ERROR,
						"Error while reading/writing");
			}
		} catch (IOException e) {
			return new HangedManResult(HangedManGame.State.ERROR,
					"Error while connecting");
		}
	}
}
