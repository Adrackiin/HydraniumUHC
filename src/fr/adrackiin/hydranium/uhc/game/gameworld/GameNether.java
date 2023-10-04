package fr.adrackiin.hydranium.uhc.game.gameworld;

import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.api.utils.HashSet;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class GameNether implements Listener {

    private int searchRadius = 32;

    private final HashSet<Block> netherPortalsWorld = new HashSet<>();
    private final HashSet<Block> netherPortalsNether = new HashSet<>();
    private final APHash<Integer, Block> nearest = new APHash<>();
    private final Settings settings = Game.getGame().getSettings();

    public GameNether(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler
    public void onCreatePortal(PortalCreateEvent e){
        if(!Game.getGame().getSettings().isNether()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortalBreakOrCreate(BlockPhysicsEvent e){
        if(e.getBlock().getType() == Material.PORTAL){
            Block check = e.getBlock();
            Block portal = getNetherPortalBlock(check, getRotationPortal(check));
            if(e.getChangedType() == Material.PORTAL){
                if(e.getBlock().getWorld() == Game.getGame().getWorldUHC().getWorld()){
                    netherPortalsWorld.remove(portal);
                } else if(e.getBlock().getWorld() == Game.getGame().getWorldNether().getWorld()){
                    netherPortalsNether.remove(portal);
                }
            } else {
                if(e.getBlock().getWorld() == Game.getGame().getWorldUHC().getWorld()){
                    netherPortalsWorld.add(portal);
                } else if(e.getBlock().getWorld() == Game.getGame().getWorldNether().getWorld()){
                    netherPortalsNether.add(portal);
                }
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (!Game.getGame().getSettings().isNether()) {
            e.setCancelled(true);
            return;
        }
        World uhc = Game.getGame().getWorldUHC().getWorld();
        World nether = Game.getGame().getWorldNether().getWorld();
        if(Game.getGame().getSettings().isNetherPortalProtection() && e.getFrom().getWorld() == uhc){
            Player player = e.getPlayer();
            //Game.getGame().getInvulPlayers().add(player.getUniqueId());
            Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                if(player.getFireTicks() != 0){
                    player.setFireTicks(0);
                }
                //Game.getGame().getInvulPlayers().remove(player.getUniqueId());
            }, 5*20L);
        }

        Location locPlayer = e.getPlayer().getLocation();

        if (e.getFrom().getWorld() == uhc) {

            Location toNether = new Location(nether,
                    (locPlayer.getX() / 8.0D),
                    (locPlayer.getY()),
                    (locPlayer.getZ() / 8.0D));

            if(netherPortalsNether.getSize() == 0){
                e.setTo(createNetherPortal(toNether, 1));
                return;
            }

            int xPlayer = (int)toNether.getX();
            int zPlayer = (int)toNether.getZ();

            for (Block netherPortal : netherPortalsNether.copy()) {

                int xPortal = netherPortal.getX() - xPlayer;
                int zPortal = netherPortal.getZ() - zPlayer;

                if ((int) Math.sqrt(xPortal * xPortal + zPortal * zPortal) <= searchRadius) {
                    nearest.put((int) Math.sqrt(xPortal * xPortal + zPortal * zPortal), netherPortal);
                }
            }

            if(nearest.getSize() > 0){
                int closest = searchRadius;
                for(int range : nearest.getKeys()){
                    if(range < closest){
                        closest = range;
                    }
                }
                e.setTo(nearest.get(closest).getLocation());
                nearest.clear();
            } else {
                e.setTo(createNetherPortal(toNether, 1));
            }
        } else if (e.getFrom().getWorld() == nether) {
            Location toOverworld = new Location(uhc,
                    (locPlayer.getX() * 8.0D),
                    (locPlayer.getY()),
                    (locPlayer.getZ() * 8.0D));

            if(netherPortalsWorld.getSize() == 0){
                e.setTo(createNetherPortal(toOverworld, 0));
                return;
            }

            int xPlayer = (int)toOverworld.getX();
            int zPlayer = (int)toOverworld.getZ();

            for (Block netherPortal : netherPortalsWorld.copy()) {

                int xPortal = netherPortal.getX() - xPlayer;
                int zPortal = netherPortal.getZ() - zPlayer;

                if ((int) Math.sqrt(xPortal * xPortal + zPortal * zPortal) <= searchRadius*8) {
                    nearest.put((int) Math.sqrt(xPortal * xPortal + zPortal * zPortal), netherPortal);
                }
            }

            if(nearest.getSize() > 0){
                int closest = searchRadius*8;
                for(int range : nearest.getKeys()){
                    if(range < closest){
                        closest = range;
                    }
                }
                e.setTo(nearest.get(closest).getLocation());
                nearest.clear();
            } else {
                e.setTo(createNetherPortal(toOverworld, 0));
            }
        }
    }

    public int getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    private Location createNetherPortal(Location locToCreate, int world){
        if(world == 0){
            if(Math.abs(locToCreate.getX()) > settings.getSize()){
                locToCreate.setX(locToCreate.getX() < 0 ? -settings.getSize() + 10 : settings.getSize() - 10);
            }
            if(Math.abs(locToCreate.getZ()) > settings.getSize()){
                locToCreate.setZ(locToCreate.getZ() < 0 ? -settings.getSize() + 10 : settings.getSize() - 10);
            }
            for(int i = 5; i < 200; i ++){
                locToCreate.setY(i);
                if((locToCreate.getBlock().getType() == Material.STONE ||
                        locToCreate.getBlock().getType() == Material.DIRT ||
                        locToCreate.getBlock().getType() == Material.GRASS ||
                        locToCreate.getBlock().getType() == Material.OBSIDIAN ||
                        locToCreate.getBlock().getType() == Material.GRAVEL)&&
                        locToCreate.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR){
                    Block portal = locToCreate.getBlock();
                    if(portal.getRelative(BlockFace.UP, 2).getType() == Material.AIR &&
                            portal.getRelative(BlockFace.UP, 3).getType() == Material.AIR) {

                        if(portal.getRelative(BlockFace.NORTH).getType() != Material.AIR &&
                                portal.getRelative(BlockFace.EAST).getType() != Material.AIR){
                            return createNetherPortal(portal, UHCCore.getRandom().nextBoolean(), 0);
                        } else if(portal.getRelative(BlockFace.EAST).getType() == Material.AIR){
                            return createNetherPortal(portal, true, 0);
                        } else if(portal.getRelative(BlockFace.NORTH).getType() == Material.AIR){
                            return createNetherPortal(portal, false, 0);
                        } else {
                            return createNetherPortal(portal, UHCCore.getRandom().nextBoolean(), 0);
                        }
                    }
                }
            }

            for(int i = 200; i > 5; i --){
                locToCreate.setY(i);
                if(locToCreate.getBlock().getType() != Material.AIR){
                    return createNetherPortal(locToCreate.getBlock(), UHCCore.getRandom().nextBoolean(), 0);
                }
            }
            locToCreate.setY(40 + UHCCore.getRandom().nextInt(30));
            return createNetherPortal(locToCreate.getBlock(), UHCCore.getRandom().nextBoolean(), 0);
        } else if(world == 1){
            for(int i = 31; i < 100; i ++){
                locToCreate.setY(i);
                if((locToCreate.getBlock().getType() == Material.NETHERRACK ||
                        locToCreate.getBlock().getType() == Material.NETHER_BRICK ||
                        locToCreate.getBlock().getType() == Material.NETHER_BRICK_STAIRS ||
                        locToCreate.getBlock().getType() == Material.SOUL_SAND ||
                        locToCreate.getBlock().getType() == Material.GRAVEL)&&
                        locToCreate.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR){
                    Block portal = locToCreate.getBlock();
                    if(portal.getRelative(BlockFace.UP, 2).getType() == Material.AIR &&
                            portal.getRelative(BlockFace.UP, 3).getType() == Material.AIR) {

                        if(portal.getRelative(BlockFace.NORTH).getType() != Material.AIR &&
                                portal.getRelative(BlockFace.EAST).getType() != Material.AIR){
                            return createNetherPortal(portal, UHCCore.getRandom().nextBoolean(), 1);
                        } else if(portal.getRelative(BlockFace.EAST).getType() == Material.AIR){
                            return createNetherPortal(portal, true, 1);
                        } else if(portal.getRelative(BlockFace.NORTH).getType() == Material.AIR){
                            return createNetherPortal(portal, false, 1);
                        } else {
                            return createNetherPortal(portal, UHCCore.getRandom().nextBoolean(), 1);
                        }
                    }
                }
            }
            locToCreate.setY(40 + UHCCore.getRandom().nextInt(30));
            return createNetherPortal(locToCreate.getBlock(), UHCCore.getRandom().nextBoolean(), 1);
        } else{
            return new Location(Game.getGame().getWorldUHC().getWorld(), 0, 100, 0);
        }
    }

    private Location createNetherPortal(Block portal, boolean rotationPortal, int world){
        /*
         * O O O O   H I J K
         * O A A O   D E F G
         * O A A O   9 A B C
         * O F A O   5 6 7 8
         * O O O O   1 2 3 4
         */
        if(rotationPortal){

            if(Game.getGame().getSettings().isNetherPortalProtection() && world == 1) {
                Block toFill = portal.getRelative(3, 0, 2);
                for(int y = 0; y < 7; y ++) {
                    for (int x = 0; x < 7; x ++) {
                        for (int z = 0; z < 8; z ++) {
                            if(y == 0) {
                                toFill.getRelative(-x, 0, -z).setType(Material.BEDROCK);
                            } else {
                                toFill.getRelative(-x, y, -z).setType(Material.AIR);
                            }
                        }
                    }
                }

            } else {
                Block toFill = portal.getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                for(int y = 0; y < 3; y ++){
                    for(int x = 0; x < 3; x ++) {
                        for (int z = 0; z < 2; z++) {
                            toFill.getRelative(-x, y, -z).setType(Material.AIR);
                        }
                    }
                }
                toFill = portal.getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH);
                for(int x = 0; x < 3; x ++){
                    for(int z = 0; z < 2; z ++){
                        toFill.getRelative(-x, 0, -z).setType(Material.OBSIDIAN);
                    }
                }
            }

            portal.setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.NORTH, 1).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.NORTH, 2).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.NORTH, 3).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 1).setType(Material.OBSIDIAN);
            portal.getRelative(0, 1, -3).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 2).setType(Material.OBSIDIAN);
            portal.getRelative(0, 2, -3).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 3).setType(Material.OBSIDIAN);
            portal.getRelative(0, 3, -3).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 4).setType(Material.OBSIDIAN);
            portal.getRelative(0, 4, -1).setType(Material.OBSIDIAN);
            portal.getRelative(0, 4, -2).setType(Material.OBSIDIAN);
            portal.getRelative(0, 4, -3).setType(Material.OBSIDIAN);

            portal.getRelative(0, 1, -1).setType(Material.FIRE);

        } else {

            if(Game.getGame().getSettings().isNetherPortalProtection() && world == 1) {
                Block toFill = portal.getRelative(-2, 0, 3);
                for(int k = 0; k < 7; k ++) {
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 8; j++) {
                            if(k == 0) {
                                toFill.getRelative(j, 0, -i).setType(Material.BEDROCK);
                            } else {
                                toFill.getRelative(j, k, -i).setType(Material.AIR);
                            }
                        }
                    }
                }
            } else {
                Block toFill = portal.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                for(int y = 0; y < 3; y ++){
                    for(int x = 0; x < 2; x ++) {
                        for (int z = 0; z < 3; z++) {
                            toFill.getRelative(x, y, -z).setType(Material.AIR);
                        }
                    }
                }
                toFill = portal.getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH);
                for(int x = 0; x < 2; x ++){
                    for(int z = 0; z < 3; z ++){
                        toFill.getRelative(x, 0, -z).setType(Material.OBSIDIAN);
                    }
                }
            }

            portal.setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.EAST, 1).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.EAST, 2).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.EAST, 3).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 1).setType(Material.OBSIDIAN);
            portal.getRelative(3, 1, 0).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 2).setType(Material.OBSIDIAN);
            portal.getRelative(3, 2, 0).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 3).setType(Material.OBSIDIAN);
            portal.getRelative(3, 3, 0).setType(Material.OBSIDIAN);
            portal.getRelative(BlockFace.UP, 4).setType(Material.OBSIDIAN);
            portal.getRelative(1, 4, 0).setType(Material.OBSIDIAN);
            portal.getRelative(2, 4, 0).setType(Material.OBSIDIAN);
            portal.getRelative(3, 4, 0).setType(Material.OBSIDIAN);

            portal.getRelative(1, 1, 0).setType(Material.FIRE);
        }
        return rotationPortal ? portal.getLocation().add(0, 1, -1) : portal.getLocation().add(1, 1, 0);
    }

    private int getRotationPortal(Block check){
        if(check.getRelative(BlockFace.NORTH).getType() != Material.PORTAL &&
                check.getRelative(BlockFace.SOUTH).getType() != Material.PORTAL &&
                (check.getRelative(BlockFace.EAST).getType() == Material.PORTAL ||
                        check.getRelative(BlockFace.WEST).getType() == Material.PORTAL)){
            return 0;
        } else if(check.getRelative(BlockFace.EAST).getType() != Material.PORTAL &&
                check.getRelative(BlockFace.WEST).getType() != Material.PORTAL &&
                (check.getRelative(BlockFace.NORTH).getType() == Material.PORTAL ||
                        check.getRelative(BlockFace.SOUTH).getType() == Material.PORTAL)) {
            return 1;
        } else {
            return -1;
        }
    }

    private Block getNetherPortalBlock(Block check, int rotation){
        while(check.getRelative(BlockFace.DOWN).getType() == Material.PORTAL) {
            check = check.getRelative(BlockFace.DOWN);
        }
        if(rotation == 1){
            while(check.getRelative(BlockFace.SOUTH).getType() == Material.PORTAL) {
                check = check.getRelative(BlockFace.SOUTH);
            }
        } else if(rotation == 0){
            while(check.getRelative(BlockFace.WEST).getType() == Material.PORTAL) {
                check = check.getRelative(BlockFace.WEST);
            }
        }
        return check;
    }
}
