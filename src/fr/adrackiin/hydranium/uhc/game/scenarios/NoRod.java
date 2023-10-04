package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NoRod extends Scenario implements Listener {

    public NoRod() {
        super(
                "NoRod",
                "Les cannes à pêche ne peuvent pas être utilisé pour PvP (Snowballs compris)",
                new String[]{"", "§7Les cannes à pêche ne peuvent", "§7pas être utilisé pour PvP", "§7(Snowballs compris)"}
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
    public void onPvPRod(EntityDamageByEntityEvent e){
        if((e.getDamager() instanceof FishHook || e.getDamager() instanceof Snowball) && e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }

}
