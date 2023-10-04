package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class EventsManager implements Listener {

    private boolean isDay = false;
    private boolean isNight = false;

    public EventsManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), ()-> UHCCore.getPlugin().getServer().getPluginManager().callEvent(new SecondEvent()), 0L, 20L);
    }

    @EventHandler
    public void checkDay(SecondEvent e){
        if(!isDay) {
            if (Game.getGame().getWorldUHC().getWorld().getTime() > 0 && Game.getGame().getWorldUHC().getWorld().getTime() < 12_000) {
                UHCCore.getPlugin().getServer().getPluginManager().callEvent(new DayEvent());
                isDay = true;
                isNight = false;
            }
        }
    }

    @EventHandler
    public void checkNight(SecondEvent e){
        if(!isNight) {
            if (Game.getGame().getWorldUHC().getWorld().getTime() > 12_000 && Game.getGame().getWorldUHC().getWorld().getTime() < 24_000) {
                UHCCore.getPlugin().getServer().getPluginManager().callEvent(new NightEvent());
                isNight = true;
                isDay = false;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.SKULL || e.getBlock().getType() == Material.WALL_BANNER || e.getBlock().getType() == Material.STANDING_BANNER){
            return;
        }
        e.setCancelled(true);
        if(Game.getGame().getUHCManager().getUHCBlock(e.getBlock().getType(), e.getBlock().getData()) == null){
            UHCCore.getPlugin().logServer("=============== BLOCK MANQUANT ===============\nMaterial: " + e.getBlock().getType() + ", Data: " + e.getBlock().getData());
            e.setCancelled(false);
            return;
        }
        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerBreakBlockEvent(Game.getGame().getUHCPlayer(e.getPlayer().getUniqueId()), e.getBlock(), true, Game.getGame().getUHCManager().getUHCBlock(Game.getGame().getUHCManager().getUHCBlock(e.getBlock().getType(), e.getBlock().getData())).getDrops(), e.getExpToDrop()));
    }

}
