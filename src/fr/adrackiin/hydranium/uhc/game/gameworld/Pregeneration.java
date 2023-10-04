package fr.adrackiin.hydranium.uhc.game.gameworld;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashSet;
import java.util.List;

public class Pregeneration implements Runnable, Listener{

    private GameWorld.Environment environment;
    private HashSet<Location> toLoad;
    private int totalChunk;
    private int startTime;
    private int idTask;

    public Pregeneration(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        start(Game.getGame().getWorldUHC());
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Location locationTemp;
        while(System.currentTimeMillis() - start < 40L){
            if(this.getToLoad().isEmpty()){
                Bukkit.getScheduler().cancelTask(getIdTask());
                Bukkit.broadcastMessage(Prefix.uhc + "§7Génération de §c" + this.getTotalChunk() + "§7 chunks en §c" + (((int) System.currentTimeMillis() - this.getStartTime()) / 1000) + "§7 secondes du monde §b" + this.getEnvironment().toString());
                switch(this.getEnvironment()){
                    case OVERWORLD:
                        if(Game.getGame().getSettings().isNether()) start(Game.getGame().getWorldNether());
                        else start(Game.getGame().getWorldEnder());
                        break;
                    case NETHER: start(Game.getGame().getWorldEnder()); break;
                    case ENDER:
                        UHCCore.getPlugin().getServer().getPluginManager().callEvent(new WorldCheckFinishEvent());
                        return;
                }
            }
            locationTemp = this.getToLoad().first();
            this.getToLoad().remove(locationTemp);
            locationTemp.getWorld().getChunkAt(locationTemp.getBlockX(), locationTemp.getBlockZ()).load(true);
        }
        APICore.getPlugin().getPlayerManager().sendMessageBarAll("§aPrégénération: §6" + (int)((((float)this.totalChunk - (float)this.getToLoad().getSize()) / (float)this.totalChunk) * 100) + "% §7(§b" + this.getEnvironment().toString() + "§7)");
    }

    public void stop(){
        HandlerList.unregisterAll(this);
        this.toLoad.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onUnload(ChunkUnloadEvent e){
        e.setCancelled(true);
    }

    private GameWorld.Environment getEnvironment() {
        return environment;
    }

    private void setEnvironment(GameWorld.Environment environment) {
        this.environment = environment;
    }

    private HashSet<Location> getToLoad() {
        return toLoad;
    }

    private void setToLoad(List<Location> toLoad) {
        this.toLoad = new HashSet<>(new HashSet<>(toLoad));
    }

    private int getTotalChunk() {
        return totalChunk;
    }

    private void setTotalChunk(int totalChunk) {
        this.totalChunk = totalChunk;
    }

    private int getStartTime() {
        return startTime;
    }

    private void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    private int getIdTask() {
        return idTask;
    }

    private void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    private void start(GameWorld world){
        this.setEnvironment(world.getEnvironment());
        this.setToLoad(world.getChunksLocation());
        this.setTotalChunk(getToLoad().getSize());
        this.setStartTime((int)System.currentTimeMillis());
        this.setIdTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 1L, 1L));
    }
}
