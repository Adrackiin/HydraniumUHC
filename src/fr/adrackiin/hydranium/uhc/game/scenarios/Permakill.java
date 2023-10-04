package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Permakill extends Scenario implements Listener {

    public Permakill(){
        super("PermaKill",
                "Change le temps à chaque kill",
                new String[]{"", "§7Change le temps", "§7à chaque kill"});
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
     * When Event is triggered, if it's the day, set the night, otherwise, set the day
     * You can choose when a day starts and end. 0 = start of a minecraft day, 12000 = start of minecraft night
     * You can choose the time you want to set, whith setDay and setNight. 6000 = midday, 18000 = midnight
     *
     * @param e KillEvent or DeathEvent : When a player kills another or a player die
     */
    @EventHandler
    public void onKill(UHCPlayerDeathEvent e){
        World world = Game.getGame().getWorldUHC().getWorld();
        int startDay = 0, endDay = 12000, setDay = 6000, setNight = 18000;
        if(world.getTime() >= startDay && world.getTime() < endDay){
            world.setTime((long)setNight);
        } else {
            world.setTime((long)setDay);
        }
    }
}
