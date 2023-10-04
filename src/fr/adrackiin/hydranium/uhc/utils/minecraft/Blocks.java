package fr.adrackiin.hydranium.uhc.utils.minecraft;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blocks {

    private static final HBlockFace[] blockFaces = HBlockFace.values();
    private static final HBlockFace[] blockSides = {HBlockFace.NORTH, HBlockFace.SOUTH, HBlockFace.EAST, HBlockFace.WEST, HBlockFace.NORTH_EAST, HBlockFace.NORTH_WEST, HBlockFace.SOUTH_EAST, HBlockFace.SOUTH_WEST};
    private static final List<Material> fluids = Arrays.asList(Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA);
    private static final List<Material> instantMining = Arrays.asList(Material.WATER_LILY, Material.CARROT, Material.DEAD_BUSH, Material.FIRE, Material.FLOWER_POT, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.MELON_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WARTS, Material.POTATO, Material.PUMPKIN_STEM, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.TORCH, Material.REDSTONE_WIRE, Material.SAPLING, Material.SLIME_BLOCK, Material.SUGAR_CANE_BLOCK, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.CROPS);
    private static final ArrayList<Block> countBlock = new ArrayList<>();
    private static final ArrayList<Block> countBlock2 = new ArrayList<>();
    private static final ArrayList<Block> countBlock3 = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public static void head(Block block, String name, Location location){
        Skull skull = (Skull)block.getRelative(BlockFace.UP).getState();
        skull.setOwner(name);
        skull.setRawData((byte)1);
        skull.setSkullType(SkullType.PLAYER);
        skull.setRotation(BlockFaceUtil.getBlockFace(location.getYaw()));
        skull.update();
    }

    public static List<Block> getVein(Block block, String metadata){
        countBlock2.clear();
        count(block, metadata);
        return countBlock2;
    }

    public static List<Block> getVein(Block block, List<Material> type){
        countBlock.clear();
        count(block, type);
        return countBlock;
    }

    public static List<Block> getVein(Block block, List<Material> type, String metadata){
        countBlock3.clear();
        count(block, type, metadata);
        return countBlock3;
    }

    public static boolean isVeinContactFluid(List<Block> vein){
        for(Block block : vein){
            for(HBlockFace blockFace : blockFaces){
                Material check = getRelative(block, blockFace).getType();
                if(check == Material.AIR || check == Material.WATER || check == Material.LAVA || check == Material.STATIONARY_LAVA || check == Material.STATIONARY_WATER){
                    return true;
                }
            }
        }
        return false;
    }

    public static void remplaceArround(Block block, Material toReplace, Material type, boolean onlySide){
        if(onlySide){
            for(HBlockFace blockFace : blockSides){
                if(getRelative(block, blockFace).getType() == toReplace){
                    getRelative(block, blockFace).setType(type);
                }
            }
        } else {
            for(HBlockFace blockFace : blockFaces){
                if(getRelative(block, blockFace).getType() == toReplace){
                    getRelative(block, blockFace).setType(type);
                }
            }
        }
    }

    public static int getHighestBlock(World world, int x, int z){
        for(int y = 255; y >= 0; y --){
            if(world.getBlockAt(x, y, z).getType() != Material.AIR){
                return y;
            }
        }
        return 0;
    }

    public static void noFluidSize(Block toCheck, Material toFill) {
        for(HBlockFace blockFace : blockSides){
            if(fluids.contains(getRelative(toCheck, blockFace).getType())){
                getRelative(toCheck, blockFace).setType(toFill);
            }
        }
    }

    /**
     * Warning: May cause lag
     *
     * @param block
     */
    @Deprecated
    public static void breakParticule(Block block){
        float x = block.getX();
        float y = block.getY();
        float z = block.getZ();
        for(Player p : Bukkit.getOnlinePlayers()){
            float x2 = x - p.getLocation().getBlockX();
            float z2 = z - p.getLocation().getBlockZ();
            if (Math.sqrt(x2 * x2 + z2 * z2) < 50.0F) {
                for(float xParticle = 0; xParticle < 1; xParticle += 0.33F){
                    for(float yParticle = 0; yParticle < 1; yParticle += 0.33F){
                        for(float zParticle = 0; zParticle < 1; zParticle += 0.33F){
                            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(
                                    PacketUtils.packetParticules(
                                            EnumParticle.BLOCK_CRACK,
                                            x + xParticle,
                                            y + yParticle,
                                            z + zParticle, 0, 0, 0, 0, 1, block.getType().getId() + block.getData() * 4096));
                        }
                    }
                }
            }
        }
    }

    public static Block getRelative(Block block, HBlockFace blockFace){
        return block.getRelative(blockFace.getX(), blockFace.getY(), blockFace.getZ());
    }

    public static boolean isInstantMining(Material type){
        return instantMining.contains(type);
    }

    private static void count(Block block, String metadata){
        if(block.hasMetadata(metadata) && !countBlock2.contains(block)){
            countBlock2.add(block);
            for(HBlockFace blockFace : blockFaces){
                count(getRelative(block, blockFace), metadata);
            }
        }
    }

    private static void count(Block block, List<Material> type){
        if(type.contains(block.getType()) && !countBlock.contains(block)){
            countBlock.add(block);
            for(HBlockFace blockFace : blockFaces){
                count(getRelative(block, blockFace), type);
            }
        }
    }

    private static void count(Block block, List<Material> type, String metadata){
        if(type.contains(block.getType()) && !countBlock3.contains(block) && !block.hasMetadata(metadata)){
            countBlock3.add(block);
            for(HBlockFace blockFace : blockFaces){
                count(getRelative(block, blockFace), type, metadata);
            }
        }
    }

    public enum HBlockFace {

        NORTH(0, 0, -1),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0),
        EAST(1, 0, 0),
        NORTH_WEST(-1, 0, -1),
        NORTH_EAST(1, 0, -1),
        SOUTH_WEST(-1, 0, 1),
        SOUTH_EAST(1, 0, 1),
        UP(0, 1, 0),
        UP_NORTH(0, 1, -1),
        UP_SOUTH(0, 1, 1),
        UP_WEST(-1, 1, 0),
        UP_EAST(1, 1, 0),
        UP_NORTH_WEST(-1, 1, -1),
        UP_NORTH_EAST(1, 1, -1),
        UP_SOUTH_WEST(-1, 1, 1),
        UP_SOUTH_EAST(1, 1, 1),
        DOWN(0, -1, 0),
        DOWN_NORTH(0, -1, -1),
        DOWN_SOUTH(0, -1, 1),
        DOWN_WEST(-1, -1, 0),
        DOWN_EAST(1, -1, 0),
        DOWN_NORTH_WEST(-1, -1, -1),
        DOWN_NORTH_EAST(1, -1, -1),
        DOWN_SOUTH_WEST(-1, -1, 1),
        DOWN_SOUTH_EAST(1, -1, 1);

        private final int x;
        private final int y;
        private final int z;

        HBlockFace(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }

}
