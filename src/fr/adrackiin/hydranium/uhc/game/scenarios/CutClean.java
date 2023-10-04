package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.Drops;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class CutClean extends Scenario implements Listener {

    private final ItemStack gold = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack iron = new ItemStack(Material.IRON_INGOT);

    public CutClean() {
        super(
                "CutClean",
                "Les minerais et la nourriture sont directement cuits",
                new String[]{"", "§7Les minerais et la nourriture", "§7sont directement cuits"});

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
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.IRON_ORE).setDrops(new Drops(new ItemStack[]{Items.simpleItem(Material.IRON_INGOT)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.IRON_INGOT)}, new short[][][]{{{10000, 1000}, {10000, 2000}, {10000, 4000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.IRON_ORE)}, new short[][]{{10000}}, false));
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.GOLD_ORE).setDrops(new Drops(new ItemStack[]{Items.simpleItem(Material.GOLD_INGOT)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(Material.GOLD_INGOT)}, new short[][][]{{{10000, 1000}, {10000, 2000}, {10000, 4000}}}, new ItemStack[]{Items.simpleItem(HBlock.Type.GOLD_ORE)}, new short[][]{{10000}}, false));
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.IRON_ORE).setDrops(new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.IRON_ORE)}));
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.GOLD_ORE).setDrops(new Drops(new ItemStack[]{Items.simpleItem(HBlock.Type.GOLD_ORE)}));
    }

    @EventHandler
    public void onKillAnimal(EntityDeathEvent e){
        if(e.getEntity() instanceof Cow){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.RAW_BEEF){
                    item.setType(Material.COOKED_BEEF);
                }
            }
        } else if(e.getEntity() instanceof Pig){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.PORK){
                    item.setType(Material.GRILLED_PORK);
                }
            }
        } else if(e.getEntity() instanceof Chicken){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.RAW_CHICKEN){
                    item.setType(Material.COOKED_CHICKEN);
                }
            }
        } else if(e.getEntity() instanceof Sheep){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.MUTTON){
                    item.setType(Material.COOKED_MUTTON);
                }
            }
        } else if(e.getEntity() instanceof Rabbit){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.RABBIT){
                    item.setType(Material.COOKED_RABBIT);
                }
            }
        } else if(e.getEntity() instanceof MushroomCow){
            for(ItemStack item : e.getDrops()){
                if(item.getType() == Material.RAW_BEEF){
                    item.setType(Material.COOKED_BEEF);
                }
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e){
        for(Block b : e.blockList()){
            switch(b.getType()){
                case IRON_ORE:
                    b.setType(Material.AIR);
                    if(UHCCore.getRandom().nextDouble() < e.getYield()){
                        b.getLocation().getWorld().dropItemNaturally(b.getLocation(), iron);
                    }
                    break;
                case GOLD_ORE:
                    b.setType(Material.AIR);
                    if(UHCCore.getRandom().nextDouble() < e.getYield()){
                        b.getLocation().getWorld().dropItemNaturally(b.getLocation(), gold);
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void onElockExplode(EntityExplodeEvent e){
        for(Block b : e.blockList()){
            switch(b.getType()){
                case IRON_ORE:
                    b.setType(Material.AIR);
                    if(UHCCore.getRandom().nextDouble() < e.getYield()){
                        b.getLocation().getWorld().dropItemNaturally(b.getLocation(), iron);
                    }
                    break;
                case GOLD_ORE:
                    b.setType(Material.AIR);
                    if(UHCCore.getRandom().nextDouble() < e.getYield()){
                        b.getLocation().getWorld().dropItemNaturally(b.getLocation(), gold);
                    }
                    break;
            }
        }
    }
}
