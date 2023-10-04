package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private final GameState gameState;
    private boolean cancelled;

    public GameStateChangeEvent(GameState gameState){
        this.gameState = gameState;
    }

    public GameState getNewGameState(){
        return this.gameState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
