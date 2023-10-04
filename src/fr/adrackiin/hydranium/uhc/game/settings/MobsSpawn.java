package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class MobsSpawn extends Configurable implements Listener {

    private final List<EntityType> entityForbidden = new ArrayList<>();

    public MobsSpawn(){
        entityForbidden.add(EntityType.SQUID);
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
    public void onMobSpawn(CreatureSpawnEvent e){
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM){
            return;
        }
        if(entityForbidden.contains(e.getEntityType())){
            e.setCancelled(true);
        }
    }

    public boolean isMobForbid(EntityType e){
        return entityForbidden.contains(e);
    }

    public void setMob(EntityType entityType){
        if(entityForbidden.contains(entityType)){
            entityForbidden.remove(entityType);
        } else {
            entityForbidden.add(entityType);
        }
    }

}
