package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class NotchApple extends Configurable implements Listener {

    public NotchApple(){
        activate();
    }

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftNotchApple(PrepareItemCraftEvent e){
        if(e.getRecipe().getResult().getType() == Material.GOLDEN_APPLE && e.getRecipe().getResult().getDurability() == 1){
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onEatNotchApple(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1){
            e.setCancelled(true);
        }

    }

}
