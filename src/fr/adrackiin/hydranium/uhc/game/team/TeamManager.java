package fr.adrackiin.hydranium.uhc.game.team;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.api.utils.APTreeSet;
import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.commons.Rank;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class TeamManager {

    private final APTreeSet<GameTeam> teams = new APTreeSet<>();
    private final HashSet<GameTeam> aliveTeams = new HashSet<>();
    private final APHash<Integer, GameTeam> gameTeams = new APHash<>();

    /**
     * Impair = normal
     * Pair = Invisible
     */
    public TeamManager(){
        for(int i = 0; i <= 60; i += 15){
            this.addGameTeam(new GameTeam(i + 1, "§4", "dark_red", "Rouge foncé"));
            this.addGameTeam(new GameTeam(i + 3, "§c", "red", "Rouge"));
            this.addGameTeam(new GameTeam(i + 5, "§6", "gold", "Orange"));
            this.addGameTeam(new GameTeam(i + 7, "§e", "yellow", "Jaune"));
            this.addGameTeam(new GameTeam(i + 9, "§a", "green", "Vert"));
            this.addGameTeam(new GameTeam(i + 11, "§2", "dark_green", "Vert foncé"));
            this.addGameTeam(new GameTeam(i + 13, "§1", "dark_blue", "Bleu doncé"));
            this.addGameTeam(new GameTeam(i + 15, "§9", "blue", "Bleu"));
            this.addGameTeam(new GameTeam(i + 17, "§3", "dark_aqua", "Turquoise"));
            this.addGameTeam(new GameTeam(i + 19, "§b", "aqua", "Bleu clair"));
            this.addGameTeam(new GameTeam(i + 21, "§d", "light_purple", "Rose"));
            this.addGameTeam(new GameTeam(i + 23, "§5", "dark_purple", "Violet"));
            this.addGameTeam(new GameTeam(i + 25, "§f", "white", "Blanc"));
            this.addGameTeam(new GameTeam(i + 27, "§7", "gray", "Gris"));
            this.addGameTeam(new GameTeam(i + 29, "§8", "dark_gray", "Gris foncé"));
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if(scoreboard.getTeam("uhczdead") != null) {
            scoreboard.getTeam("uhczdead").unregister();
        }
        scoreboard.registerNewTeam("uhczdead").setPrefix("§0");
        if(scoreboard.getTeam("uhcffa") != null) {
            scoreboard.getTeam("uhcffa").unregister();
        }
    }

    public void fillTeams(){
        GameTeam team;
        UHCPlayer player;
        for(UUID uuid : Game.getGame().getAlivePlayers().copy()){
            player = Game.getGame().getUHCPlayer(uuid);
            if(player != null) {
                if (!player.hasTeam()) {
                    team = getFirstTeam();
                    if (team == null) {
                        try {
                            APICore.getPlugin().getChannelManager().getChannel("channel.host").sendMessage("§cPlus aucune équipe disponible, merci de prévenir un développeur !");
                        } catch(ChannelNotFoundException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    team.addPlayer(uuid, false);
                }
            }
        }
    }

    public GameTeam getFirstTeamColor(String color){
        for(GameTeam gameTeam : getTeams().copy()){
            if(gameTeam.isFree() && !gameTeam.isForbid() && color.equalsIgnoreCase(gameTeam.getColor())){
                return gameTeam;
            }
        }
        return null;
    }

    public void setAliveTeam() {
        for(GameTeam gameTeam : getTeams().copy()){
            if(!gameTeam.isFree() && !gameTeam.isForbid()){
                getAliveTeams().add(gameTeam);
                for(UUID uuid : gameTeam.getMembers().copy()){
                    gameTeam.getAliveMembers().add(uuid);
                }
            }
        }
    }

    public void addFFA(UHCPlayer player){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Rank rank = player.getAPPlayer().getRank();
                Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
                    if(Game.getGame().hasGameStart() || rank == Rank.PLAYER){
                        if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + player.getId()) == null){
                            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("uhcffa" + player.getId()).setPrefix("§f");
                        }
                        player.getAPPlayer().setTeam(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhcffa" + player.getId()));
                    } else {
                        player.getAPPlayer().setTeam(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(rank.toString().toLowerCase()));
                    }
                });
            } catch(CanTakeTimeException ignored){}
        });

    }

    public GameTeam getTeam(int id){
        return this.gameTeams.get(id);
    }

    public void addGameTeam(GameTeam team){
        this.gameTeams.put(team.getId(), team);
        this.teams.add(team);
    }

    public GameTeam getFirstTeam(){
        for(GameTeam gameTeam : getTeams().copy()){
            if(gameTeam.isFree() && !gameTeam.isForbid()){
                return gameTeam;
            }
        }
        return null;
    }

    public int getNumberOfTeam(){
        int i = 0;
        for(GameTeam gameTeam : getTeams().copy()){
            if(!gameTeam.isFree() && !gameTeam.isForbid()){
                i ++;
            }
        }
        return i;
    }

    public APTreeSet<GameTeam> getTeams() {
        return teams;
    }

    public HashSet<GameTeam> getAliveTeams() {
        return aliveTeams;
    }

    public void setFriendlyFire(boolean b){
        if(!Game.getGame().isTeam()){
            return;
        }
        for (GameTeam team : getTeams().copy()) {
            team.getTeam().setAllowFriendlyFire(b);
        }
    }

    public void setAlive(UHCPlayer player) {
        if(!player.hasTeam()){
            this.setAliveFFA(player);
        } else {
            this.setAliveTeam(player);
        }
    }

    public void setDead(UHCPlayer player){
        this.setDeadReconnect(player);
        if(player.hasTeam()) {
            player.getGameTeam().getAliveMembers().remove(player.getAPPlayer().getUUID());
            if(player.getGameTeam().getAliveMembers().isEmpty()) {
                Bukkit.broadcastMessage(Prefix.uhc + "§7L'équipe " + player.getGameTeam().getPrefix() + player.getGameTeam().getName() + "§7 a été §céliminé");
                Game.getGame().getTeamManager().getAliveTeams().remove(player.getGameTeam());
            }
        }
    }

    public void setDeadReconnect(UHCPlayer player){
        player.getAPPlayer().setTeam(Bukkit.getScoreboardManager().getMainScoreboard().getTeam("uhczdead"));
    }

    private void setAliveFFA(UHCPlayer player){
        addFFA(player);
    }

    private void setAliveTeam(UHCPlayer player){
        player.getAPPlayer().setTeam(player.getGameTeam().getTeam());
    }
}