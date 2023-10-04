package fr.adrackiin.hydranium.uhc.game.gameworld;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class WorldGeneration implements Listener, Runnable {

    private final GameWorld.Environment[] worlds;
    private final String finishMessage;
    private final String startTitle;
    private final String loadingBar;
    private final HashSet<Location> locations = new HashSet<>();
    protected Chunk chunkTemp;
    private boolean active;
    private Runnable generation;
    private int totalLocations;
    private int id;
    private int startTime;
    private int loading;
    private boolean isFinish = false;
    private int totalBlocks = 0;
    private int changedBlock = 0;

    public WorldGeneration(GameWorld.Environment[] worlds, String startTitle, String finishMessage, String loadingBar) {
        this.worlds = worlds;
        this.startTitle = startTitle;
        this.finishMessage = finishMessage;
        this.loadingBar = loadingBar;
        this.setActive(false);
    }

    @Override
    public void run(){
        long start = System.currentTimeMillis();
        Location tempLoc;
        while(System.currentTimeMillis() - start < 40L){
            if (this.getLocations().isEmpty()) {
                Bukkit.getScheduler().cancelTask(this.getId());
                APICore.getPlugin().getPlayerManager().sendMessageAll(this.getFinishMessage());
                this.setFinish(true);
                this.stop();
                UHCCore.getPlugin().getServer().getPluginManager().callEvent(new WorldCheckFinishEvent());
                return;
            }
            tempLoc = this.getLocations().first();
            chunkTemp = tempLoc.getWorld().getChunkAt(tempLoc.getBlockX(), tempLoc.getBlockZ());
            this.getLocations().remove();
            this.getGeneration().run();
        }
            APICore.getPlugin().getPlayerManager().sendMessageBarAll("§aPrégénération: §6" + (int)((((float)this.totalLocations - (float)this.getLocations().getSize()) / (float)this.totalLocations) * 100) + "%");

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFinishPregeneration(WorldCheckFinishEvent e) {
        if(this.isFinish()){
            return;
        }
        e.setCancelled(true);
        for(GameWorld.Environment world : worlds){
            switch(world){
                case OVERWORLD: this.locations.add(Game.getGame().getWorldUHC().getChunksLocation()); break;
                case NETHER: this.locations.add(Game.getGame().getWorldNether().getChunksLocation()); break;
                case ENDER: this.locations.add(Game.getGame().getWorldEnder().getChunksLocation()); break;
            }
        }
        APICore.getPlugin().getPlayerManager().sendTitleAll("§0", this.getStartTitle());
        onFinishPregeneration();
    }

    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    public void stop() {
        HandlerList.unregisterAll(this);
    }

    public void incrementLoading(){
        this.loading ++;
    }

    public void incrementTotalBlock(){
        this.totalBlocks ++;
    }

    public void incrementChangedBlock(){
        this.changedBlock ++;
    }

    public void incrementChangedBlock(int increment){
        this.changedBlock += increment;
    }

    public String getStartTitle() {
        return startTitle;
    }

    public String getFinishMessage() {
        String[] temp = this.finishMessage.split("%totalBlock%|%time%|%changedBlocks%");
        return temp[0] + this.getTotalBlocks() + temp[1] + this.getTimeTaken() + temp[2] + this.getChangedBlock() + temp[3];
    }

    public String getTimeTaken() {
        return String.valueOf(((int)System.currentTimeMillis() - this.startTime) / 1000);
    }

    public String getLoadingBar() {
        String[] temp = this.loadingBar.split("%loading%");
        return temp[0] + this.getLoading() + temp[1];
    }

    public Runnable getGeneration() {
        return generation;
    }

    public GameWorld.Environment[] getWorlds() {
        return worlds;
    }

    public int getTotalLocations() {
        return totalLocations;
    }

    public void setTotalLocations(int totalLocations) {
        this.totalLocations = totalLocations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getLoading() {
        return loading;
    }

    public void setLoading(int loading) {
        this.loading = loading;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public void setTotalBlocks(int totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public int getChangedBlock() {
        return changedBlock;
    }

    public void setChangedBlock(int changedBlock) {
        this.changedBlock = changedBlock;
    }

    public HashSet<Location> getLocations() {
        return locations;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setGenerationModifier(Runnable generation){
        this.generation = generation;
    }

    private void onFinishPregeneration(){
        this.setTotalLocations(this.getLocations().getSize());
        this.setStartTime((int)System.currentTimeMillis());
        this.setId(Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 1L, 1L));
    }
}
