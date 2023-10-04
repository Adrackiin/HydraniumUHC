package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerReviveEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SuperHeroes extends Scenario implements Listener {

    private final Map<UUID, SuperPowers> superPowers = new HashMap<>();

    public SuperHeroes() {
        super(
                "SuperHeroes",
                "Tous les joueurs recevront un pouvoir aléatoire. Pouvoirs: Vitesse, Résistance, Force, Double Vie, Lapin",
                new String[]{"", "§7Tous les joueurs recevront un pouvoir aléatoire", "§7Pouvoirs: Vitesse, Résistance, Force, Double Vie, Lapin"}
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



    @EventHandler
    public void onStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.INVULNERABILITY){
            start();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player) {
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if(superPowers.get(e.getEntity().getUniqueId()) == SuperPowers.JUMPBOOST) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRegen(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.GOLDEN_APPLE){
            if(superPowers.get(e.getPlayer().getUniqueId()) == SuperPowers.HEALTH){
                Player player = e.getPlayer();
                if(e.getItem().getItemMeta().getDisplayName() != null) {
                    if(e.getItem().getItemMeta().getDisplayName().equals("§6" + "Golden Head")) {
                        player.removePotionEffect(PotionEffectType.ABSORPTION);
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, 1));
                        return;
                    }
                }
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,  75, 1));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFood(FoodLevelChangeEvent e){
        if(superPowers.get(e.getEntity().getUniqueId()) == SuperPowers.STRENGTH){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRevive(UHCPlayerReviveEvent e){
        if(superPowers.get(e.getPlayer().getAPPlayer().getUUID()) == SuperPowers.HEALTH){
            apply(e.getPlayer().getAPPlayer().getUUID(), SuperPowers.HEALTH, false);
        }
    }

    private void start(){
        SuperPowers sp;
        List<SuperPowers> sps = new ArrayList<>(Arrays.asList(SuperPowers.values()));
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            sp = sps.get(UHCCore.getRandom().nextInt(sps.size()));
            superPowers.put(uuid, sp);
            apply(uuid, sp, true);
        }
    }

    private void apply(UUID uuid, SuperPowers sp, boolean announce){
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        if(player.isConnected()){
            if(announce){
                player.sendMessage(Prefix.uhc + "§7Vous possédez le pouvoir §c" + sp.getName());
                player.sendMessage(Prefix.uhc + sp.getDescription());
            }
            switch(sp){
                case SPEED:
                    player.addPotionEffect(PotionEffectType.SPEED, 1_000_000, 1, false);
                    player.addPotionEffect(PotionEffectType.FAST_DIGGING, 1_000_000, 0, false);
                    break;
                case HEALTH:
                    player.setMaxHealth(40.0D);
                    player.setHealth(40.0D);
                    break;
                case STRENGTH:
                    player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 1_000_000, 0, false);
                    break;
                case RESISTANCE:
                    player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1_000_000, 1, false);
                    break;
                case JUMPBOOST:
                    player.addPotionEffect(PotionEffectType.JUMP, 1_000_000, 3, false);
                    player.addPotionEffect(PotionEffectType.SPEED, 1_000_000, 0, false);
                    break;
            }
        }
    }

    private enum SuperPowers {

        RESISTANCE("Résistance", "§7Vous bénéficiez de l'effet §cRésistance II"),
        STRENGTH("Force", "§7Vous bénéficiez de l'effet §cForce §7. Vous n'avez pas besoin de manger"),
        SPEED("Vitesse", "§7Vous bénéficiez de l'effet §cVitesse II §7ainsi que §cVitesse de minage"),
        HEALTH("Double Vie", "§7Vous bénéficiez de §c2x plus de vie§7. Les §cpommes dorées §7vous régénèreront de §c3 coeurs et les §cGolden Head §7de §c6 coeurs"),
        JUMPBOOST("Lapin", "§7Vous bénéficiez de l'effet §cJump Boost§7, §cNoFall §7et §cVitesse");

        private final String name;
        private final String description;

        SuperPowers(String name, String description){
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

}
