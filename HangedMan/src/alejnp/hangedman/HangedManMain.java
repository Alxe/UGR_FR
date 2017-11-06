package alejnp.hangedman;

import java.util.Scanner;

import alejnp.hangedman.client.HangedManClient;
import alejnp.hangedman.client.HangedManResult;
import alejnp.hangedman.client.TcpClient;
import alejnp.hangedman.server.HangedManServer;
import alejnp.hangedman.server.TcpServer;

public class HangedManMain {

	public static void main(String... args) {
		if(args.length < 1) {
			System.out.println("Usage: HangedMan [cliente|servidor]");
			return;
		}
		
		String argument = args[0].toLowerCase();
		
		if(argument.equals("cliente")) {
			client(args);
		} else if(argument.equals("servidor")) {
			server(args);
		} else {
			System.out.printf("Usage:\n\t%s [cliente|servidor]\n", args[0]);
		}
	}

	public static void server(String... args) {
		final int port = (args.length >= 2) ? Integer.parseInt(args[2]) : 8989;
		
		HangedManServer server = new TcpServer();
		
		System.out.println("Arrancando servidor...");
		server.start(port);
	}
	
	public static void client(String... args) {
		final String host = (args.length >= 3) ? args[1] : "localhost";
		final int port = (args.length >= 3) ? Integer.parseInt(args[2]) : 8989;
				
		HangedManClient client = new TcpClient(host, port);
		
		System.out.println("Welcome to Hanged Man! Establishing connection...");
		
		HangedManResult result = client.start();
		HangedManGame.State gameState = result.gameState;
		String guessedWord = result.response;
		
		switch(gameState) {
		case STARTED:
			System.out.println("Connection established! Have fun!");
			
			try(final Scanner scanner = new Scanner(System.in)) {
				System.out.printf("Word: %s\n", guessedWord);
				
				while(!gameState.isTterminating) {
					System.out.printf("Guess (character): ");
					String input = scanner.nextLine();
					
					result = client.guess(input);
					gameState = result.gameState;
					
					// Just Display the response
					System.out.println(result.response);
				}
			}
			break;
		case ERROR:
		default:
			System.out.println("Error establishing connection! Exiting...");
			break;
		}
	}
}
