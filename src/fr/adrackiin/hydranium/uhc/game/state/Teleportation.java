package fr.adrackiin.hydranium.uhc.game.state;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class Teleportation implements Listener{

    private final boolean team = Game.getGame().isTeam();
    private final int size;
    private double distanceX;
    private double distanceZ;
    private int id;
    private int id2;
    private int max;
    private int numberOfTp;
    private int loading = 0;
    private int total;
    private final LinkedList<UUID> playerToTp = new LinkedList<>();
    private final LinkedList<Location> locTp = new LinkedList<>();
    private final LinkedList<Location> toLoad = new LinkedList<>();
    private final HashMap<UUID, String> pseudoOffline = new HashMap<>();

    public Teleportation(){
        size = Game.getGame().getSettings().getSize();
        Bukkit.getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        prepareTeleportation();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(ChunkUnloadEvent e){
        e.setCancelled(true);
    }

    private void prepareTeleportation(){
        if(Game.getGame().getAlivePlayers().getSize() == 0){
            HandlerList.unregisterAll(this);
            Bukkit.broadcastMessage(Prefix.uhc + "§7Démarrage de la partie");
            Game.getGame().start();
            Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.INVULNERABILITY));
            return;
        }
        Bukkit.broadcastMessage(Prefix.uhc + "§7Génération des spawns");
        for(UHCPlayer p : Game.getGame().getUHCPlayers().getValues()){
            if(p.isPlaying()){
                p.getAPPlayer().addPotionEffect(PotionEffectType.JUMP, 1_000_000, 255, true);
                playerToTp.add(p.getAPPlayer().getUUID());
                pseudoOffline.put(p.getAPPlayer().getUUID(), p.getAPPlayer().getName());
            }
        }
        if(team) numberOfTp = Game.getGame().getTeamManager().getNumberOfTeam();
        else numberOfTp = playerToTp.size();
        int nbPlayersLineMin = (int) Math.sqrt(numberOfTp);
        if(nbPlayersLineMin == 1){
            nbPlayersLineMin = 2;
        }
        int x = 1;
        while (x * nbPlayersLineMin < numberOfTp) {
            x++;
        }
        if (Math.abs(numberOfTp - (x - 1) * nbPlayersLineMin) < Math.abs(numberOfTp - x * nbPlayersLineMin) &&
                (x - 1) * nbPlayersLineMin >= numberOfTp) {
            x--;
        }
        if(x <= 1){
            x = 2;
        }
        distanceZ = Math.abs((double)((Game.getGame().getSettings().getSize() * 2) - 100) / (nbPlayersLineMin - 1));
        distanceX = Math.abs((double)((Game.getGame().getSettings().getSize() * 2) - 100) / (x - 1));
        this.max = Game.getGame().getSettings().getSize() - 50;
        generateSpawnLocations();
    }

    private void generateSpawnLocations(){
        int i = 0;
        int z;
        for (int x = -size + 50; x <= max; x += distanceX) {
            for (z = -size + 50; z <= max; z += distanceZ) {
                if (i < numberOfTp) {
                    locTp.add(new Location(Game.getGame().getWorldUHC().getWorld(), x + 0.5D, Game.getGame().getWorldUHC().getWorld().getHighestBlockYAt(x, z) + 5.0D, z + 0.5D));
                    i++;
                }
            }
        }
        i = 0;
        if (team) {
            for (GameTeam gameTeam : Game.getGame().getTeamManager().getAliveTeams().copy()) {
                for (UUID uuid : gameTeam.getMembers().copy()) {
                    if (Game.getGame().getUHCPlayer(uuid) != null) {
                        Game.getGame().getUHCPlayer(uuid).setSpawnLocation(locTp.get(i));
                    }
                }
                i++;
            }
        } else {
            for (UUID uuid : playerToTp) {
                if (Game.getGame().getUHCPlayer(uuid) != null) {
                    Game.getGame().getUHCPlayer(uuid).setSpawnLocation(locTp.get(i));
                }
                i++;
            }
        }
        int xChunk;
        int xChunkMax;
        int zChunk;
        int zChunkMax;
        Location temp;
        for (Location spawn : locTp) {
            xChunkMax = Bukkit.getViewDistance();
            zChunkMax = Bukkit.getViewDistance();
            for (xChunk = -xChunkMax; xChunk <= xChunkMax; xChunk++) {
                for (zChunk = -zChunkMax; zChunk <= zChunkMax; zChunk++) {
                    temp = new Location(Game.getGame().getWorldUHC().getWorld(), spawn.getX() + (spawn.getX() < 0 ? xChunk : -xChunk), 0.0D, spawn.getZ() + (spawn.getZ() < 0 ? zChunk : -zChunk));
                    if (!toLoad.contains(temp)) {
                        toLoad.add(temp);
                    }
                }
            }
        }
        startGenerateSpawn();
    }

    private void startGenerateSpawn(){
        total = toLoad.size();
        this.id2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this::generateSpawns, 1L, 1L);
    }

    private void generateSpawns(){
        Chunk tempChunk;
        Location temp;
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 40L){
            if(toLoad.isEmpty()){
                for(APPlayer p : APICore.getPlugin().getAPPlayers()){
                    p.sendMessageBar("§aGénération des spawns: §6100%");
                }
                Bukkit.getScheduler().cancelTask(this.id2);
                Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Génération des spawns terminée");
                startTeleport();
                return;
            }
            temp = toLoad.get(0);
            tempChunk = temp.getWorld().getChunkAt(temp.getBlockX(), temp.getBlockZ());
            tempChunk.load(false);
            toLoad.remove(0);
            if(toLoad.size() % ((total / 100 <= 1 ? (total / 100) + 1 : (total / 100))) == 0){
                for(APPlayer p : APICore.getPlugin().getAPPlayers()){
                    p.sendMessageBar("§aGénération des spawns: §6" + loading + "%");
                }
                if(loading < 100) {
                    loading++;
                }
            }
        }
    }

    private void startTeleport(){
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this::teleport, 20L, 5L);
        for(UHCPlayer p : Game.getGame().getUHCPlayers().getValues()){
            if(p.isPlaying()){
                p.getAPPlayer().addPotionEffect(PotionEffectType.BLINDNESS, 1_000_000, 127, true);
                p.getAPPlayer().addPotionEffect(PotionEffectType.SLOW, 1_000_000, 127, true);
                p.getAPPlayer().addPotionEffect(PotionEffectType.WEAKNESS, 1_000_000, 127, true);
            }
        }
    }

    private void teleport(){
        if(playerToTp.isEmpty()){
            Bukkit.getScheduler().cancelTask(this.id);
            HandlerList.unregisterAll(this);
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Téléportation terminée. Démarrage de la partie");
            Game.getGame().start();
            Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.INVULNERABILITY));
            return;
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(playerToTp.get(0));
        UHCCore.getPlugin().logServer(player.getSpawnLocation() + "");
        if(player.getAPPlayer().isConnected()){
            player.getAPPlayer().teleport(player.getSpawnLocation());
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Téléportation de " + "§6" + Bukkit.getPlayer(playerToTp.get(0)).getName());
        } else {
            Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Téléportation de " + "§6" + pseudoOffline.get(playerToTp.get(0)));
        }
        playerToTp.remove(0);
    }

}
