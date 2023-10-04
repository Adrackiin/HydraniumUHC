package fr.adrackiin.hydranium.uhc.game.team;

import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameTeamJoinedEvent;
import fr.adrackiin.hydranium.uhc.events.GameTeamLeftEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.UUID;

public class GameTeam implements Comparable<GameTeam> {

    private final int id;
    private final HashSet<UUID> members = new HashSet<>();
    private final HashSet<UUID> aliveMembers = new HashSet<>();
    private final boolean forbid;
    private UUID leader;
    private String prefix;
    private String name;
    private Team team;
    private String color;
    private boolean free;
    private int kills = 0;
    private final Team invisible;

    public GameTeam(int id, String colorTeam, String color, String name) {
        this.id = id;
        Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();
        if (main.getTeam("uhc" + id) != null) {
            main.getTeam("uhc" + id).unregister();
        }
        this.team = main.registerNewTeam("uhc" + id);
        this.team.setAllowFriendlyFire(true);
        if(id > 120 ){
            this.prefix = colorTeam + "§o" + "§n";
        } else if(id > 90){
            this.prefix = colorTeam + "§o";
        } else if(id > 60){
            this.prefix = colorTeam + "§n";
        } else if(id > 30){
            this.prefix = colorTeam + "§l";
        } else {
            this.prefix = colorTeam;
        }
        this.team.setPrefix(this.prefix);
        this.color = color;
        this.free = true;
        this.forbid = false;
        this.name = name;
        if (main.getTeam("uhc" + (id + 1)) != null) {
            main.getTeam("uhc" + (id + 1)).unregister();
        }
        this.invisible = main.registerNewTeam("uhc" + (id + 1));
        this.invisible.setNameTagVisibility(NameTagVisibility.NEVER);
        this.invisible.setPrefix(this.prefix);
        this.invisible.setAllowFriendlyFire(true);
        this.invisible.setCanSeeFriendlyInvisibles(true);
    }

    public void addPlayer(UUID uuid, boolean announce){
        UHCPlayer player = Game.getGame().getUHCPlayer(uuid);
        if(player == null){
            return;
        }
        if(getMembers().getSize() >= Game.getGame().getSettings().getTeam()){
            player.getAPPlayer().sendMessage(Prefix.uhc + "§cCette équipe est complète");
            return;
        }
        this.getMembers().add(uuid);
        player.setGameTeam(this);
        Game.getGame().getTeamManager().setAlive(player);
        if(this.getLeader() == null){
            this.setFree(false);
            this.setLeader(uuid);
            UHCCore.getPlugin().getServer().getPluginManager().callEvent(new GameTeamJoinedEvent(this));
            if(announce) {
                player.getAPPlayer().sendMessage(Prefix.uhc + "§7Vous venez de créer votre équipe");
                player.getAPPlayer().sendMessage(Prefix.uhc + "§cVous êtes le capitaine de l'équipe. /team invite <joueur> pour inviter un joueur");
            }
        } else {
            if(announce) {
                player.getAPPlayer().sendMessage(Prefix.uhc + "§7Vous venez de rejoindre une équipe");
            }
        }
    }

    public void removePlayer(UUID uuid, boolean announce){
        UHCPlayer player = Game.getGame().getUHCPlayer(uuid);
        if(player == null){
            return;
        }
        if(announce) {
            player.getAPPlayer().sendMessage(Prefix.uhc + "§7" + "Vous venez de quitter votre Team");
        }
        this.getMembers().remove(uuid);
        player.setGameTeam(null);
        if(this.getLeader() == uuid){
            if(!this.getMembers().isEmpty()){
                this.setLeader(this.getMembers().first());
                if(announce) {
                    if(Game.getGame().getUHCPlayer(this.getLeader()) != null) {
                        Game.getGame().getUHCPlayer(this.getLeader()).getAPPlayer().sendMessage(Prefix.uhc + "§7Vous êtes le nouveau capitaine de l'équipe");
                    }
                }
            }
        }
        if(this.getMembers().isEmpty()){
            this.setFree(true);
            this.setLeader(null);
            UHCCore.getPlugin().getServer().getPluginManager().callEvent(new GameTeamLeftEvent(this));

        }
        Game.getGame().getTeamManager().setAlive(player);
    }

    public void sendMessageTeam(String message) {
        UHCPlayer player;
        if(Game.getGame().hasGameStart()) {
            for (UUID uuid : this.getAliveMembers().copy()) {
                player = Game.getGame().getUHCPlayer(uuid);
                if (player != null) {
                    player.getAPPlayer().sendMessage(Prefix.team + message);
                }
            }
        } else {
            for (UUID uuid : this.getMembers().copy()) {
                player = Game.getGame().getUHCPlayer(uuid);
                if (player != null) {
                    player.getAPPlayer().sendMessage(Prefix.team + message);
                }
            }
        }
    }

    public void addKills(int amount){
        this.kills += amount;
    }

    public boolean willEmpty(UUID uuid){
        return members.getSize() == 1 && members.first() == uuid;
    }

    public HashSet<UUID> getMembers() {
        return members;
    }

    public HashSet<UUID> getAliveMembers() {
        return aliveMembers;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isForbid() {
        return forbid;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getId(){
        return id;
    }

    public Team getInvisible(){
        return invisible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        GameTeam gameTeam = (GameTeam) o;
        return id == gameTeam.id;
    }

    @Override
    public int compareTo(GameTeam o) {
        return Integer.compare(this.id, o.id);
    }

}
