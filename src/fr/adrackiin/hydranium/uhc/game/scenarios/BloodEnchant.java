package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.UUID;

public class BloodEnchant extends Scenario implements Listener {

    private final ArrayList<UUID> willDie = new ArrayList<>();

    public BloodEnchant(){
        super("BloodEnchant",
                "Vous perdez un demi-coeur à chaque enchantement",
                new String[]{"", "§7Vous perdez un demi-coeur", "§7à chaque enchantement"});
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
    public void onEnchantItem(EnchantItemEvent e){
        Player player = e.getEnchanter();
        if(player.getHealth() <= 1){
            willDie.add(player.getUniqueId());
        }
        player.damage(1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
            willDie.remove(player.getUniqueId());
        }, 2L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e){
        if(willDie.contains(e.getEntity().getUniqueId())){
            e.setDeathMessage(e.getEntity().getName() + " has enchanted too much");
        }
    }
}
