package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerDeathEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.commands.CommandLG;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.RoleManager;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.votes.VoteManager;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Players;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WereWolves extends Scenario implements Listener {

    private static WereWolves wereWolves;

    private String name;
    private String description;
    private String[] descriptionHost;
    private boolean configurable;

    private int discover;

    private VoteManager voteManager;
    private TimeManager timeManager;
    private RoleManager roleManager;

    private final Map<UUID, WWUHCPlayer> wwuhcPlayers = new HashMap<>();
    private final ArrayList<Roles> roles = new ArrayList<>();
    private boolean discovered = false;

    public WereWolves() {
        super(
                "Loups-Garous",
                "Adaptation en UHC du jeu Loups-Garous de Thiercelieux",
                new String[]{"", "§7Les minerais et la nourriture", "§7sont directement cuits"}
        );

        this.discover = 10;
        wereWolves = this;
    }

    @Override
    public void setScenario(boolean status) {

    }

    @Override
    public void enable() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        Game.getGame().getScenariosManager().forceFFA();
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGameStart(GameStateChangeEvent e){
        if(e.getNewGameState() == GameState.INVULNERABILITY){
            voteManager = new VoteManager();
            timeManager = new TimeManager();
            roleManager = new RoleManager();
            new CommandLG();
            timeManager.startChangeTime();
            for(UUID uuid : Game.getGame().getWhitelistedPlayers().copy()){
                WWUHCPlayer wwPlayer = new WWUHCPlayer(uuid);
                wwuhcPlayers.put(uuid, wwPlayer);
            }
            roles.add(Roles.CHAMAN);
            roles.add(Roles.WILD_CHILDREN);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(UHCPlayerDeathEvent e){
        if(e.isCancellable()){
            e.setCancelled(true);
        } else {
            return;
        }
        WWUHCPlayer wwPlayer = WereWolves.getWereWolves().getWWPlayer(e.getDeath().getAPPlayer().getUUID());
        wwPlayer.setAlive(false);
        String deathMessage = Prefix.kill + "§7" + "! Un Villageois est mort ! Il s'agit de "
                + "§c" + wwPlayer.getName() + "§7" + " qui était " + "§b" + wwPlayer.getRole().getName();
        if(roles.contains(Roles.FATHER_WOLF) && roles.contains(Roles.WITCH)){
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                if(!wwPlayer.isAlive()) {
                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerDeathEvent(e.getDeath(), e.getKiller(), null, e.isOffline(), e.getDamageCause(), e.getDeathCause(), e.isKeepInventory(), e.getLoots(), e.getArmorLoots(), false));
                }
            }, 20 * 20L);
        } else if(roles.contains(Roles.FATHER_WOLF) || roles.contains(Roles.WITCH)){
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                if(!wwPlayer.isAlive()) {
                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerDeathEvent(e.getDeath(), e.getKiller(), null, e.isOffline(), e.getDamageCause(),e.getDeathCause(), e.isKeepInventory(), e.getLoots(), e.getArmorLoots(), false));
                }
            }, 10 * 20L);
        } else {
            UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerDeathEvent(e.getDeath(), e.getKiller(), null, e.isOffline(), e.getDamageCause(), e.getDeathCause(), e.isKeepInventory(), e.getLoots(), e.getArmorLoots(), false));
            return;
        }
        wwPlayer.getUHCPlayer().getAPPlayer().setGameMode(GameMode.ADVENTURE);
        wwPlayer.getUHCPlayer().getAPPlayer().teleport(Game.getGame().getWorldUHC().getSpawn());
    }

    public void distributeRoles(){
        /*if(true){
            getWWPlayer(Bukkit.getPlayer("Adrackiin").getUniqueId()).setRole(Roles.CHAMAN);
            getWWPlayer(Bukkit.getPlayer("DeuxFelinsIci").getUniqueId()).setRole(Roles.WILD_CHILDREN);
            return;
        }*/
        ArrayList<UUID> players = new ArrayList<>(Game.getGame().getAlivePlayers().copy());
        ArrayList<Roles> roles = new ArrayList<>(this.roles);
        int nbWolves = 0 + 1;
        if(isRoleActivated(Roles.FATHER_WOLF)){
            nbWolves --;
        }
        if(isRoleActivated(Roles.WHITE_WOLF)){
            nbWolves --;
        }
        for(int i = 0; i < nbWolves; i ++){
            int r = UHCCore.getRandom().nextInt(players.size());
            this.getWWPlayer(players.get(r)).setRole(Roles.WOLF);
            players.remove(r);
        }
        for(UUID uuid : players){
            if(!roles.isEmpty()){
                int r = UHCCore.getRandom().nextInt(roles.size());
                int r2 = UHCCore.getRandom().nextInt(players.size());
                this.getWWPlayer(players.get(r2)).setRole(roles.get(r));
                roles.remove(r);
                players.remove(players.get(r2));
            } else {
                int r2 = UHCCore.getRandom().nextInt(players.size());
                this.getWWPlayer(players.get(r2)).setRole(Roles.VILLAGER);
            }

        }
    }

    public WWUHCPlayer getWWPlayer(UUID uuid){
        return wwuhcPlayers.get(uuid);
    }

    public WWUHCPlayer getWWPlayer(String name){
        return wwuhcPlayers.get(Players.getOfflinePlayer(name).getUniqueId());
    }

    public boolean isRoleActivated(Roles role){
        return roles.contains(role);
    }

    public int getDiscover() {
        return discover;
    }

    public void setDiscover(int discover) {
        this.discover = discover;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public Map<UUID, WWUHCPlayer> getWwuhcPlayers() {
        return wwuhcPlayers;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public ArrayList<Roles> getRoles() {
        return roles;
    }

    public static WereWolves getWereWolves(){
        return wereWolves;
    }

}
