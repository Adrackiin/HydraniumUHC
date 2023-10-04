package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class FlowerPower extends Scenario implements Listener {

    private final ArrayList<Material> items = new ArrayList<>();
    private final Random random = new Random();

    public FlowerPower(){
        super("FlowerPower",
                "Vous obtenez un item aléatoire lorsque vous cassez une fleur",
                new String[]{"", "§7Vous obtenez un item", "§7aléatoire lorsque vous", "§7cassez une fleur"});
        HashSet<Material> removeItems = new HashSet<>();
        removeItems.add(Material.AIR);
        removeItems.add(Material.BLAZE_ROD);
        removeItems.add(Material.BREWING_STAND_ITEM);
        removeItems.add(Material.BREWING_STAND);
        removeItems.add(Material.SKULL);
        removeItems.add(Material.CARROT);
        removeItems.add(Material.POTATO);
        removeItems.add(Material.ACACIA_DOOR);
        removeItems.add(Material.BIRCH_DOOR);
        removeItems.add(Material.DARK_OAK_DOOR);
        removeItems.add(Material.JUNGLE_DOOR);
        removeItems.add(Material.SPRUCE_DOOR);
        removeItems.add(Material.CAULDRON);
        removeItems.add(Material.FLOWER_POT);
        removeItems.add(Material.WOODEN_DOOR);
        removeItems.add(Material.IRON_DOOR_BLOCK);
        removeItems.add(Material.SNOW);
        removeItems.add(Material.SUGAR_CANE_BLOCK);
        removeItems.add(Material.BED_BLOCK);
        removeItems.add(Material.CAKE_BLOCK);
        removeItems.add(Material.POTION);
        removeItems.add(Material.ENCHANTED_BOOK);
        removeItems.add(Material.MONSTER_EGG);
        removeItems.add(Material.TRIPWIRE);
        removeItems.add(Material.REDSTONE_WIRE);
        removeItems.add(Material.STANDING_BANNER);
        removeItems.add(Material.WALL_BANNER);
        removeItems.add(Material.WALL_SIGN);
        removeItems.add(Material.SIGN_POST);
        removeItems.add(Material.MELON_STEM);
        removeItems.add(Material.PUMPKIN_STEM);
        removeItems.add(Material.WOOD_DOUBLE_STEP);
        removeItems.add(Material.CROPS);
        removeItems.add(Material.WATER);
        removeItems.add(Material.STATIONARY_WATER);
        removeItems.add(Material.LAVA);
        removeItems.add(Material.STATIONARY_LAVA);
        removeItems.add(Material.REDSTONE_TORCH_OFF);
        removeItems.add(Material.DIODE_BLOCK_ON);
        removeItems.add(Material.DIODE_BLOCK_OFF);
        removeItems.add(Material.REDSTONE_LAMP_ON);
        removeItems.add(Material.REDSTONE_COMPARATOR_ON);
        removeItems.add(Material.REDSTONE_COMPARATOR_OFF);
        removeItems.add(Material.DOUBLE_STEP);
        removeItems.add(Material.DOUBLE_STONE_SLAB2);
        removeItems.add(Material.GLOWING_REDSTONE_ORE);
        removeItems.add(Material.SOIL);
        removeItems.add(Material.BURNING_FURNACE);
        removeItems.add(Material.DAYLIGHT_DETECTOR_INVERTED);
        removeItems.add(Material.PORTAL);
        removeItems.add(Material.ENDER_PORTAL);
        removeItems.add(Material.COCOA);
        removeItems.add(Material.PISTON_EXTENSION);
        removeItems.add(Material.PISTON_MOVING_PIECE);
        removeItems.add(Material.NETHER_WARTS);
        removeItems.add(Material.FIRE);
        for(Material m : Material.values()){
            if(!removeItems.contains(m)){
                items.add(m);
            }
        }
    }

    @Override
    public void setScenario(boolean status){
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void disable(){
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.YELLOW_FLOWER || e.getBlock().getType() == Material.RED_ROSE){
            Location loc = e.getBlock().getLocation().add(0.5D, 0.5D, 0.5D);
            e.getBlock().setType(Material.AIR);
            loc.getWorld().dropItem(loc, new ItemStack(new ItemStack(items.get(this.random.nextInt(items.size())))));
            e.setCancelled(true);
        }
    }

}
