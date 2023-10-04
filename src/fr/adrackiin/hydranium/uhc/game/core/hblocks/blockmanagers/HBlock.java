package fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers;

import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.utils.minecraft.Items;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HBlock {

    private final Type type;
    private final List<Material> itemsBreak = new ArrayList<>();
    private final Type replace;
    private final EntityType entity;
    private Drops drops;

    public HBlock(Type type, Material[] itemsBreak, Drops drops, Type replace){
        this.type = type;
        this.itemsBreak.addAll(Collections.singletonList(itemsBreak));
        this.drops = drops;
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = replace;
        this.entity = null;
    }

    public HBlock(Type type, Material[] itemsBreak, Drops drops){
        this.type = type;
        this.itemsBreak.addAll(Collections.singletonList(itemsBreak));
        this.drops = drops;
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = Type.AIR;
        this.entity = null;
    }

    public HBlock(Type type, Material[] itemsBreak){
        this.type = type;
        this.itemsBreak.addAll(Collections.singletonList(itemsBreak));
        this.drops = new Drops(new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(type)}, new short[][][]{{{10000}, {10000}, {10000}}}, new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{}, new Material[][]{{}}, new short[][]{{}}, true);
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = Type.AIR;
        this.entity = null;
    }

    public HBlock(Type type, Drops drops){
        this.type = type;
        this.drops = drops;
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = Type.AIR;
        this.entity = null;
    }

    public HBlock(Type type, Type replace){
        this.type = type;
        this.drops = new Drops(new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(type)}, new short[][][]{{{10000}, {10000}, {10000}}}, new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{}, new Material[][]{{}}, new short[][]{{}}, true);
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = replace;
        this.entity = null;
    }

    public HBlock(Type type){
        this.type = type;
        this.drops = new Drops(new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{Items.simpleItem(type)}, new short[][][]{{{10000}, {10000}, {10000}}}, new ItemStack[]{Items.simpleItem(type)}, new short[][]{{10000}}, new ItemStack[]{}, new Material[][]{{}}, new short[][]{{}}, true);
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = Type.AIR;
        this.entity = null;
    }

    public HBlock(Type type, Drops drops, EntityType entity) {
        this.type = type;
        this.drops = drops;
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.entity = entity;
        this.replace = Type.AIR;
    }

    public HBlock(Type type, Drops drops, Type replace) {
        this.type = type;
        this.drops = drops;
        for(short data : type.getDatas()) {
            Game.getGame().getUHCManager().addUHCBlock(new TypeID(type.getType(), data), type, this);
        }
        this.replace = replace;
        this.entity = null;
    }

    public List<ItemStack> getDrops(ItemStack itemBreak) {
        if(itemsBreak.isEmpty() || itemsBreak.contains(itemBreak.getType())) {
            return drops.getDrops(itemBreak);
        }
        return Collections.singletonList(Items.simpleItem(Material.AIR));
    }

    public Type getType() {
        return type;
    }

    public List<Material> getItemsBreak() {
        return itemsBreak;
    }

    public Drops getDrops() {
        return drops;
    }

    public void setDrops(Drops drops) {
        this.drops = drops;
    }

    public Type getReplace() {
        return replace;
    }

    public EntityType getEntity() {
        return entity;
    }

    public enum Type {

        STONE(Material.STONE),
        GRANITE(Material.STONE, (short)1),
        POLISHED_GRANITE(Material.STONE, (short)2),
        DIORITE(Material.STONE, (short)3),
        POLISHED_DIORITE(Material.STONE, (short)4),
        ANDESITE(Material.STONE, (short)5),
        POLISHED_ANDESITE(Material.STONE, (short)6),
        GRASS(Material.GRASS),
        DIRT(Material.DIRT),
        COARSE_DIRT(Material.DIRT, (short)1),
        PODZOL(Material.DIRT, (short)2),
        COBBLESTONE(Material.COBBLESTONE),
        OAK_PLANK(Material.WOOD),
        SPRUCE_PLANK(Material.WOOD, (short)1),
        BIRCH_PLANK(Material.WOOD, (short)2),
        JUNGLE_PLANK(Material.WOOD, (short)3),
        ACACIA_PLANK(Material.WOOD, (short)4),
        DARK_PLANK(Material.WOOD, (short)5),
        OAK_SAPLING(Material.SAPLING),
        SPRUCE_SAPLING(Material.SAPLING, (short)1),
        BIRCH_SAPLING(Material.SAPLING, (short)2),
        JUNGLE_SAPLING(Material.SAPLING, (short)3),
        ACACIA_SAPLING(Material.SAPLING, (short)4),
        DARK_SAPLING(Material.SAPLING, (short)5),
        BEDROCK(Material.BEDROCK),
        FLOWING_WATER(Material.STATIONARY_WATER),
        WATER(Material.WATER),
        FLOWING_LAVA(Material.STATIONARY_LAVA),
        LAVA(Material.LAVA),
        SAND(Material.SAND),
        RED_SAND(Material.SAND, (short)1),
        GRAVEL(Material.GRAVEL),
        GOLD_ORE(Material.GOLD_ORE),
        IRON_ORE(Material.IRON_ORE),
        COAL_ORE(Material.COAL_ORE),
        OAK_WOOD(Material.LOG, new short[]{0, 4, 8}),
        SPRUCE_WOOD(Material.LOG, new short[]{1, 5, 9}),
        BIRCH_WOOD(Material.LOG, new short[]{2, 6, 10}),
        JUNGLE_WOOD(Material.LOG, new short[]{3, 7, 11}),
        OAK_LEAVES(Material.LEAVES, new short[]{0, 4, 8, 12}),
        SPRUCE_LEAVES(Material.LEAVES, new short[]{1, 5, 9, 13}),
        BIRCH_LEAVES(Material.LEAVES, new short[]{2, 6, 10 ,14}),
        JUNGLE_LEAVES(Material.LEAVES, new short[]{3, 7, 11, 15}),
        SPONGE(Material.SPONGE),
        WET_SPONGE(Material.SPONGE, (short)1),
        GLASS(Material.GLASS),
        LAPIS_ORE(Material.LAPIS_ORE),
        LAPIS_BLOCK(Material.LAPIS_BLOCK),
        DISPENSER(Material.DISPENSER, new short[]{0, 1, 2, 3, 4, 5}),
        SANDSTONE(Material.SANDSTONE),
        CHISELED_SANDSTONE(Material.SANDSTONE, (short)1),
        SMOOTH_SANDSTONE(Material.SANDSTONE, (short)2),
        NOTE_BLOCK(Material.NOTE_BLOCK),
        BED(Material.BED_BLOCK, new short[]{0, 1, 2, 3}),
        BED_TOP(Material.BED_BLOCK, new short[]{8, 9, 10, 11}),
        POWERED_RAIL(Material.POWERED_RAIL, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        DETECTOR_RAIL(Material.DETECTOR_RAIL, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        STICKY_PISTON(Material.PISTON_STICKY_BASE, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        COBWEB(Material.WEB),
        SHRUB(Material.LONG_GRASS),
        TALL_GRASS(Material.LONG_GRASS, (short)1),
        FERN(Material.LONG_GRASS, (short)2),
        DEAD_BUSH(Material.DEAD_BUSH),
        PISTON(Material.PISTON_BASE, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        PISTON_HEAD(Material.PISTON_EXTENSION, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        WHITE_WOOL(Material.WOOL),
        ORANGE_WOOL(Material.WOOL, (short)1),
        MAGENTA_WOOL(Material.WOOL, (short)2),
        LIGHT_BLUE_WOOL(Material.WOOL, (short)3),
        YELLOW_WOOL(Material.WOOL, (short)4),
        LIME_WOOL(Material.WOOL, (short)5),
        PINK_WOOL(Material.WOOL, (short)6),
        GRAY_WOOL(Material.WOOL, (short)7),
        LIGHT_GRAY_WOOL(Material.WOOL, (short)8),
        CYAN_WOOL(Material.WOOL, (short)9),
        PURPLE_WOOL(Material.WOOL, (short)10),
        BLUE_WOOL(Material.WOOL, (short)11),
        BROWN_WOOL(Material.WOOL, (short)12),
        GREEN_WOOL(Material.WOOL, (short)13),
        RED_WOOL(Material.WOOL, (short)14),
        BLACK_WOOL(Material.WOOL, (short)15),
        DANDELION(Material.YELLOW_FLOWER),
        POPPY(Material.RED_ROSE),
        BLUE_ORCHID(Material.RED_ROSE, (short)1),
        ALIUM(Material.RED_ROSE, (short)2),
        AZURE_BLUET(Material.RED_ROSE, (short)3),
        RED_TULIP(Material.RED_ROSE, (short)4),
        ORANGE_TULIP(Material.RED_ROSE, (short)5),
        WHITE_TULIP(Material.RED_ROSE, (short)6),
        PINK_TULIP(Material.RED_ROSE, (short)7),
        OXEYE_DAISY(Material.RED_ROSE, (short)8),
        BROWN_MUSHROOM(Material.BROWN_MUSHROOM),
        RED_MUSHROOM(Material.RED_MUSHROOM),
        GOLD_BLOCK(Material.GOLD_BLOCK),
        IRON_BLOCK(Material.IRON_BLOCK),
        DOUBLE_STONE_SLAB(Material.DOUBLE_STEP),
        DOUBLE_SANDSTONE_SLAB(Material.DOUBLE_STEP, (short)1),
        DOUBLE_WOODEN_SLAB(Material.DOUBLE_STEP, (short)3),
        DOUBLE_COBBLESTONE_SLAB(Material.DOUBLE_STEP, (short)3),
        DOUBLE_BRICK_SLAB(Material.DOUBLE_STEP, (short)4),
        DOUBLE_STONEBRICK_SLAB(Material.DOUBLE_STEP, (short)5),
        DOUBLE_NETHERBRICK_SLAB(Material.DOUBLE_STEP, (short)6),
        DOUBLE_QUARTZ_SLAB(Material.DOUBLE_STEP, (short)7),
        STONE_SLAB(Material.STEP, new short[]{0, 8}),
        SANDSTONE_SLAB(Material.STEP, new short[]{1, 9}),
        COBBLESTONE_SLAB(Material.STEP, new short[]{3, 11}),
        BRICK_SLAB(Material.STEP, new short[]{4, 12}),
        STONEBRICK_SLAB(Material.STEP, new short[]{5, 13}),
        NETHERBRICK_SLAB(Material.STEP, new short[]{6, 14}),
        QUARTZ_SLAB(Material.STEP, new short[]{7, 15}),
        BRICK(Material.BRICK),
        TNT(Material.TNT),
        BOOKSHELF(Material.BOOKSHELF),
        MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE),
        OBSIDIAN(Material.OBSIDIAN),
        TORCH(Material.TORCH, new short[]{1, 2, 3, 4, 5}),
        FIRE(Material.FIRE),
        MOB_SPAWNER(Material.MOB_SPAWNER),
        OAK_PLANK_STAIR(Material.WOOD_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        COBBLESTONE_STAIR(Material.COBBLESTONE_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        BRICK_STAIR(Material.BRICK_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        STONEBRICK_STAIR(Material.SMOOTH_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        CHEST(Material.CHEST, new short[]{2, 3, 4, 5}),
        REDSTONE_WIRE(Material.REDSTONE_WIRE, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        DIAMOND_ORE(Material.DIAMOND_ORE),
        DIAMOND_BLOCK(Material.DIAMOND_BLOCK),
        CRAFTING_TABLE(Material.WORKBENCH),
        WHEAT_CROPS(Material.CROPS, new short[]{0, 1, 2, 3, 4, 5, 6}),
        WHEAT_CROPS_MATURE(Material.CROPS, (short)7),
        FARMLAND(Material.SOIL, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        FURNACE(Material.FURNACE, new short[]{2, 3, 4, 5}),
        BURNING_FURNACE(Material.BURNING_FURNACE, new short[]{2, 3, 4, 5}),
        GROUND_SIGN(Material.SIGN_POST, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        WALL_SIGN(Material.WALL_SIGN, new short[]{2, 3, 4, 5}),
        OAK_DOOR(Material.WOODEN_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        OAK_DOOR_TOP(Material.WOODEN_DOOR, (short)8),
        IRON_DOOR(Material.IRON_DOOR_BLOCK, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        IRON_DOOR_TOP(Material.IRON_DOOR_BLOCK, (short)8),
        LADDER(Material.LADDER, new short[]{2, 3, 4, 5}),
        RAIL(Material.RAILS, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}),
        LEVER(Material.LEVER, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ,15}),
        STONE_PRESSURE_PLATE(Material.STONE_PLATE, new short[]{0, 1}),
        OAK_PRESSURE_PLATE(Material.WOOD_PLATE, new short[]{0, 1}),
        REDSTONE_ORE(Material.REDSTONE_ORE),
        GLOWING_REDSTONE_ORE(Material.GLOWING_REDSTONE_ORE),
        REDSTONE_TORCH_ON(Material.REDSTONE_TORCH_ON, new short[]{1, 2, 3, 4, 5}),
        REDSTONE_TORCH_OFF(Material.REDSTONE_TORCH_OFF, new short[]{1, 2, 3, 4, 5}),
        STONE_BUTTON(Material.STONE_BUTTON, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        SNOW_LAYER(Material.SNOW, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        SNOW_BLOCK(Material.SNOW_BLOCK),
        ICE(Material.ICE),
        CACTUS(Material.CACTUS),
        CLAY(Material.CLAY),
        SUGAR_CANE(Material.SUGAR_CANE_BLOCK),
        JUKEBOX(Material.JUKEBOX, new short[]{0, 1}),
        OAK_FENCE(Material.FENCE),
        PUMPKIN(Material.PUMPKIN, new short[]{0, 1, 2, 3}),
        NETHERRACK(Material.NETHERRACK),
        SOUL_SAND(Material.SOUL_SAND),
        GLOWSTONE(Material.GLOWSTONE),
        NETHER_PORTAL(Material.PORTAL),
        GLOWING_PUMPKIN(Material.JACK_O_LANTERN, new short[]{0, 1, 2, 3}),
        CAKE(Material.CAKE_BLOCK, new short[]{0, 1, 2, 3, 4, 5, 6}),
        REDSTONE_REPEATER_ON(Material.DIODE_BLOCK_ON, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        REDSTONE_REPEATER_OFF(Material.DIODE_BLOCK_OFF, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        WHITE_GLASS(Material.STAINED_GLASS),
        ORANGE_GLASS(Material.STAINED_GLASS, (short)1),
        MAGENTA_GLASS(Material.STAINED_GLASS, (short)2),
        LIGHT_BLUE_GLASS(Material.STAINED_GLASS, (short)3),
        YELLOW_GLASS(Material.STAINED_GLASS, (short)4),
        LIME_GLASS(Material.STAINED_GLASS, (short)5),
        PINK_GLASS(Material.STAINED_GLASS, (short)6),
        GRAY_GLASS(Material.STAINED_GLASS, (short)7),
        LIGHT_GRAY_GLASS(Material.STAINED_GLASS, (short)8),
        CYAN_GLASS(Material.STAINED_GLASS, (short)9),
        PURPLE_GLASS(Material.STAINED_GLASS, (short)10),
        BLUE_GLASS(Material.STAINED_GLASS, (short)11),
        BROWN_GLASS(Material.STAINED_GLASS, (short)12),
        GREEN_GLASS(Material.STAINED_GLASS, (short)13),
        RED_GLASS(Material.STAINED_GLASS, (short)14),
        BLACK_GLASS(Material.STAINED_GLASS, (short)15),
        OAK_TRAPDOOR(Material.TRAP_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ,15}),
        STONE_SILVERFISH(Material.MONSTER_EGGS),
        COBBLESTONE_SILVERFISH(Material.MONSTER_EGGS, (short)1),
        STONEBRICK_SILVERFISH(Material.MONSTER_EGGS, (short)2),
        MOSSY_STONEBRICK_SILVERFISH(Material.MONSTER_EGGS, (short)3),
        CRACKED_STONEBRICK_SILVERFISH(Material.MONSTER_EGGS, (short)4),
        CHISELED_STONEBRICK_SILVERFISH(Material.MONSTER_EGGS, (short)5),
        STONEBRICK(Material.SMOOTH_BRICK),
        MOSSY_STONEBRICK(Material.SMOOTH_BRICK, (short)1),
        CRACKED_STONEBRICK(Material.SMOOTH_BRICK, (short)2),
        CHISELED_STONEBRICK(Material.SMOOTH_BRICK, (short)3),
        GIANT_BROWN_MUSHROOM(Material.HUGE_MUSHROOM_1, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        GIANT_RED_MUSHROOM(Material.HUGE_MUSHROOM_2, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        IRON_BARS(Material.IRON_FENCE),
        GLASS_PANE(Material.THIN_GLASS),
        MELON(Material.MELON_BLOCK),
        MELON_CROPS(Material.MELON_STEM, new short[]{0, 1, 2, 3, 4, 5, 6}),
        MELON_CROPS_MATURE(Material.MELON_STEM, (short)7),
        PUMPKIN_CROPS(Material.PUMPKIN_STEM, new short[]{0, 1, 2, 3, 4, 5, 6}),
        PUMPKIN_CROPS_MATURE(Material.PUMPKIN_STEM, (short)7),
        VINE(Material.VINE, new short[]{1, 2, 4, 8}),
        OAK_FENCE_GATE(Material.FENCE_GATE, new short[]{2, 3, 4, 5}),
        MYCELIUM(Material.MYCEL),
        LILYPAD(Material.WATER_LILY),
        NETHER_BRICK(Material.NETHER_BRICK),
        NETHER_BRICK_FENCE(Material.NETHER_FENCE),
        NETHER_BRICK_STAIR(Material.NETHER_BRICK_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        NETHER_WART(Material.NETHER_WARTS, new short[]{0, 1, 2}),
        NETHER_WART_MATURE(Material.NETHER_WARTS, (short)3),
        ENCHANTMENT_TABLE(Material.ENCHANTMENT_TABLE),
        BREWING_STAND(Material.BREWING_STAND, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        CAULDRON(Material.CAULDRON, new short[]{0, 1, 2, 3}),
        END_PORTAL(Material.ENDER_PORTAL),
        END_STONE(Material.ENDER_STONE),
        END_PORTAL_FRAME(Material.ENDER_PORTAL_FRAME, new short[]{0, 1, 2, 3}),
        DRAGON_EGG(Material.DRAGON_EGG),
        REDSTONE_LAMP_ON(Material.REDSTONE_LAMP_ON),
        REDSTONE_LAMP_OFF(Material.REDSTONE_LAMP_OFF),
        DOUBLE_OAK_SLAB(Material.WOOD_DOUBLE_STEP),
        DOUBLE_SPRUCE_SLAB(Material.WOOD_DOUBLE_STEP, (short)1),
        DOUBLE_BIRCH_SLAB(Material.WOOD_DOUBLE_STEP, (short)2),
        DOUBLE_JUNGLE_SLAB(Material.WOOD_DOUBLE_STEP, (short)3),
        DOUBLE_ACACIA_SLAB(Material.WOOD_DOUBLE_STEP, (short)4),
        DOUBLE_DARK_SLAB(Material.WOOD_DOUBLE_STEP, (short)5),
        OAK_SLAB(Material.WOOD_STEP, new short[]{0, 8}),
        SPRUCE_SLAB(Material.WOOD_STEP, new short[]{1, 9}),
        BIRCH_SLAB(Material.WOOD_STEP, new short[]{2, 10}),
        JUNGLE_SLAB(Material.WOOD_STEP, new short[]{3, 11}),
        ACACIA_SLAB(Material.WOOD_STEP, new short[]{4, 12}),
        DARK_SLAB(Material.WOOD_STEP, new short[]{5, 13}),
        COCOA(Material.COCOA, new short[]{0, 1, 3, 4, 6, 7, 9, 10}),
        COCOA_MATURE(Material.COCOA, new short[]{2, 5, 8, 11}),
        SANDSTONE_STAIR(Material.SANDSTONE_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        EMERALD_ORE(Material.EMERALD_ORE),
        ENDER_CHEST(Material.ENDER_CHEST, new short[]{2, 3, 4, 5}),
        STRING_WIRE_HOOK(Material.TRIPWIRE_HOOK, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 12, 13, 14, 15}),
        STRING_WIRE(Material.TRIPWIRE, new short[]{0, 1, 2, 3}),
        EMERALD_BLOCK(Material.EMERALD_BLOCK),
        SPRUCE_STAIR(Material.SPRUCE_WOOD_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        BIRCH_STAIR(Material.BIRCH_WOOD_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        JUNGLE_STAIR(Material.JUNGLE_WOOD_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        COMMAND_BLOCK(Material.COMMAND),
        BEACON(Material.BEACON),
        COBBLESTONE_WALL(Material.COBBLE_WALL),
        MOSSY_COBBLESTONE_WALL(Material.COBBLE_WALL, (short)1),
        FLOWER_POT(Material.FLOWER_POT),
        CARROT_CROPS(Material.CARROT, new short[]{0, 1, 2, 3, 4, 5, 6}),
        CARROT_CROPS_MATURE(Material.CARROT, (short)7),
        POTATO_CROPS(Material.POTATO, new short[]{0, 1, 2, 3, 4, 5, 6}),
        POTATO_CROPS_MATURE(Material.POTATO, (short)7),
        OAK_BUTTON(Material.WOOD_BUTTON, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        HEAD(Material.SKULL, new short[]{1, 2, 3, 4, 5}),
        ANVIL(Material.ANVIL, new short[]{0, 1, 2, 3}),
        ANVIL_DAMAGED(Material.ANVIL, new short[]{4, 5, 6, 7}),
        ANVIL_VERY_DAMAGED(Material.ANVIL, new short[]{8, 9, 10, 11}),
        TRAPPED_CHEST(Material.TRAPPED_CHEST, new short[]{2, 3, 4, 5}),
        GOLD_PRESSURE_PLATE(Material.GOLD_PLATE, new short[]{0, 1}),
        IRON_PRESSURE_PLATE(Material.IRON_PLATE, new short[]{0, 1}),
        REDSTONE_COMPARATOR_ON(Material.REDSTONE_COMPARATOR_ON),
        REDSTONE_COMPARATOR_OFF(Material.REDSTONE_COMPARATOR_OFF, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        DAYLIGHT_SENSOR(Material.DAYLIGHT_DETECTOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ,15}),
        REDSTONE_BLOCK(Material.REDSTONE_BLOCK, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        QUARTZ_ORE(Material.QUARTZ_ORE),
        HOPPER(Material.HOPPER, new short[]{0, 2, 3, 4, 5}),
        QUARTZ(Material.QUARTZ_BLOCK),
        CHISELED_QUARTZ(Material.QUARTZ_BLOCK, (short)1),
        PILLAR_QUARTZ(Material.QUARTZ_BLOCK, (short)2),
        QUARTZ_STAIR(Material.QUARTZ_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        ACTIVATOR_RAIL(Material.ACTIVATOR_RAIL, new short[]{0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13}),
        DROPPER(Material.DROPPER, new short[]{0, 1, 2, 3, 4, 5}),
        WHITE_HARDENED_CLAY(Material.STAINED_CLAY),
        ORANGE_HARDENED_CLAY(Material.STAINED_CLAY, (short)1),
        MAGENTA_HARDENED_CLAY(Material.STAINED_CLAY, (short)2),
        LIGHT_BLUE_HARDENED_CLAY(Material.STAINED_CLAY, (short)3),
        YELLOW_HARDENED_CLAY(Material.STAINED_CLAY, (short)4),
        LIME_HARDENED_CLAY(Material.STAINED_CLAY, (short)5),
        PINK_HARDENED_CLAY(Material.STAINED_CLAY, (short)6),
        GRAY_HARDENED_CLAY(Material.STAINED_CLAY, (short)7),
        LIGHT_GRAY_HARDENED_CLAY(Material.STAINED_CLAY, (short)8),
        CYAN_HARDENED_CLAY(Material.STAINED_CLAY, (short)9),
        PURPLE_HARDENED_CLAY(Material.STAINED_CLAY, (short)10),
        BLUE_HARDENED_CLAY(Material.STAINED_CLAY, (short)11),
        BROWN_HARDENED_CLAY(Material.STAINED_CLAY, (short)12),
        GREEN_HARDENED_CLAY(Material.STAINED_CLAY, (short)13),
        RED_HARDENED_CLAY(Material.STAINED_CLAY, (short)14),
        BLACK_HARDENED_CLAY(Material.STAINED_CLAY, (short)15),
        WHITE_GLASS_PANE(Material.STAINED_GLASS_PANE),
        ORANGE_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)1),
        MAGENTA_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)2),
        LIGHT_BLUE_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)3),
        YELLOW_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)4),
        LIME_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)5),
        PINK_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)6),
        GRAY_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)7),
        LIGHT_GRAY_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)8),
        CYAN_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)9),
        PURPLE_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)10),
        BLUE_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)11),
        BROWN_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)12),
        GREEN_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)13),
        RED_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)14),
        BLACK_GLASS_PANE(Material.STAINED_GLASS_PANE, (short)15),
        ACACIA_LEAVES(Material.LEAVES_2, new short[]{0, 4, 8, 12}),
        DARK_LEAVES(Material.LEAVES_2, new short[]{1, 5, 9, 13}),
        ACACIA_WOOD(Material.LOG_2, new short[]{0, 4, 8}),
        DARK_WOOD(Material.LOG_2, new short[]{1, 5, 9}),
        ACACIA_STAIR(Material.ACACIA_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        DARK_STAIR(Material.DARK_OAK_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        SLIME(Material.SLIME_BLOCK),
        BARRIER(Material.BARRIER),
        IRON_TRAPDOOR(Material.IRON_TRAPDOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        PRISMARINE(Material.PRISMARINE),
        PRISMARINE_BRICK(Material.PRISMARINE, (short)1),
        PRISMARINE_DARK(Material.PRISMARINE, (short)2),
        SEA_LANTERN(Material.SEA_LANTERN),
        HAY(Material.HAY_BLOCK, new short[]{0, 4, 8}),
        WHITE_CARPET(Material.CARPET),
        ORANGE_CARPET(Material.CARPET, (short)1),
        MAGENTA_CARPET(Material.CARPET, (short)2),
        LIGHT_BLUE_CARPET(Material.CARPET, (short)3),
        YELLOW_CARPET(Material.CARPET, (short)4),
        LIME_CARPET(Material.CARPET, (short)5),
        PINK_CARPET(Material.CARPET, (short)6),
        GRAY_CARPET(Material.CARPET, (short)7),
        LIGHT_GRAY_CARPET(Material.CARPET, (short)8),
        CYAN_CARPET(Material.CARPET, (short)9),
        PURPLE_CARPET(Material.CARPET, (short)10),
        BLUE_CARPET(Material.CARPET, (short)11),
        BROWN_CARPET(Material.CARPET, (short)12),
        GREEN_CARPET(Material.CARPET, (short)13),
        RED_CARPET(Material.CARPET, (short)14),
        BLACK_CARPET(Material.CARPET, (short)15),
        HARDENED_CLAY(Material.HARD_CLAY),
        COAL_BLOCK(Material.COAL_BLOCK),
        PACKED_ICE(Material.PACKED_ICE),
        SUNFLOWER(Material.DOUBLE_PLANT),
        LILAC(Material.DOUBLE_PLANT, (short)1),
        HUGE_GRASS(Material.DOUBLE_PLANT, (short)2),
        HUGE_FERN(Material.DOUBLE_PLANT, (short)3),
        ROSE_BUSH(Material.DOUBLE_PLANT, (short)4),
        PEONY_BUSH(Material.DOUBLE_PLANT, (short)5),
        DOUBLE_PLANT_TECHNICAL(Material.DOUBLE_PLANT, (short)10),
        GROUND_BANNER(Material.STANDING_BANNER, new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}),
        WALL_BANNER(Material.WALL_BANNER, new short[]{2, 3, 4, 5}),
        MOON_SENSOR(Material.DAYLIGHT_DETECTOR_INVERTED),
        RED_SANDSTONE(Material.RED_SANDSTONE),
        CHISELED_RED_SANDSTONE(Material.RED_SANDSTONE, (short)1),
        SMOOTH_RED_SANDSTONE(Material.RED_SANDSTONE, (short)2),
        RED_SANDSTONE_STAIR(Material.RED_SANDSTONE_STAIRS, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        DOUBLE_RED_SANDSTONE_SLAB(Material.DOUBLE_STONE_SLAB2),
        RED_SANDSTONE_SLAB(Material.STONE_SLAB2),
        SPRUCE_FENCE(Material.SPRUCE_FENCE),
        SPRUCE_FENCE_GATE(Material.SPRUCE_FENCE_GATE, new short[]{2, 3, 4, 5}),
        BIRCH_FENCE(Material.BIRCH_FENCE),
        BIRCH_FENCE_GATE(Material.BIRCH_FENCE_GATE, new short[]{2, 3, 4, 5}),
        JUNGLE_FENCE(Material.JUNGLE_FENCE),
        JUNGLE_FENCE_GATE(Material.JUNGLE_FENCE_GATE, new short[]{2, 3, 4, 5}),
        DARK_FENCE(Material.DARK_OAK_FENCE),
        DARK_FENCE_GATE(Material.DARK_OAK_FENCE_GATE, new short[]{2, 3, 4, 5}),
        ACACIA_FENCE(Material.ACACIA_FENCE),
        ACACIA_FENCE_GATE(Material.ACACIA_FENCE_GATE, new short[]{2, 3, 4, 5}),
        SPRUCE_DOOR(Material.SPRUCE_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        SPRUCE_DOOR_TOP(Material.SPRUCE_DOOR, (short)8),
        BIRCH_DOOR(Material.BIRCH_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        BIRCH_DOOR_TOP(Material.BIRCH_DOOR, (short)8),
        JUNGLE_DOOR(Material.JUNGLE_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        JUNGLE_DOOR_TOP(Material.JUNGLE_DOOR, (short)8),
        ACACIA_DOOR(Material.ACACIA_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        ACACIA_DOOR_TOP(Material.ACACIA_DOOR, (short)8),
        DARK_DOOR(Material.DARK_OAK_DOOR, new short[]{0, 1, 2, 3, 4, 5, 6, 7}),
        DARK_DOOR_TOP(Material.DARK_OAK_DOOR, (short)8),
        AIR(Material.AIR);

        private final Material type;
        private final short[] data;

        Type(Material type, short[] data){
            this.type = type;
            this.data = data;
        }

        Type(Material type, short data){
            this.type = type;
            this.data = new short[]{data};
        }

        Type(Material type){
            this.type = type;
            this.data = new short[]{0};
        }

        public Material getType() {
            return type;
        }

        public short[] getDatas() {
            return data;
        }
    }

}
