package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class NoEnchant extends Scenario implements Listener {

    public NoEnchant(){
        super("NoEnchant",
                "La table d'enchantement est désactivé",
                new String[]{"", "§7La table d'enchantement", "§7est désactivé"});
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

    /**
     * When Event is triggered, cancel it
     *
     * @param e Event of enchant item
     */
    @EventHandler
    public void onEnchantItem(PrepareItemEnchantEvent e){
        e.setCancelled(true);
    }

    /**
     * When Event is triggered, cancel it
     *
     * @param e Event of enchant item
     */
    @EventHandler
    public void onEnchantItem(EnchantItemEvent e){
        e.setCancelled(true);
    }

    /**
     * When Event is triggered, cancel it
     *
     * @param e Event of craft enchantment table
     */
    @EventHandler
    public void onCraftEnchantTable(PrepareItemCraftEvent e){
        if(e.getRecipe().getResult().getType() == Material.ENCHANTMENT_TABLE){
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

}
