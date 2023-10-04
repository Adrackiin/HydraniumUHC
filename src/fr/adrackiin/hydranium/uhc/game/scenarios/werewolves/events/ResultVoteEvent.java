package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events;

import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WWUHCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ResultVoteEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final WWUHCPlayer voted;
    private boolean cancelled;

    public ResultVoteEvent(WWUHCPlayer voted){
        this.voted = voted;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public WWUHCPlayer getVoted() {
        return voted;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
