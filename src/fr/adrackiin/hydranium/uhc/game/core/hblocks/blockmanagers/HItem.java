package fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HItem {

    private ItemStack item;
    private short[] itemAmountChance = new short[]{};
    private ItemStack fortune;
    private short[][] fortuneAmountChance = new short[][]{};
    private ItemStack silk;
    private short[] silkAmountChance = new short[]{};
    private ItemStack special;
    private Material[] breakSpecial = new Material[]{};
    private short[] specialAmountChance = new short[]{};

    public HItem() {}

    public short[] getFortuneAmountChance(EnchantmentLevel level) {
        return fortuneAmountChance[level.getLevel()];
    }

    public short[][] getFortuneAmountChance() {
        return fortuneAmountChance;
    }

    public void setFortuneAmountChance(short[][] fortuneAmountChance) {
        this.fortuneAmountChance = fortuneAmountChance;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public short[] getItemAmountChance() {
        return itemAmountChance;
    }

    public void setItemAmountChance(short[] itemAmountChance) {
        this.itemAmountChance = itemAmountChance;
    }

    public ItemStack getFortune() {
        return fortune;
    }

    public ItemStack getSilk() {
        return silk;
    }

    public void setSilk(ItemStack silk) {
        this.silk = silk;
    }

    public short[] getSilkAmountChance() {
        return silkAmountChance;
    }

    public void setSilkAmountChance(short[] silkAmountChance) {
        this.silkAmountChance = silkAmountChance;
    }

    public ItemStack getSpecial() {
        return special;
    }

    public void setSpecial(ItemStack special) {
        this.special = special;
    }

    public List<Material> getBreakSpecial() {
        return Collections.singletonList(breakSpecial);
    }

    public void setBreakSpecial(Material[] breakSpecial) {
        this.breakSpecial = breakSpecial;
    }

    public short[] getSpecialAmountChance() {
        return specialAmountChance;
    }

    public void setSpecialAmountChance(short[] specialAmountChance) {
        this.specialAmountChance = specialAmountChance;
    }

    public boolean isDefault(){
        return itemAmountChance.length != 0;
    }

    public boolean isFortune(){
        return fortuneAmountChance.length != 0;
    }

    public void setFortune(ItemStack fortune) {
        this.fortune = fortune;
    }

    public boolean isSilkTouch(){
        return silkAmountChance.length != 0;
    }

    public boolean isSpecialDrops(){
        return specialAmountChance.length != 0;
    }

    public enum EnchantmentLevel {

        LEVEL_1((short) 0),
        LEVEL_2((short) 1),
        LEVEL_3((short) 2);

        private final short level;

        EnchantmentLevel(short level) {
            this.level = level;
        }

        public short getLevel() {
            return level;
        }
    }
}
