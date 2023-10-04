package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Scenario for StrafeUHC
 *
 * Scenario: KillEffect
 * Description: You get a random effect on a kill
 *
 * You can remove any effect by adding it to the set in constructor
 * When a kill is performed, give an effect among 'effects' list
 *
 * @author Adrackiin
 */
public class KillEffect extends Scenario implements Listener {

    private final ArrayList<PotionEffectType> effects = new ArrayList<>();
    private final Random random = new Random();

    public KillEffect(){
        super("KillEffect",
                "Obtenez un effet aléatoire lors d'un kill",
                new String[]{"", "§7Obtenez un effet aléatoire", "§7lors d'un kill"});
        HashSet<PotionEffectType> removeEffects = new HashSet<>();
        removeEffects.add(PotionEffectType.ABSORPTION);
        removeEffects.add(PotionEffectType.REGENERATION);
        removeEffects.add(PotionEffectType.HARM);
        removeEffects.add(PotionEffectType.HEAL);
        removeEffects.add(PotionEffectType.HEALTH_BOOST);
        removeEffects.add(PotionEffectType.WITHER);
        removeEffects.add(PotionEffectType.SATURATION);
        for(PotionEffectType p : PotionEffectType.values()){
            if(!removeEffects.contains(p)){
                effects.add(p);
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

    /**
     * When Event is triggered, give a random effect to the player
     *
     * @param e KillEvent
     */
    @EventHandler
    public void onKill(UHCPlayerDeathEvent e){
        if(e.getKiller() == null){
            return;
        }
        e.getKiller().getAPPlayer().addPotionEffect(effects.get(random.nextInt(effects.size())), 180 * 20, 0, true);
    }
}
