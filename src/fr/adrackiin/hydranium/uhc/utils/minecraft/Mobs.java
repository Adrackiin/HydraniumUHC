package fr.adrackiin.hydranium.uhc.utils.minecraft;

import fr.adrackiin.hydranium.api.utils.APList;
import org.bukkit.entity.EntityType;

public class Mobs {

    private static final APList<EntityType> animals = new APList<>();
    private static final APList<EntityType> monsters = new APList<>();
    private static final APList<EntityType> venimous = new APList<>();

    static {
        animals.add(EntityType.BAT);
        animals.add(EntityType.PIG);
        animals.add(EntityType.SHEEP);
        animals.add(EntityType.COW);
        animals.add(EntityType.CHICKEN);
        animals.add(EntityType.SQUID);
        animals.add(EntityType.WOLF);
        animals.add(EntityType.MUSHROOM_COW);
        animals.add(EntityType.SNOWMAN);
        animals.add(EntityType.OCELOT);
        animals.add(EntityType.IRON_GOLEM);
        animals.add(EntityType.HORSE);
        animals.add(EntityType.RABBIT);
        animals.add(EntityType.VILLAGER);
        monsters.add(EntityType.CREEPER);
        monsters.add(EntityType.SKELETON);
        monsters.add(EntityType.SPIDER);
        monsters.add(EntityType.CAVE_SPIDER);
        monsters.add(EntityType.GIANT);
        monsters.add(EntityType.ZOMBIE);
        monsters.add(EntityType.SLIME);
        monsters.add(EntityType.GHAST);
        monsters.add(EntityType.PIG_ZOMBIE);
        monsters.add(EntityType.ENDERMAN);
        monsters.add(EntityType.SILVERFISH);
        monsters.add(EntityType.BLAZE);
        monsters.add(EntityType.MAGMA_CUBE);
        monsters.add(EntityType.ENDER_DRAGON);
        monsters.add(EntityType.WITHER);
        monsters.add(EntityType.WITCH);
        monsters.add(EntityType.ENDERMITE);
        monsters.add(EntityType.GUARDIAN);
        venimous.add(EntityType.CAVE_SPIDER);
        venimous.add(EntityType.WITCH);
    }

    public static boolean isAnimal(EntityType e){
        return animals.contains(e);
    }

    public static boolean isMonster(EntityType e){
        return monsters.contains(e);
    }

    public static boolean isVenimous(EntityType e){
        return venimous.contains(e);
    }

}
