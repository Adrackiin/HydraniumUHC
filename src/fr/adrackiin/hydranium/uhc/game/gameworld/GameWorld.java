package fr.adrackiin.hydranium.uhc.game.gameworld;

import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.utils.Files;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private static final World BUFFER = Bukkit.getServer().getWorld("world");
    private Environment environment;
    private World world;
    private final int ambiantLimit;
    private final int animalLimit;
    private final int monsterLimit;
    private final int waterLimit;
    private final int x;
    private int y;
    private final int z;
    private final UHCBorder border;
    private final Location spawn;

    public GameWorld(World world){
        this.world = world;
        switch(world.getEnvironment()){
            case NORMAL: this.environment = Environment.OVERWORLD; break;
            case NETHER: this.environment = Environment.NETHER; break;
            case THE_END: this.environment = Environment.ENDER; break;
        }
        this.animalLimit = 0;
        this.ambiantLimit = 0;
        this.monsterLimit = 0;
        this.waterLimit = 0;
        this.x = 0;
        if(world.getWorldType() == WorldType.FLAT){
            this.y = 50;
        } else {
            this.y = 150;
        }
        this.z = 0;
        this.border = new UHCBorder(world);
        if(world.getName().equals("uhc_nether")){
            Game.getGame().setGameNether(new GameNether());
        } else if(world.getName().equals("uhc")){
            this.world.setDifficulty(Difficulty.PEACEFUL);
            this.world.setAnimalSpawnLimit(this.animalLimit);
            this.world.setAmbientSpawnLimit(this.ambiantLimit);
            this.world.setMonsterSpawnLimit(this.monsterLimit);
            this.world.setWaterAnimalSpawnLimit(this.waterLimit);
            this.world.setPVP(false);
            this.world.setSpawnLocation(this.x, this.y, this.z);
            this.world.setStorm(false);
            this.world.setThundering(false);
            this.world.setWeatherDuration(Integer.MAX_VALUE);
            this.world.setGameRuleValue("doDaylightCycle", "false");
            this.world.setTime(6000);
        }
        this.world.setGameRuleValue("naturalRegeneration", "false");
        this.spawn = new Location(this.world, this.x, this.y + 5, this.z);
    }

    public void newWorld(WorldCreator creator) {
        Bukkit.broadcastMessage(Prefix.uhc + "§7Nouveau monde en cours de génération, §cfreeze à venir !");
        Location bufferLoc = new Location(BUFFER, 0, 10000, 0);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(bufferLoc);
            p.closeInventory();
        }
        File worldFile = world.getWorldFolder();
        Bukkit.getServer().unloadWorld(world, true);
        Files.remove(worldFile);
        this.world = creator.createWorld();
        this.world.setAnimalSpawnLimit(this.animalLimit);
        this.world.setAmbientSpawnLimit(this.ambiantLimit);
        this.world.setMonsterSpawnLimit(this.monsterLimit);
        this.world.setWaterAnimalSpawnLimit(this.waterLimit);
        this.world.setPVP(false);
        this.world.setSpawnLocation(this.x, this.y, this.z);
        this.world.setStorm(false);
        this.world.setThundering(false);
        this.world.setWeatherDuration(Integer.MAX_VALUE);
        this.world.setGameRuleValue("naturalRegeneration", "false");
        if(this.world.getName().equals("uhc")) {
            if(Game.getGame().getGameWorldType() == Type.FLAT_MINESHAFT ||
                    Game.getGame().getGameWorldType() == Type.FLAT_VILLAGE) {
                this.y = 50;
            } else {
                this.y = 150;
            }
            this.world.setGameRuleValue("doDaylightCycle", "false");
            this.world.setTime(6000);
            this.spawn.setWorld(this.world);
            this.spawn.setY(this.y);
            Game.getGame().getPlatform().createPlatform();
        }
        Location newWorldLoc = new Location(this.world, this.x, this.y + 5, this.z);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(newWorldLoc);
        }
        this.border.setWorldBorder(this.world);
    }

    public void newWorld(){
        switch(world.getEnvironment()){
            case NORMAL:
                switch(Game.getGame().getGameWorldType()){
                    case NORMAL:
                        newWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.NORMAL));
                        break;
                    case FLAT_VILLAGE:
                        newWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.FLAT).generatorSettings("3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;village(size=3 distance=9)"));
                        break;
                    case FLAT_MINESHAFT:
                        newWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.FLAT).generatorSettings("3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;mineshaft(chance=0.3)"));
                        break;
                }
                break;
            case NETHER:
                newWorld(new WorldCreator("uhc_nether").environment(World.Environment.NETHER).type(WorldType.NORMAL));
                break;
            case THE_END:
                newWorld(new WorldCreator("uhc_the_end").environment(World.Environment.THE_END).type(WorldType.NORMAL));
                break;
        }

    }

    public World getWorld() {
        return world;
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

    public UHCBorder getBorder() {
        return border;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public List<Location> getChunksLocation(){
        ArrayList<Location> chunksLocation = new ArrayList<>();
        int max = Game.getGame().getSettings().getSize() / 16 + Bukkit.getViewDistance();
        for(int x = -max; x <= max; x ++){
            for(int z = -max; z <= max; z ++){
                chunksLocation.add(new Location(world, x, 0.0D, z));
            }
        }
        return chunksLocation;
    }

    public void setType(Environment environment) {
        this.environment = environment;
    }

    public enum Environment {

        OVERWORLD("Overworld"),
        NETHER("Nether"),
        ENDER("Ender");

        private final String name;

        Environment(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Type {

        NORMAL,
        FLAT_VILLAGE,
        FLAT_MINESHAFT

    }

    public enum SettingType {

        LAPIS(World.Environment.NORMAL, Type.FLAT_VILLAGE),
        CAVE_ORES(World.Environment.NORMAL, Type.NORMAL);

        private final World.Environment environment;
        private final Type category;

        SettingType(World.Environment environment, Type category) {
            this.environment = environment;
            this.category = category;
        }

        public World.Environment getEnvironment() {
            return environment;
        }

        public Type getCategory() {
            return category;
        }
    }

}
