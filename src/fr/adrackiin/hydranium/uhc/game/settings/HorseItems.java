package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;

import java.util.ArrayList;
import java.util.List;

public class HorseItems extends Configurable implements Listener {

    private final List<Material> remove = new ArrayList<>();
    private boolean active = false;

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        active = true;
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
        active = false;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void equipHorseArmor(InventoryClickEvent e) {
        if(e.getCurrentItem() == null){
            return;
        }
        if(e.getWhoClicked().getOpenInventory().getTopInventory() instanceof HorseInventory){
            if(e.getClick() == ClickType.NUMBER_KEY){
                if(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) == null){
                    return;
                }
                if(remove.contains(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()).getType())){
                    e.setCancelled(true);
                    return;
                }
            }
            if(remove.contains(e.getCurrentItem().getType())) {
                e.setCancelled(true);
            }
        }
    }

    public void setArmor(boolean armor){
        if(armor && remove.contains(Material.DIAMOND_BARDING)){
            remove.remove(Material.IRON_BARDING);
            remove.remove(Material.GOLD_BARDING);
            remove.remove(Material.DIAMOND_BARDING);
        }
        if(!armor && !remove.contains(Material.DIAMOND_BARDING)){
            remove.add(Material.IRON_BARDING);
            remove.add(Material.GOLD_BARDING);
            remove.add(Material.DIAMOND_BARDING);
        }
        if(!active && !remove.isEmpty()){
            activate();
        } else if(active && remove.isEmpty()){
            deactivate();
        }
    }

    public void setSaddle(boolean saddle){
        if(saddle){
            remove.remove(Material.SADDLE);
        }
        if(!saddle && !remove.contains(Material.SADDLE)){
            remove.add(Material.SADDLE);
        }
        if(remove.isEmpty()){
            deactivate();
        }
        if(!active && !remove.isEmpty()){
            activate();
        } else if(active && remove.isEmpty()){
            deactivate();
        }
    }
}
