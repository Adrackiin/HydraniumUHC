package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.WorldCheckFinishEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class CorruptedWorld extends Scenario implements Listener, Runnable {

    private int percentage;

    private int total;
    private int id;
    private int startTime;
    private int loading;
    private int corruptedChunks = 0;
    private final List<Chunk> chunksTemp = new ArrayList<>();
    private boolean isFinish = false;
    private boolean chooseChunk = false;
    private int i = 0;

    public CorruptedWorld() {
        super(
                "CorruptedWorld",
                "Certains chunks sont vides",
                new String[]{"", "§630% §7des chunks sont vides", "", "§c⚠ Prend beaucoup de temps à la Pregen ⚠"}
        );

        this.percentage = 30;
    }

    @Override
    public void setScenario(boolean status) {
        if(status){
            enable();
        } else {
            disable();
        }
        this.setActive(status);
    }

    @Override
    public void enable() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }



    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 40L) {
            if (!chooseChunk) {
                if(i < chunksTemp.size()) {
                    if(UHCCore.getRandom().nextDouble() > (1 - (percentage / 100))){
                        chunksTemp.remove(chunksTemp.get(i));
                    }
                    i ++;
                } else {
                    chooseChunk = true;
                    loading = 0;
                    total = chunksTemp.size();
                    for(APPlayer p : APICore.getPlugin().getAPPlayers()){
                        p.sendTitle("§1", "§aCorruption du monde en cours...", 10, 40, 10);
                    }
                }
            } else {
                if (chunksTemp.isEmpty()) {
                    Bukkit.getScheduler().cancelTask(id);
                    Bukkit.broadcastMessage(Prefix.uhc + "§7" + "Analyse de §c" + total + "§7 chunks en §c" +
                            (((int) System.currentTimeMillis() - this.startTime) / 1000) + " §7secondes pour §c" + corruptedChunks + "§7 chunks corrompus");
                    isFinish = true;
                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new WorldCheckFinishEvent());
                    Game.getGame().getPlatform().createPlatform();
                    return;
                }
                Chunk chunkTemp = chunksTemp.get(0);
                chunksTemp.remove(0);
                if (UHCCore.getRandom().nextDouble() < 0.3) {
                    corruptedChunks++;
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = Blocks.getHighestBlock(chunkTemp.getWorld(), x, z); y > 0; y--) {
                                Block block = chunkTemp.getBlock(x, y, z);
                                if (block.getType() != Material.AIR) {
                                    block.setType(Material.AIR);
                                }
                                if(x == 0 || x == 15 || z == 0 || z == 15){
                                    Blocks.noFluidSize(block, (UHCCore.getRandom().nextBoolean() ? Material.SOUL_SAND : Material.OBSIDIAN));
                                }
                            }
                        }
                    }
                }
                if (chunksTemp.size() % (total / 100) == 0) {
                    for (APPlayer p : APICore.getPlugin().getAPPlayers()) {
                        p.sendMessageBar("§aCorruption du monde: §6" + loading + "%");
                    }
                    loading++;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWorldCheckFinish(WorldCheckFinishEvent e){
        if(isFinish){
            return;
        }
        e.setCancelled(true);
        //chunksTemp.addAll(Game.getGame().getChunks());
        //chunksTemp.addAll(Game.getGame().getChunksNether());
        //chunksTemp.addAll(Game.getGame().getChunksEnd());
        this.startTime = (int) System.currentTimeMillis();
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCCore.getPlugin(), this, 1L, 1L);
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        this.setDescriptionHost(new String[]{"", "§6" + percentage + "% §7des chunks sont vides", "", "§c⚠ Prend beaucoup de temps à la Pregen ⚠"});
    }
}
