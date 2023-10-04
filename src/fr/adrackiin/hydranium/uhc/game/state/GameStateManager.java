package fr.adrackiin.hydranium.uhc.game.state;

import fr.adrackiin.hydranium.uhc.utils.PubSub;

public class GameStateManager {

    private GameState gameState;

    public GameStateManager(){
        gameState = GameState.WAITING_HOST;
    }

    public boolean isGameState(GameState gameState){
        return this.gameState == gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        PubSub.changeGameStatus();
    }

}
