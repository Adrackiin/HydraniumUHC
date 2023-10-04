package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class LavaLess extends Scenario implements Listener {

/*        super(new GameWorld.Environment[]{GameWorld.Environment.OVERWORLD, GameWorld.Environment.NETHER},
                "§aSupression de la lave en cours...",
                Prefix.uhc + "§7Analyse de §c%totalBlock%§7 blocs en §c%time% §7secondes pour §c%changedBlocks%§7 blocks de lave supprimés",
                "§aSupression de la lave: §6%loading%%");
        this.setGenerationModifier(()-> {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y <= 256; y++) {
                        Block block = chunkTemp.getBlock(x, y, z);
                        this.incrementTotalBlock();
                        if(block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
                            block.setMetadata("lava", new FixedMetadataValue(APICore.getPlugin(), ""));
                        }
                    }
                }
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y <= 256; y++) {
                        Block block = chunkTemp.getBlock(x, y, z);
                        this.incrementTotalBlock();
                        if(block.hasMetadata("lava")) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        });
    }*/


    private final HashSet<Location> locations = new HashSet<>();
    private boolean active;
    private int totalLocations;
    private int id;
    private int startTime;
    private int loading;
    private boolean isFinish = false;
    private int totalBlocks = 0;
    private int changedBlock = 0;

    private Chunk chunkTemp;
    private Location tempLoc;
    private Block block;

    private int state = 0;

    public LavaLess() {
        super("LavaLess",
                "La lave n'est pas générée",
                new String[]{"", "§7La lave n'est", "§7pas générée"});
        this.setActive(false);
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
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @Override
    public void disable(){
        HandlerList.unregisterAll(this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void run(){
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 40L){
            if (this.getLocations().isEmpty()) {
                Bukkit.getScheduler().cancelTask(this.getId());
                if(state == 0){
                    state = 1;
                    APICore.getPlugin().getPlayerManager().sendMessageAll(Prefix.uhc + "§7Analyse de §c" + this.getTotalBlocks() + "§7 blocs en §c" + this.getTimeTaken() + " §7secondes pour §c" + this.getChangedBlock() + "§7 blocks de lave remplacés");
                    this.setChangedBlock(0);
                    this.setTotalBlocks(0);
                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new WorldCheckFinishEvent());
                    return;
                }
                APICore.getPlugin().getPlayerManager().sendMessageAll(Prefix.uhc + "§7Analyse de §c" + this.getTotalBlocks() + "§7 blocs en §c" + this.getTimeTaken() + " §7secondes pour §c" + this.getChangedBlock() + "§7 blocks de lave supprimés");
                this.setFinish(true);
                //this.stop();
                UHCCore.getPlugin().getServer().getPluginManager().callEvent(new WorldCheckFinishEvent());
                return;
            }
            tempLoc = this.getLocations().first();
            chunkTemp = tempLoc.getWorld().getChunkAt(tempLoc.getBlockX(), tempLoc.getBlockZ());
            this.getLocations().remove();
            if(this.state == 0){
                for(int x = 0; x < 16; x++){
                    for(int z = 0; z < 16; z++){
                        for(int y = 0; y <= 128; y++){
                            block = chunkTemp.getBlock(x, y, z);
                            this.incrementTotalBlock();
                            if(block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA){
                                block.setType(Material.STAINED_GLASS_PANE);
                                this.incrementChangedBlock();
                            }
                        }
                    }
                }
                APICore.getPlugin().getPlayerManager().sendMessageBarAll("§aSupression: §6" + (int)((((float)this.totalLocations - (float)this.getLocations().getSize()) / (float)this.totalLocations) * 100) + "%");
            } else {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y <= 128; y++) {
                            block = chunkTemp.getBlock(x, y, z);
                            this.incrementTotalBlock();
                            if(block.getType() == Material.STAINED_GLASS_PANE) {
                                block.setType(Material.AIR);
                                this.incrementChangedBlock();
                            }
                        }
                    }
                }
                APICore.getPlugin().getPlayerManager().sendMessageBarAll("§aFinition: §6" + (int)((((float)this.totalLocations - (float)this.getLocations().getSize()) / (float)this.totalLocations) * 100) + "%");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFinishPregeneration(WorldCheckFinishEvent e) {
        if(this.isFinish()){
            return;
        }
        e.setCancelled(true);
        this.locations.add(Game.getGame().getWorldUHC().getChunksLocation());
        APICore.getPlugin().getPlayerManager().sendTitleAll("§0", "§aSupression de la lave en cours...");
        onFinishPregeneration();
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

    public String getTimeTaken() {
        return String.valueOf(((int)System.currentTimeMillis() - this.startTime) / 1000);
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

    private void onFinishPregeneration(){
        this.setTotalLocations(this.getLocations().getSize());
        this.setStartTime((int)System.currentTimeMillis());
        this.setId(Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this::run, 1L, 1L));
    }

}
