package alejnp.hangedman.client;

import java.util.Objects;

import alejnp.hangedman.HangedManGame;

public class HangedManResult {
	public static class Builder {
		private HangedManGame.State gameState;
		private String response;
		
		public Builder(HangedManGame.State gameState, String response) {
			this.gameState = gameState;
			this.response = response;
		}
		
		public Builder withGameState(HangedManGame.State gameState) {
			this.gameState = gameState; return this;
		}
		
		public Builder withResponse(String response) {
			this.response = response; return this;
		}
		
		public HangedManResult build() {
			return new HangedManResult(gameState, response);
		}
	}
	
	public final HangedManGame.State gameState;
	public final String response;
	
	public HangedManResult(HangedManGame.State gameState, String response) {
		Objects.requireNonNull(gameState);
		Objects.requireNonNull(response);
		
		this.gameState = gameState;
		this.response = response;
	}
}
