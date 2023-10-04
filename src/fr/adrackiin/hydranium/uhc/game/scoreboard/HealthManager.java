package fr.adrackiin.hydranium.uhc.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class HealthManager {

    public HealthManager(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if(scoreboard.getObjective("health") != null){
            scoreboard.getObjective("health").unregister();
        }
        scoreboard.registerNewObjective("health", "health");
        scoreboard.getObjective("health").setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

}
