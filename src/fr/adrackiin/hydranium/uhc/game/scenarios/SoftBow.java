package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Configurable;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoftBow extends Scenario implements Listener, Configurable {

    private int rechargingTime;

    private final Map<UUID, Boolean> canShoot = new HashMap<>();

    public SoftBow() {
        super(
                "SoftBow",
                "Les arcs sont limités à une flèche toutes les §610§7secondes",
                new String[]{"", "§7Les arcs sont limités à une flèche toutes", "§7les §610 §7secondes"}
        );

        this.rechargingTime = 10;
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

    @Override
    public void configure(APPlayer player) {
        try {
            APICore.getPlugin().getAPGuiManager().get("§cSoftBow").getAPGui().open(player);
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player shooter = (Player)e.getEntity();
        if(!canShoot.containsKey(shooter.getUniqueId())){
            canShoot.put(shooter.getUniqueId(), true);
        }
        if(canShoot.get(shooter.getUniqueId())){
            disallow(shooter);
        } else {
            shooter.sendMessage(Prefix.uhc + "§cVotre arc n'est pas encore rechargé");
            e.setCancelled(true);
        }
    }

    public int getRechargingTime() {
        return rechargingTime;
    }

    public void setRechargingTime(int rechargingTime) {
        this.rechargingTime = rechargingTime;
        this.setDescription("Les arcs sont limités à une flèche toutes les §6" + rechargingTime + "§7secondes");
        this.setDescriptionHost(new String[]{"", "§7Les arcs sont limités à une flèche toutes", "§7les §6" + rechargingTime + " §7secondes"});
    }

    private void allow(Player player){
        canShoot.replace(player.getUniqueId(), true);
    }

    private void disallow(Player player){
        canShoot.replace(player.getUniqueId(), false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> allow(player), rechargingTime * 20L);
    }
}
