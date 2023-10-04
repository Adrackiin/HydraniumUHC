package fr.adrackiin.hydranium.uhc.game.gameworld.worldsetting;

import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import fr.adrackiin.hydranium.uhc.game.gameworld.WorldGeneration;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class FlatLandLapis extends WorldGeneration {

    public FlatLandLapis() {
        super(new GameWorld.Environment[]{GameWorld.Environment.OVERWORLD},
                "§aAjout de Lapis-Lazuli en cours...",
                Prefix.uhc + "§7Analyse de §c%totalBlock%§7 blocs en §c%time% §7secondes pour §c%changedBlocks%§7 Lapis-Lazuli ajoutés",
                "§aAjout de Lapis-Lazuli: §6%loading%%");
        this.setGenerationModifier(()-> {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y <= 32; y++) {
                        Block block = chunkTemp.getBlock(x, y, z);
                        this.incrementTotalBlock();
                        if (block.getType() == Material.WORKBENCH) {
                            block.getRelative(BlockFace.UP).setType(Material.LAPIS_BLOCK);
                            block.getRelative(BlockFace.UP, 2).setType(Material.LAPIS_BLOCK);
                            this.incrementChangedBlock(2);
                        }
                    }
                }
            }
        });
    }
}
