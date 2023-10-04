package fr.adrackiin.hydranium.uhc.utils.minecraft;

import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class Items {

    private static final List<Material> tools = Arrays.asList(Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_SWORD, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_SWORD, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_SWORD, Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_SWORD, Material.SHEARS);
    private static final List<Material> shearsBreak = Arrays.asList(Material.WEB, Material.LEAVES, Material.LEAVES_2, Material.DOUBLE_PLANT, Material.LONG_GRASS, Material.WOOL, Material.TRIPWIRE);
    private static final Material[] pickaxe = {Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE};

    public static ItemStack getItem(Material material, String name, int amount, int dataValue, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short)dataValue);
        ItemMeta metaItem = item.getItemMeta();
        metaItem.setDisplayName(name);
        metaItem.setLore(lore);
        item.setItemMeta(metaItem);
        return item;
    }

    public static ItemStack getHead(String name, int amount, List<String> lore, String player){

        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte)3);
        SkullMeta metaSkull = (SkullMeta)item.getItemMeta();
        metaSkull.setDisplayName(name);
        metaSkull.setOwner(player);
        metaSkull.setLore(lore);
        item.setItemMeta(metaSkull);

        return item;
    }

    public static ItemStack simpleItem(Material material, int data, byte amount){
        return new ItemStack(material, amount, (short)data);
    }

    public static ItemStack simpleItem(Material material, int data){
        return new ItemStack(material, 1, (short)data);
    }

    public static ItemStack simpleItem(Material material){
        return new ItemStack(material, 1, (short)0);
    }

    public static ItemStack simpleItem(HBlock.Type type){
        return new ItemStack(type.getType(), 1, type.getDatas()[0]);
    }

    public static ItemStack simpleItem(HBlock.Type type, byte amount){
        return new ItemStack(type.getType(), amount, type.getDatas()[0]);
    }

    public static ItemStack simplePotion(int data){
        return new ItemStack(Material.POTION, 1, (byte)data);
    }

    public static void addGlowingEffect(ItemStack item){
        if(item.getType() == Material.GOLDEN_APPLE){
            item.setDurability((short)1);
            return;
        }
        ItemMeta toolMeta = item.getItemMeta();
        toolMeta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
        if(!toolMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            toolMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(toolMeta);
    }

    public static void removeGlowingEffect(ItemStack item){
        if(item.getType() == Material.GOLDEN_APPLE){
            item.setDurability((short)0);
            return;
        }
        ItemMeta toolMeta = item.getItemMeta();
        toolMeta.removeEnchant(Enchantment.ARROW_INFINITE);
        item.setItemMeta(toolMeta);
    }

    public static ItemStack enchantedBook(Enchantment enchantment, int level){
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = book.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        book.setItemMeta(itemMeta);
        return book;
    }

    public static boolean isTool(Material type){
        return tools.contains(type);
    }

    public static boolean canShearsBreak(Material type){
        return shearsBreak.contains(type);
    }

    public static void breakItem(ItemStack tool, Player player) {
        tool.setType(Material.AIR);
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
    }

    public static Material[] getPickaxe() {
        return pickaxe;
    }
}
