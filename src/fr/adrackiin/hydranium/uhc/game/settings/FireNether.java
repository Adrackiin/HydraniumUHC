package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class FireNether extends Configurable implements Listener{

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlaceFire(BlockPlaceEvent e){
        if(e.getBlock().getType() == Material.FIRE){
            if(e.getBlock().getWorld().getName().equals("uhc_nether")){
                if(e.getBlockAgainst().getType() != Material.OBSIDIAN) {
                    e.setCancelled(true);
                } else {
                    if(e.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
                        APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()).sendMessageBar(Prefix.uhc + "§7Si vous tentez d'activer un portail, veuillez allumer §cles blocs supérieurs");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDispenser(BlockDispenseEvent e){
        if(e.getItem() == null){
            return;
        }
        if(e.getBlock().getWorld() != Game.getGame().getWorldNether().getWorld()){
            return;
        }
        if(e.getItem().getType() == Material.FLINT_AND_STEEL || e.getItem().getType() == Material.FIREBALL){
            e.setCancelled(true);
        }
    }

}
