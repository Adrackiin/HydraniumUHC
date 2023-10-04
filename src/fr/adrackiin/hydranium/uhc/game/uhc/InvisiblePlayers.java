package fr.adrackiin.hydranium.uhc.game.uhc;

import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class InvisiblePlayers implements Listener{

    private final HashSet<UUID> invisiblePlayers = new HashSet<>();

    public InvisiblePlayers(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDrinkInvisibilityPotion(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.POTION){
            if(e.getItem().getData().getData() == (byte)46 || e.getItem().getData().getData() == (byte)78) {
                UHCPlayer player = Game.getGame().getUHCPlayer(e.getPlayer().getUniqueId());
                invisible(player);
                for(PotionEffect potionEffect : Potion.fromItemStack(e.getItem()).getEffects()){
                    if(potionEffect.getType().getName().equals(PotionEffectType.INVISIBILITY.getName())){
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), () -> {
                            boolean isInvisible = false;
                            for(PotionEffect check : player.getAPPlayer().getActivePotionEffects()){
                                if(check.getType().getName().equals(PotionEffectType.INVISIBILITY.getName())){
                                    isInvisible = true;
                                }
                            }
                            if(!isInvisible){
                                if (invisiblePlayers.contains(player.getAPPlayer().getUUID())) {
                                    visible(player);
                                }
                            }

                        }, potionEffect.getDuration() + 1L);
                        break;
                    }
                }
            }
        } else if(e.getItem().getType() == Material.MILK_BUCKET){
            if(invisiblePlayers.contains(e.getPlayer().getUniqueId())){
                visible(Game.getGame().getUHCPlayer(e.getPlayer().getUniqueId()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSplashInvisibilityPotion(PotionSplashEvent event){
        for(PotionEffect potionEffect : event.getPotion().getEffects()){
            if(potionEffect.getType().getName().equals(PotionEffectType.INVISIBILITY.getName())){
                for(Entity entity : event.getAffectedEntities()){
                    if(entity instanceof Player){
                        UHCPlayer player = Game.getGame().getUHCPlayer(entity.getUniqueId());
                        invisible(player);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), () -> {
                            for(PotionEffect potionEffect1 : player.getAPPlayer().getActivePotionEffects()) {
                                if(potionEffect.getType().getName().equals(PotionEffectType.INVISIBILITY.getName())) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                                        boolean isInvisible = false;
                                        for(PotionEffect check : player.getAPPlayer().getActivePotionEffects()){
                                            if(check.getType().getName().equals(PotionEffectType.INVISIBILITY.getName())){
                                                isInvisible = true;
                                            }
                                        }
                                        if(!isInvisible){
                                            if (invisiblePlayers.contains(player.getAPPlayer().getUUID())) {
                                                visible(player);
                                            }
                                        }
                                    }, potionEffect1.getDuration() + 1L);
                                }
                            }
                        });
                    }
                }
                break;
            }
        }
    }

    private void invisible(UHCPlayer player){
        player.setInvisible();
        invisiblePlayers.add(player.getAPPlayer().getUUID());
    }

    private void visible(UHCPlayer player){
        player.setVisible();
        invisiblePlayers.remove(player.getAPPlayer().getUUID());
    }

}
