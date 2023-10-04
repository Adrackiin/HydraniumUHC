package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerBreakBlockEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timber extends Scenario implements Listener {

    public Timber() {
        super(
                "Timber",
                "Les arbres se cassent instantanément",
                new String[]{"", "§7Les arbres se cassent instantanément"}
        );
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



    @EventHandler(priority = EventPriority.LOW)
    public void onBreakTree(UHCPlayerBreakBlockEvent e){
        if(e.getBlock().hasMetadata("Timber") || e.getBlock().hasMetadata("BlockPlacePlayer")){
            return;
        }
        if(e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) {
            List<Block> vein = Blocks.getVein(e.getBlock(), Collections.singletonList(e.getBlock().getType()), "BlockPlacePlayer");
            vein.remove(e.getBlock());
            for(Block blockVein : vein){
                blockVein.setMetadata("Timber", new FixedMetadataValue(UHCCore.getPlugin(), e.getPlayer().getAPPlayer().getName()));
            }
            new Break(e.getPlayer().getAPPlayer().getPlayer(), vein);
        }
    }

    private class Break implements Runnable {

        private final int id;
        private final Player player;
        private final List<Block> vein = new ArrayList<>();

        private Break(Player player, List<Block> vein){
            this.player = player;
            this.vein.addAll(vein);
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 2L, 2L);
        }

        @Override
        public void run() {
            if(!vein.isEmpty()) {
                breakBlock(vein.get(0), player);
                vein.remove(0);
            } else {
                stop();
            }
        }

        private void breakBlock(Block block, Player player){
            if(block.getType() != Material.AIR) {
                //Blocks.breakParticule(block);
                ((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                player.getLocation().getWorld().playSound(block.getLocation(), Sound.DIG_WOOD, 1F, 0.8F);
            }
            if(block.hasMetadata("Timber")){
                block.removeMetadata("Timber", UHCCore.getPlugin());
            }
        }

        private void stop(){
            Bukkit.getScheduler().cancelTask(id);
        }
    }

}
