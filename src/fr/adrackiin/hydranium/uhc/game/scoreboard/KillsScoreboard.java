package fr.adrackiin.hydranium.uhc.game.scoreboard;

import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class KillsScoreboard {

    public KillsScoreboard(){
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        if(scoreboard.getObjective(Prefix.topKills) != null) {
            scoreboard.getObjective(Prefix.topKills).unregister();
        }
        scoreboard.registerNewObjective(Prefix.topKills, "dummy");
    }

    public void updateKill(UHCPlayer uhcPlayer){
        Bukkit.getScoreboardManager().getMainScoreboard().getObjective(Prefix.topKills).getScore(uhcPlayer.getAPPlayer().getName()).setScore(uhcPlayer.getKills());
    }

    public Score getPve() {
        return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(Prefix.topKills).getScore(Prefix.pve);
    }

    public Objective getObjective(){
        return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(Prefix.topKills);
    }
}
