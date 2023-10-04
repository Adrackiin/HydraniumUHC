package fr.adrackiin.hydranium.uhc.utils.minecraft;

import org.bukkit.block.BlockFace;

public class BlockFaceUtil{

    public static BlockFace getBlockFace(double yaw)
    {
        if(yaw < -180){
            yaw += 360;
        } else if(yaw > 180){
            yaw -= 360;
        }
        double y = (yaw / 22.5D);
        if((Math.abs(y - ((int)y))) > 0.5){
            if(y > 0){
                y++;
            } else {
                y--;
            }
        }
        switch ((int)y)
        {
            case -8:
                return BlockFace.NORTH;
            case -7:
                return BlockFace.NORTH_NORTH_EAST;
            case -6:
                return BlockFace.NORTH_EAST;
            case -5:
                return BlockFace.EAST_NORTH_EAST;
            case -4:
                return BlockFace.EAST;
            case -3:
                return BlockFace.EAST_SOUTH_EAST;
            case -2:
                return BlockFace.SOUTH_EAST;
            case -1:
                return BlockFace.SOUTH_SOUTH_EAST;
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.SOUTH_SOUTH_WEST;
            case 2:
                return BlockFace.SOUTH_WEST;
            case 3:
                return BlockFace.WEST_SOUTH_WEST;
            case 4:
                return BlockFace.WEST;
            case 5:
                return BlockFace.WEST_NORTH_WEST;
            case 6:
                return BlockFace.NORTH_WEST;
            case 7:
                return BlockFace.NORTH_NORTH_WEST;
            case 8:
                return BlockFace.NORTH;
        }
        return BlockFace.NORTH;
    }
}
