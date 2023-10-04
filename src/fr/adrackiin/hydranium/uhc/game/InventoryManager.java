package fr.adrackiin.hydranium.uhc.game;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.utils.enumeration.ItemsAllowed;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;

public class InventoryManager implements Listener{

    public InventoryManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryCreative(InventoryCreativeEvent e){
        if(!(e.getWhoClicked() instanceof Player)){
            return;
        }
        if(APICore.getPlugin().getAPPlayer(e.getWhoClicked().getUniqueId()).hasPermission("hydranium.uhc.bypass")){
            return;
        }
        boolean isAllowed = false;
        Material typeItemClicked = e.getCursor().getType();
        for(Material allowed : ItemsAllowed.itemsAllowed){
            if(typeItemClicked == allowed){
                isAllowed = true;
                break;
            }
        }
        if(!isAllowed){
            e.setCancelled(true);
        }
    }

}
