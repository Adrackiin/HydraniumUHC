package fr.adrackiin.hydranium.uhc.game.gameworld.worldsetting;

import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import fr.adrackiin.hydranium.uhc.game.gameworld.WorldGeneration;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collections;

public class CaveOres extends WorldGeneration {

    public CaveOres() {
        super(new GameWorld.Environment[]{GameWorld.Environment.OVERWORLD},
                "§aSupression des minerais en cours...",
                Prefix.uhc + "§7Analyse de §c%totalBlock%§7 blocs en §c%time% §7secondes pour §c%changedBlocks%§7 veines supprimés",
                "§aSupression des minerais: §6%loading%%");
        this.setGenerationModifier(()-> {
            HashSet<Block> vein = new HashSet<>();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y <= 32; y++) {
                        Block block = chunkTemp.getBlock(x, y, z);
                        this.incrementTotalBlock();
                        if (block.getType() == Material.GOLD_ORE || block.getType() == Material.DIAMOND_ORE) {
                            vein.clear();
                            vein.add(Blocks.getVein(block, Collections.singletonList(block.getType())));
                            if (!Blocks.isVeinContactFluid(vein.copy())) {
                                this.incrementChangedBlock();
                                for (Block blockVein : vein.copy()) {
                                    blockVein.setType(Material.STONE);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
