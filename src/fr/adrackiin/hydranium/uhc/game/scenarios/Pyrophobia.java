package fr.adrackiin.hydranium.uhc.game.scenarios;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

public class Pyrophobia extends Scenario implements Listener {

    public Pyrophobia() {
        super(
                "Pyrophobia",
                "L'eau est remplacée par la Lave, la canne à sucre s'obtient par les feuilles, l'obidienne est trouvable en grotte",
                new String[]{"", "§7L'eau est remplacée par la Lave", "§7la canne à sucre s'obtient par les feuilles", "§7l'obidienne est trouvable en grotte"}
        );

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



    public void convertWorld(ChunkPopulateEvent e){
        Block block;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 1; y <= 128; y ++){
                    block = e.getChunk().getBlock(x, y, z);
                    if(block.getType() == Material.WATER){

                        block.setType(Material.EMERALD_BLOCK);
                    }
                    if(block.getType() == Material.SUGAR_CANE_BLOCK){
                        block.setType(Material.AIR);
                    }
                    if(y == 10){
                        if(block.getType() == Material.LAVA){
                            Blocks.remplaceArround(block, Material.STONE, Material.OBSIDIAN, true);
                        }
                    }
                }
            }
        }
    }
}
