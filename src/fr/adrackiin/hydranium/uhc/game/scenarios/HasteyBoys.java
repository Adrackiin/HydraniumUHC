package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HasteyBoys extends Scenario implements Listener {

    private final List<Material> toEnchant = new ArrayList<>();

    public HasteyBoys() {
        super(
                "HasteyBoys",
                "Tous les outils possèdent les enchantements Efficacité III et Incassable III",
                new String[]{"", "§7Tous les outils possèdent", "§7les enchantements Efficacité III", "§7et Incassable III"});

        toEnchant.addAll(Arrays.asList(Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE));
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



    @EventHandler
    public void onCraft(PrepareItemCraftEvent e){
        if(toEnchant.contains(e.getInventory().getResult().getType())){
            e.getInventory().getResult().addEnchantment(Enchantment.DIG_SPEED, 3);
            e.getInventory().getResult().addEnchantment(Enchantment.DURABILITY, 3);
        }
    }
}
