package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class DayNightCycle implements Listener {

    public DayNightCycle(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler
    public void onStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.INVULNERABILITY){
            if(Game.getGame().getSettings().isDayNightCycle()){
                Game.getGame().getWorldUHC().getWorld().setGameRuleValue("doDaylightCycle", "true");
                Game.getGame().getWorldUHC().getWorld().setTime(0);
            } else {
                Game.getGame().getWorldUHC().getWorld().setGameRuleValue("doDaylightCycle", "false");
                Game.getGame().getWorldUHC().getWorld().setTime(6000L);
            }
            HandlerList.unregisterAll(this);
        }
    }
}
