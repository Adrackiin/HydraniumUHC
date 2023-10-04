package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFire extends Configurable implements Listener {

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHitTeam(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            if(Game.getGame().getUHCPlayer(event.getDamager().getUniqueId()).getGameTeam() == Game.getGame().getUHCPlayer((event.getEntity()).getUniqueId()).getGameTeam()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.START){
            if(Game.getGame().getSettings().isFriendlyFire() && !Game.getGame().isTeam()){
                deactivate();
            }
        }
    }
}
