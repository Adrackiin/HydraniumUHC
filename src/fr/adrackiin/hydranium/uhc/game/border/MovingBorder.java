package fr.adrackiin.hydranium.uhc.game.border;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class MovingBorder implements Listener {

    private final Settings settings = Game.getGame().getSettings();

    private int size;
    private int time;
    private WorldBorder border;

    public void initBorderMove(){
        size = settings.getFinalSize() * 2;
        time = settings.getMeetup() * 60;
    }

    public void startBorder(){
        border = Game.getGame().getWorldUHC().getWorld().getWorldBorder();
        border.setSize(size, time);
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void stop(){
        HandlerList.unregisterAll(this);
    }

    public void reset(){
        stop();
        border.setSize(settings.getSize());
    }

    @EventHandler
    public void onTimer(SecondEvent e){
        borderMove();
    }

    private void borderMove(){
        if(time == 0){
            stop();
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Réduction de la bordure terminée");
        }
        size = (int) (border.getSize() / 2);
        Game.getGame().getScoreboardManager().setActualSize(size);
        time --;
    }
}
