package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameTeamLeftEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final GameTeam team;

    public GameTeamLeftEvent(GameTeam team){
        this.team = team;
    }

    public GameTeam getTeam(){
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

}
