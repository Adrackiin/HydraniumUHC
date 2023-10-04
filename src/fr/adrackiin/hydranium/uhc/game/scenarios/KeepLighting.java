package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class KeepLighting extends Scenario implements Runnable, Listener {

    public KeepLighting() {
        super(
                "KeepLighting",
                "Vous prenez des dégâts dans les ténèbres",
                new String[]{"", "§7Vous prenez des dégâts", "§7dans les ténèbres"});

    }

    @Override
    public void setScenario(boolean status) {
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        Player player;
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            player = Bukkit.getPlayer(uuid);
            if(player.getLocation().getBlock().getLightLevel() < 6){
                if(player.getHealth() <= 1){
                    UHCPlayer dead = Game.getGame().getUHCPlayer(player.getUniqueId());
                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerDeathEvent(dead, Game.getGame().getUHCPlayer(dead.getLastDamager()), null,false, EntityDamageEvent.DamageCause.CUSTOM, UHCPlayer.DeathCause.DARKNESS, true, player.getInventory().getContents(), player.getInventory().getArmorContents(), true));
                    return;
                }
                player.damage(1);
            }
        }
    }

    @EventHandler
    public void onGameStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.MINING){
            start();
        }
    }

    private void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 0L, 2 * 20L);
    }
}