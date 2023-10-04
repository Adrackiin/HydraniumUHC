package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerReviveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UHCPlayer player;

    public UHCPlayerReviveEvent(UHCPlayer player){
        this.player = player;
    }

    public UHCPlayer getPlayer(){
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
