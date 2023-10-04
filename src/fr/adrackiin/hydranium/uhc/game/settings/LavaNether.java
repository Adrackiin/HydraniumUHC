package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class LavaNether extends Configurable implements Listener{

    private final BlockFace[] blockFaces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlaceLava(PlayerBucketEmptyEvent e){
        if(e.getBucket() == Material.LAVA_BUCKET){
            if(e.getPlayer().getWorld().getName().equals("uhc_nether")){
                if(e.getBlockClicked().getType() == Material.OBSIDIAN){
                    if(e.getBlockClicked().getRelative(e.getBlockFace()).getType() == Material.PORTAL){
                        if(e.getPlayer().getItemInHand().getType() == Material.BUCKET){
                            e.getPlayer().getItemInHand().setType(Material.LAVA_BUCKET);
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                            e.getBlockClicked().getRelative(e.getBlockFace()).setType(Material.AIR);
                        });
                        return;
                    }
                }
                e.setCancelled(true);
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
        if(e.getItem().getType() == Material.LAVA_BUCKET){
            e.setCancelled(true);
        }
    }

}
