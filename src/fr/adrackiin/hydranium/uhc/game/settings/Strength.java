package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength extends Configurable implements Listener {

    public Strength(){
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
    public void onStrengthHit(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        if(((Player)event.getDamager()).hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            for (PotionEffect effect : ((Player)event.getDamager()).getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    int level = effect.getAmplifier() + 1;
                    double percent = event.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.3D + 1.0D) * (1.0D + Game.getGame().getSettings().getStrength() * level / 100.0D) / event.getDamage(EntityDamageEvent.DamageModifier.BASE);
                    event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage(EntityDamageEvent.DamageModifier.BASE) * percent);
                    event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * percent);
                    event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * percent);
                    event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * percent);
                    event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * percent);
                    UHCCore.getPlugin().logServer(event.getEntity() + " " + event.getFinalDamage() + " " + event.getDamage());
                }
            }
        }
    }
}
