package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SkyHigh extends Scenario implements Listener {

    private int activation;
    private int doDamage;
    private int minLayer;
    private float hearth;

    private boolean activated = false;
    private int id;

    public SkyHigh() {
        super(
                "GoneFishin'",
                "Au bout de §640 §7minutes, vous prendrez §64§7 coeurs en dessous de la couche §6200§7 toutes les §630§7secondes",
                new String[]{"", "§7Au bout de §640 §7minutes", "§7vous prendrez §64§7 coeurs", "§7en dessous de la couche §6200", "§7toutes les §630§7secondes"}
        );

        activation = 40;
        doDamage = 30;
        minLayer = 200;
        hearth = 4;
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
    public void onFinish(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.VICTORY){
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSecond(SecondEvent e){
        if(activated){
            return;
        }
        if(Game.getGame().getTimer().getTimer() >= activation - 330){
            if(Game.getGame().getTimer().getTimer() % 60 == 0 && Game.getGame().getTimer().getTimer() < activation - 30){
                Bukkit.broadcastMessage(Prefix.uhc + "§7Activation du §cSkyHigh §7dans §c" + (activation - Game.getGame().getTimer().getTimer() / 60) + " §7" + (activation - Game.getGame().getTimer().getTimer() / 60 == 1 ? "minute" : "minutes"));
            }
            if(Game.getGame().getTimer().getTimer() >= activation - 90){
                if(Game.getGame().getTimer().getTimer() == activation - 30 ||
                        Game.getGame().getTimer().getTimer() == activation - 15 ||
                        Game.getGame().getTimer().getTimer() == activation - 10 ||
                        Game.getGame().getTimer().getTimer() >= activation - 5){
                    if(Game.getGame().getTimer().getTimer() == activation){
                        activated = true;
                        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this::run, doDamage * 20L, doDamage * 20L);
                    } else {
                        Bukkit.broadcastMessage(Prefix.uhc + "§7Activation du SkyHigh dans §c" + (activation - Game.getGame().getTimer().getTimer()));
                    }
                }
            }
        }
    }

    public int getActivation() {
        return activation;
    }

    public void setActivation(int activation) {
        this.activation = activation;
        this.setDescription("Au bout de §6" + activation + " §7minutes, vous prendrez §6" + hearth + "§7 coeurs en dessous de la couche §6" + minLayer + "§7 toutes les §6" + doDamage + "§7secondes");
        this.setDescriptionHost(new String[]{"", "§7Au bout de §6" + activation + " §7minutes", "§7vous prendrez §6" + hearth + "§7 coeurs", "§7en dessous de la couche §6" + minLayer, "§7toutes les §6" + doDamage + "§7secondes"});

    }

    public int getDoDamage() {
        return doDamage;
    }

    public void setDoDamage(int doDamage) {
        this.doDamage = doDamage;
        this.setDescription("Au bout de §6" + activation + " §7minutes, vous prendrez §6" + hearth + "§7 coeurs en dessous de la couche §6" + minLayer + "§7 toutes les §6" + doDamage + "§7secondes");
        this.setDescriptionHost(new String[]{"", "§7Au bout de §6" + activation + " §7minutes", "§7vous prendrez §6" + hearth + "§7 coeurs", "§7en dessous de la couche §6" + minLayer, "§7toutes les §6" + doDamage + "§7secondes"});

    }

    public int getMinLayer() {
        return minLayer;
    }

    public void setMinLayer(int minLayer) {
        this.minLayer = minLayer;
        this.setDescription("Au bout de §6" + activation + " §7minutes, vous prendrez §6" + hearth + "§7 coeurs en dessous de la couche §6" + minLayer + "§7 toutes les §6" + doDamage + "§7secondes");
        this.setDescriptionHost(new String[]{"", "§7Au bout de §6" + activation + " §7minutes", "§7vous prendrez §6" + hearth + "§7 coeurs", "§7en dessous de la couche §6" + minLayer, "§7toutes les §6" + doDamage + "§7secondes"});

    }

    public float getHearth() {
        return hearth;
    }

    public void setHearth(float hearth) {
        this.hearth = hearth;
        this.setDescription("Au bout de §6" + activation + " §7minutes, vous prendrez §6" + hearth + "§7 coeurs en dessous de la couche §6" + minLayer + "§7 toutes les §6" + doDamage + "§7secondes");
        this.setDescriptionHost(new String[]{"", "§7Au bout de §6" + activation + " §7minutes", "§7vous prendrez §6" + hearth + "§7 coeurs", "§7en dessous de la couche §6" + minLayer, "§7toutes les §6" + doDamage + "§7secondes"});

    }

    private void run(){
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            if(Bukkit.getPlayer(uuid) != null){
                Player p = Bukkit.getPlayer(uuid);
                if(p.getLocation().getY() <= minLayer){
                    p.damage(hearth * 2);
                    p.sendMessage(Prefix.uhc + "§7Vous êtes en dessous de la couche §c" + minLayer + "§7, vous avez pris des dégâts ! §cRemontez §7pour être en sécurité !");
                }
            }
        }
    }
}
