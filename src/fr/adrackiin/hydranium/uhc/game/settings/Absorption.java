package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;

public class Absorption extends Configurable implements Listener {

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEatGoldenApple(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.GOLDEN_APPLE){
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION));
        }
    }
}
