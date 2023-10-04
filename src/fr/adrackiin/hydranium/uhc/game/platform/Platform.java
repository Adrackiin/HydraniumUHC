package fr.adrackiin.hydranium.uhc.game.platform;

import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.Material;
import org.bukkit.World;

public class Platform {

    public Platform(){
        createPlatform();
    }

    public void createPlatform(){
        platform(Game.getGame().getWorldUHC().getY());
    }

    @SuppressWarnings("deprecation")
    private void platform(int yBase){
        World world = Game.getGame().getWorldUHC().getWorld();
        for(int x = -17; x <= 16; x ++) {
            for (int z = -17; z <= 16; z++) {
                if ((x >= -13 && x <= 12) && (z >= -13 && z <= 12)) {
                    world.getBlockAt(x, yBase, z).setType(Material.STAINED_GLASS);
                    world.getBlockAt(x, yBase, z).setData((byte) 14);
                } else {
                    world.getBlockAt(x, yBase, z).setType(Material.STAINED_GLASS);
                }
                if ((x >= -12 && x <= 11) && (z >= -12 && z <= 11)) {
                    world.getBlockAt(x, yBase, z).setType(Material.BARRIER);
                }
            }
        }
        for(int x = -17; x <= 16; x ++){
            for(int z = -17; z <= 16; z ++){
                if(x == -17 || x == 16 || z == -17 || z == 16){
                    for(int y = yBase; y <= yBase + 4; y ++) {
                        world.getBlockAt(x, y, z).setType(Material.STAINED_GLASS);
                        if(y <= yBase + 2){
                            world.getBlockAt(x, y, z).setData((byte)0);
                        } else if(y == yBase + 3){
                            world.getBlockAt(x, y, z).setData((byte)8);
                        } else {
                            world.getBlockAt(x, y, z).setData((byte)14);
                        }
                    }
                } else if(x == -16 || x == 15 || z == -16 || z == 15){
                    world.getBlockAt(x, yBase + 1, z).setType(Material.CARPET);
                    world.getBlockAt(x, yBase + 1, z).setData((byte)14);
                }
            }
        }
        for(int x = -18; x <= 17; x ++){
            for(int z = -18; z <= 17; z ++){
                world.getBlockAt(x, yBase - 1, z).setType(Material.BARRIER);
                world.getBlockAt(x, yBase + 10, z).setType(Material.BARRIER);
            }
        }
        for(int x = -18; x <= 17; x ++){
            for(int y = yBase - 1; y <= yBase + 10; y ++){
                world.getBlockAt(x, y, -18).setType(Material.BARRIER);
                world.getBlockAt(x, y, 17).setType(Material.BARRIER);
            }
        }
        for(int z = -18; z <= 17; z ++){
            for(int y = yBase - 1; y <= yBase + 10; y ++){
                world.getBlockAt(-18, y, z).setType(Material.BARRIER);
                world.getBlockAt(17, y, z).setType(Material.BARRIER);
            }
        }

    }

}
