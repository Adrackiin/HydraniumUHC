package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.DayEvent;
import fr.adrackiin.hydranium.uhc.events.NightEvent;
import fr.adrackiin.hydranium.uhc.events.SecondEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.events.DiscoverEvent;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TimeManager implements Listener{

    public TimeManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDay(DayEvent e){
        Bukkit.broadcastMessage(Prefix.wereWolves + "§7" + "Le jour se lève !");
        if(Game.getGame().getTimer().getTimer() < 60){
            return;
        }
        WereWolves.getWereWolves().getVoteManager().startVote();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNight(NightEvent e){
        Bukkit.broadcastMessage(Prefix.wereWolves + "§7" + "La nuit se lève ! Les loups sortent de leurs tanières");
    }

    public void changeTime(){
        Game.getGame().getWorldUHC().getWorld().setTime(Game.getGame().getWorldUHC().getWorld().getTime() + 1);
    }

    public void startChangeTime(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this::changeTime, 0L, 1L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTimer(SecondEvent e){
        if(!WereWolves.getWereWolves().isDiscovered()) {
            if (Game.getGame().getTimer().getTimer() >= WereWolves.getWereWolves().getDiscover()) {
                UHCCore.getPlugin().getServer().getPluginManager().callEvent(new DiscoverEvent());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDiscover(DiscoverEvent e){
        WereWolves.getWereWolves().setDiscovered(true);
        WereWolves.getWereWolves().distributeRoles();
        WereWolves.getWereWolves().getRoleManager().onDiscover(e);
    }

}
