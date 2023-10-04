package fr.adrackiin.hydranium.uhc.game.scoreboard;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.scoreboard.APScoreboard;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<APPlayer, APScoreboard> scoreboards = new HashMap<>();
    private int pvp;
    private int border;
    private int actualSize;
    private int meetup;
    private int reductionTime;
    private int reductionSize;
    private int finalTime;

    private String host;

    private String hostSc;
    private String playersSc;
    private String pvpSc;
    private String borderSc;
    private final String rulesSc;
    private final String helpSc;
    private String mapSc;
    private String reducSizeSc;
    private String reducTimeSc;
    private String meetupSc;
    private String finalTimeSc;

    private final String separator;

    public ScoreboardManager(){
        separator = "§8§m---------------";
        pvp = Game.getGame().getSettings().getPvp() * 60;
        border = Game.getGame().getSettings().getBorder() * 60;
        actualSize = Game.getGame().getSettings().getSize();
        meetup = Game.getGame().getSettings().getMeetup() * 60;
        hostSc = "§cAucun Host";
        playersSc = "§3Joueurs: §b";
        pvpSc = "§6PvP: §f";
        borderSc = "§6Bordure: §f";
        rulesSc = "§eRègles: /rules";
        helpSc = "§eAide: /help";
        mapSc = "§2Map: §a";
        finalTimeSc = "§6Temps: §f";
    }

    public void createScoreboardMain(UHCPlayer player){
        APScoreboard sc = new APScoreboard(player.getAPPlayer(), "§6Hydranium§cUHC");
        sc.create();
        scoreboards.put(player.getAPPlayer(), sc);
        setLines(sc, player);
    }

    public void showKills(){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            if(getScoreboard(player) != null){
                destroyAll();
                Game.getGame().getTopKills().getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        }
    }

    public void showUHC(){
        Bukkit.getScoreboardManager().getMainScoreboard().getObjective(Prefix.topKills).setDisplaySlot(null);
        for(Player player : Bukkit.getOnlinePlayers()){
            createScoreboardMain(Game.getGame().getUHCPlayer(player.getUniqueId()));
        }
    }

    public void updatePlayers(){
        playersSc = "§3Joueurs: §b" + Game.getGame().getAlivePlayers().getSize();
        setLine(9, playersSc);
    }

    public void setKills(){
        for(UHCPlayer player : Game.getGame().getUHCPlayers().getValues()){
            setKill(player);
        }
    }

    public void setTeamsKills(){
        for(UHCPlayer player : Game.getGame().getUHCPlayers().getValues()){
            setTeamKill(player);
        }
    }

    public void destroy(APPlayer player) {
        if(getScoreboard(player) != null) {
            getScoreboard(player).destroy();
            scoreboards.remove(player);
        }
    }

    public void destroyAll(){
        for(APPlayer player : APICore.getPlugin().getAPPlayers()){
            if(player.isConnected() && getScoreboard(player) != null) {
                getScoreboard(player).destroy();
            }
        }
        scoreboards.clear();
    }

    public void addPveScoreboard(){
        Game.getGame().getTopKills().getPve().setScore(Game.getGame().getPveDeath());
    }

    public void setHost(){
        host = (Game.getGame().getHost() == null ? "" : Bukkit.getOfflinePlayer(Game.getGame().getHost()).getName());
        updateHost();
    }

    public void remove(int line){
        for(APPlayer p : scoreboards.keySet()){
            remove(p, line);
        }
    }

    public void setLine(int line, String display){
        for(APPlayer p : scoreboards.keySet()){
            setLine(p, line, display);
        }
    }

    public Map<APPlayer, APScoreboard> getScoreboards() {
        return scoreboards;
    }

    public void setKill(UHCPlayer player){
        setLine(player.getAPPlayer(), 4, "§4Kills: §c" + player.getKills());
    }

    public void setTeamKill(UHCPlayer player){
        if(player.getGameTeam() != null){
            for(UUID uuid : player.getGameTeam().getAliveMembers().copy()){
                if(Bukkit.getPlayer(uuid) != null){
                    setLine(player.getAPPlayer(), 3, "§4Teams: §c" + player.getGameTeam().getKills());
                }
            }
        }
    }

    public void setTopKillScoreboard(UHCPlayer uhcPlayer){
        Game.getGame().getTopKills().updateKill(uhcPlayer);
    }

    public void setPvp(int pvp) {
        this.pvp = pvp;
        updatePvP();
    }

    public void setBorder(int border) {
        this.border = border;
        updateBorder();
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
        updateMap();
    }

    public void setMeetup(int meetup) {
        this.meetup = meetup;
        updateMeetup();
    }

    public void setReducTime(int reductionTime) {
        this.reductionTime = reductionTime;
        updateReducTime();
    }

    public void setReducSize(int reductionSize) {
        this.reductionSize = reductionSize;
        updateReducSize();
    }

    public void setFinalTime(int finalTime) {
        this.finalTime = finalTime;
        updateFinalTime();
    }

    private void setLines(APScoreboard sc, UHCPlayer player){
        sc.setLine(12, "§0" + separator);
        sc.setLine(10, "§1" + separator);
        sc.setLine(8, "§2" + separator);
        sc.setLine(5, "§3" + separator);
        sc.setLine(2, "§4" + separator);
        updateHost();
        updatePlayers();
        setKill(player);
        if(Game.getGame().isTeam()){
            if(player.getGameTeam() != null) {
                setTeamKill(player);
            }
        }
        if(Game.getGame().getGameStateManager().getGameState().getId() <= GameState.TELEPORATTION.getId()){
            updatePvP();
            updateBorder();
            sc.setLine(4, rulesSc);
            sc.setLine(3, helpSc);
        } else if(Game.getGame().getGameStateManager().getGameState().getId() <= GameState.MINING.getId()){
            updatePvP();
            updateBorder();
        } else if(Game.getGame().getGameStateManager().getGameState().getId() <= GameState.PVP.getId()){
            updateBorder();
        } else if(Game.getGame().getGameStateManager().getGameState().getId() <= GameState.BORDER.getId()){
            if(Game.getGame().getSettings().getBorderType() == UHCBorder.Type.BORDERMOVE){
               updateMeetup();
            } else {
                updateReducTime();
                updateReducSize();
            }
        } else if(Game.getGame().getGameStateManager().getGameState().getId() <= GameState.MEETUP.getId()){
            updateFinalTime();
        }
        updateMap();
    }

    private void updateHost(){
        hostSc = "§b" + host;
        setLine(11, hostSc);
    }

    private void updatePvP(){
        pvpSc = "§6PvP: §f" + (this.pvp / 60 < 10 ? "0" : "") + this.pvp / 60 + ":" + (this.pvp % 60 < 10 ? "0" : "") + this.pvp % 60;
        setLine(7, pvpSc);
    }

    private void updateBorder(){
        borderSc = "§6Bordure: §f" + (this.border / 60 < 10 ? "0" : "") + this.border / 60 + ":" + (this.border % 60 < 10 ? "0" : "") + this.border % 60;
        setLine(6, borderSc);
    }

    private void updateMap(){
        mapSc = "§2Map: §a±" + actualSize;
        setLine(1, mapSc);
    }

    private void updateReducSize(){
        reducSizeSc = "§6Prochaine Réduc': §f±" + reductionSize;
        setLine(6, reducSizeSc);
    }

    private void updateReducTime(){
        reducTimeSc = "§6Prochaine Réduc': §f" + (this.reductionTime / 60 < 10 ? "0" : "") + this.reductionTime / 60 + ":" + (this.reductionTime % 60 < 10 ? "0" : "") + this.reductionTime % 60;
        setLine(7, reducTimeSc);
    }

    private void updateMeetup(){
        meetupSc = "§6Meetup: §f" + (this.meetup / 60 < 10 ? "0" : "") + this.meetup / 60 + ":" + (this.meetup % 60 < 10 ? "0" : "") + this.meetup % 60;
        setLine(6, meetupSc);
    }

    private void updateFinalTime(){
        finalTimeSc = "§6Temps: §f" + (finalTime / 60 < 10 ? "0" : "") + finalTime / 60 + ":" + (finalTime % 60 < 10 ? "0" : "") + finalTime % 60;
        setLine(6, finalTimeSc);
    }

    private void remove(APPlayer player, int line){
        if(getScoreboard(player) != null){
            getScoreboard(player).removeLine(line);
        }
    }

    private void setLine(APPlayer player, int line, String display){
        if(getScoreboard(player) != null) {
            getScoreboard(player).setLine(line, display);
        }
    }

    private APScoreboard getScoreboard(APPlayer player){
        return scoreboards.get(player);
    }
}
