package alejnp.hangedman;

import java.util.HashSet;
import java.util.Set;

public class HangedManGame {
	public static int DEFAULT_TRIES = 5;
	
	/**
	 * State of the game 
	 * @author alejnp
	 *
	 */
	public static enum State {
		/**
		 * Game just started!
		 */
		STARTED(false),
		/**
		 * Game goes on!
		 */
		PLAYING(false),
		/**
		 * Game has been won! 
		 */
		WIN(true),
		/**
		 * Game has been lost! 
		 */
		LOSS(true),
		/**
		 * Game is over, doesn't matter if won or lost. 
		 */
		OVER(true),
		/**
		 * It explooodes!
		 */
		ERROR(true);
		
		public final boolean isTterminating;
		
		private State(boolean isTerminating) {
			this.isTterminating = isTerminating;
		}
	}
	
	
	private final String objective;
	
	private final Set<Character> guessed = new HashSet<>();
	
	private int remainingTries;
	
	public HangedManGame(String objective, int maxTries) {
		if(objective == null || objective.isEmpty()) {
			throw new IllegalArgumentException("objective can't be null or empty");
		}
		
		if(maxTries <= 0) {
			throw new IllegalArgumentException("maxTries must be greater than zero");
		}
		
		this.objective = objective;
		this.remainingTries = maxTries;
	}
	
	public HangedManGame(String objective) {
		this(objective, DEFAULT_TRIES);
	}
	
	private boolean guess(Character c) {
		// false if `c` was already tried or `objective` doesn't contain `c`
		// true otherwise
		return guessed.add(Character.toLowerCase(c)) && objective.toLowerCase().contains(c.toString().toLowerCase());
	}
	
	/**
	 * Game main method
	 * @param c The character to be guessed
	 * @return
	 */
	public State doGuess(Character c) {
		// If game is already over, return said state.
		if(isGameOver()) {
			return State.OVER;
		}
		
		if(!guess(c)) {
			// if fail, remove a try.
			remainingTries -= 1;
		}
		
		// Is game already won?
		if(isGameWon()) {
			return State.WIN;
		}
		
		// Is game already lost?
		if(isGameLost()) {
			return State.LOSS;
		}
		
		// Then keep playing!
		return State.PLAYING;
	}
	
	/**
	 * 
	 * @return true if game has been won
	 */
	public boolean isGameWon() {
		return getGuessedWord().equals(objective);
	}
	
	/**
	 * 
	 * @return true if game has been lost
	 */
	public boolean isGameLost() {
		return remainingTries <= 0;
	}
	
	/**
	 * 
	 * @return true if game has either been won or lost, is over
	 */
	public boolean isGameOver() {
		return isGameWon() || isGameLost();
	}
	
	/**
	 * 
	 * @return Length of the objective
	 */
	public int getWordLength() {
		return objective.length();
	}
	
	/**
	 * 
	 * @return Masked string of the objective
	 */
	public String getGuessedWord() {
		StringBuilder sb = new StringBuilder();
		
		for(char c : objective.toCharArray()) {
			sb.append((guessed.contains(Character.toLowerCase(c)) ? c : '_'));
		}
		
		return sb.toString();
	}

	public int getRemainingTries() {
		return remainingTries;
	}
}
