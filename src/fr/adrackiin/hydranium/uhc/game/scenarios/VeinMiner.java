package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerBreakBlockEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VeinMiner extends Scenario implements Listener {

    private final List<Material> vein = Arrays.asList(Material.DIAMOND_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.EMERALD_ORE, Material.QUARTZ_ORE, Material.LAPIS_ORE);

    public VeinMiner() {
        super(
                "VeinMiner",
                "Les veines de minerais se cassent instantanément",
                new String[]{"", "§7Les veines de minerais se cassent instantanément"}
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
    public void onOre(UHCPlayerBreakBlockEvent e) {
        if (e.getBlock().hasMetadata("VeinMiner")) {
            return;
        }
        if (vein.contains(e.getBlock().getType())) {
            List<Block> vein = Blocks.getVein(e.getBlock(), (e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE ? Arrays.asList(Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE) : Collections.singletonList(e.getBlock().getType())));
            vein.remove(e.getBlock());
            for (Block block : vein) {
                block.setMetadata("VeinMiner", new FixedMetadataValue(UHCCore.getPlugin(), block.getType()));
            }
            new Break(e.getPlayer().getAPPlayer(), vein, e.getBlock().getType());
        }
    }

    private class Break implements Runnable {

        private final int id;
        private final APPlayer player;
        private final List<Block> vein = new ArrayList<>();

        private Break(APPlayer player, List<Block> vein, Material type){
            this.player = player;
            this.vein.addAll(vein);
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 2L, 2L);
        }

        @Override
        public void run() {
            if(!vein.isEmpty()) {
                breakBlock(vein.get(0));
                vein.remove(0);
            } else {
                stop();
            }
        }

        private void breakBlock(Block block){
            if(block.getType() != Material.AIR) {
                Blocks.breakParticule(block);
                if(Game.getGame().getUHCManager().getUHCBlock(Game.getGame().getUHCManager().getUHCBlock(block.getType(), block.getData())).getDrops(player.getItemInHand()).contains(Items.simpleItem(HBlock.Type.AIR))){
                    player.sendMessageBar("§c§lVeuillez gardez votre pioche en main");
                }
                ((CraftPlayer) player.getPlayer()).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                player.getLocation().getWorld().playSound(block.getLocation(), Sound.DIG_STONE, 1F, 0.8F);
            }
            if(block.hasMetadata("VeinMiner")){
                block.removeMetadata("VeinMiner", UHCCore.getPlugin());
            }
        }

        private void stop(){
            Bukkit.getScheduler().cancelTask(id);
        }
    }



}