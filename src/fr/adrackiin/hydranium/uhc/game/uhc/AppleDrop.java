package fr.adrackiin.hydranium.uhc.game.uhc;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

public class AppleDrop implements Listener {

    public AppleDrop(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeaveDecay(LeavesDecayEvent e){
        for(ItemStack toDrop : Game.getGame().getUHCManager().getUHCBlock(Game.getGame().getUHCManager().getUHCBlock(e.getBlock().getType(), e.getBlock().getData())).getDrops(Items.simpleItem(Material.AIR))){
            if(toDrop.getType() != Material.AIR) {
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5F, 0.5F, 0.5F), toDrop);
            }
        }
        e.setCancelled(true);
        e.getBlock().setType(Material.AIR);
    }

    public void setAllTrees() {
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.ACACIA_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (Game.getGame().getSettings().getAppleRate() * 100) : 0)});
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.BIRCH_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (Game.getGame().getSettings().getAppleRate() * 100) : 0)});
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.JUNGLE_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (Game.getGame().getSettings().getAppleRate() * 100) : 0)});
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.SPRUCE_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (Game.getGame().getSettings().getAppleRate() * 100) : 0)});

    }

    public void setAppleRate(float appleRate) {
        if(Game.getGame().getSettings().isAllTrees()) {
            Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.ACACIA_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
            Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.BIRCH_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
            Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.JUNGLE_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
            Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.SPRUCE_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
        }
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.DARK_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.OAK_LEAVES).getDrops().changeAmount(Material.APPLE, new short[]{(short) (Game.getGame().getSettings().isAllTrees() ? (appleRate * 100) : 0)});
    }

}
