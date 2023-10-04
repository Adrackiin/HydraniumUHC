package fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.utils.MathUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drops {

    private final boolean uniqueDrop;
    private final ArrayList<HItem> items = new ArrayList<>();

    public Drops(ItemStack[] items, short[][] amount, ItemStack[] fortune, short[][][] amount2, ItemStack[] silk, short[][] amount3, ItemStack[] special, Material[][] breakSpecial, short[][] amount4, boolean uniqueDrop) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, fortune.length, silk.length, special.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(amount[i]);
            }
            if(fortune.length > i){
                item.setFortune(fortune[i]);
                item.setFortuneAmountChance(amount2[i]);
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(amount3[i]);
            }
            if(special.length > i){
                item.setSpecial(special[i]);
                item.setBreakSpecial(breakSpecial[i]);
                item.setSpecialAmountChance(amount4[i]);
            }
            this.items.add(item);
        }
        this.uniqueDrop = uniqueDrop;
    }

    public Drops(ItemStack[] items, short[][] amount, ItemStack[] fortune, short[][][] amount2, ItemStack[] silk, short[][] amount3, ItemStack[] special, Material[][] breakSpecial, short[][] amount4) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, fortune.length, silk.length, special.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(amount[i]);
            }
            if(fortune.length > i){
                item.setFortune(fortune[i]);
                item.setFortuneAmountChance(amount2[i]);
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(amount3[i]);
            }
            if(special.length > i){
                item.setSpecial(special[i]);
                item.setBreakSpecial(breakSpecial[i]);
                item.setSpecialAmountChance(amount4[i]);
            }
            this.items.add(item);
        }
        this.uniqueDrop = false;
    }

    public Drops(ItemStack[] items, short[][] amount, ItemStack[] fortune, short[][][] amount2, ItemStack[] silk, short[][] amount3, boolean uniqueDrop) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, fortune.length, silk.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(amount[i]);
            }
            if(fortune.length > i){
                item.setFortune(fortune[i]);
                item.setFortuneAmountChance(amount2[i]);
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(amount3[i]);
            }
            this.items.add(item);
        }
        this.uniqueDrop = uniqueDrop;
    }

    public Drops(ItemStack[] items, short[][] amount, ItemStack[] fortune, short[][][] amount2, ItemStack[] silk, short[][] amount3) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, fortune.length, silk.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(amount[i]);
            }
            if(fortune.length > i){
                item.setFortune(fortune[i]);
                item.setFortuneAmountChance(amount2[i]);
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(amount3[i]);
            }
            this.items.add(item);
        }
        this.uniqueDrop = false;
    }

    public Drops(ItemStack[] items) {
        for(byte i = 0; i < items.length; i ++){
            HItem item = new HItem();
            item.setItem(items[i]);
            item.setItemAmountChance(new short[]{10000});
            item.setFortune(items[i]);
            item.setFortuneAmountChance(new short[][]{{10000}, {10000}, {10000}});
            item.setSilk(items[i]);
            item.setSilkAmountChance(new short[]{10000});
            this.items.add(item);
        }
        this.uniqueDrop = false;
    }

    public Drops(ItemStack[] items, ItemStack[] silk) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, silk.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(new short[]{10000});
                item.setFortune(items[i]);
                item.setFortuneAmountChance(new short[][]{{10000}, {10000}, {10000}});
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(new short[]{10000});
            }
            this.items.add(item);
        }
        this.uniqueDrop = false;
    }

    public Drops(ItemStack[] items, ItemStack[] fortune, ItemStack[] silk) {
        for(byte i = 0; i < MathUtils.getMaxInt(Arrays.asList(items.length, fortune.length, silk.length)); i ++){
            HItem item = new HItem();
            if(items.length > i){
                item.setItem(items[i]);
                item.setItemAmountChance(new short[]{10000});
            }
            if(fortune.length > i){
                item.setFortune(fortune[i]);
                item.setFortuneAmountChance(new short[][]{{10000}, {10000}, {10000}});
            }
            if(silk.length > i){
                item.setSilk(silk[i]);
                item.setSilkAmountChance(new short[]{10000});
            }
            this.items.add(item);
        }
        this.uniqueDrop = false;
    }

    public List<ItemStack> getDrops(ItemStack playerItem){
        ArrayList<ItemStack> toReturn = new ArrayList<>();
        boolean stop = false;
        boolean special = false;
        for(HItem item : items){
            if(item.isSpecialDrops()){
                if(item.getBreakSpecial().contains(playerItem.getType())){
                    special = true;
                    for(short i = 0; i < item.getSpecialAmountChance().length; i ++){
                        if(item.getSpecialAmountChance()[i] == 10000){
                            toReturn.add(item.getSpecial());
                        } else {
                            if(UHCCore.getRandom().nextInt(10000) < item.getSpecialAmountChance()[i]){
                                toReturn.add(item.getSpecial());
                            }
                        }
                    }
                }
            }
        }
        if(special){
            return toReturn;
        }
        if(playerItem.hasItemMeta() && playerItem.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){
            for(HItem item : items) {
                if(item.isSilkTouch()) {
                    for(int i = 0; i < item.getSilkAmountChance().length; i++) {
                        if(item.getSilkAmountChance()[i] == 10000) {
                            toReturn.add(item.getSilk());
                            if(uniqueDrop) {
                                stop = true;
                            }
                        } else {
                            if(UHCCore.getRandom().nextInt(10000) < item.getSilkAmountChance()[i]) {
                                toReturn.add(item.getSilk());
                                if(uniqueDrop) {
                                    stop = true;
                                }
                            }
                        }
                    }
                    if(stop) {
                        break;
                    }
                }
            }
            return toReturn;
        }
        if(playerItem.hasItemMeta() && playerItem.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)){
            short e = (short)playerItem.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
            for(HItem item : items) {
                if(item.isFortune()) {
                    for(int i = 0; i < item.getFortuneAmountChance(HItem.EnchantmentLevel.valueOf("LEVEL_" + e)).length; i++) {
                        if(item.getFortuneAmountChance(HItem.EnchantmentLevel.valueOf("LEVEL_" + e))[i] == 10000) {
                            toReturn.add(item.getFortune());
                            if(uniqueDrop) {
                                stop = true;
                            }
                        } else {
                            if(UHCCore.getRandom().nextInt(10000) < item.getFortuneAmountChance(HItem.EnchantmentLevel.valueOf("LEVEL_" + e))[i]) {
                                toReturn.add(item.getFortune());
                                if(uniqueDrop) {
                                    stop = true;
                                }
                            }
                        }
                    }
                    if(stop) {
                        break;
                    }
                }
            }
            return toReturn;
        }
        for(HItem item : items) {
            if(item.isDefault()) {
                for(int i = 0; i < item.getItemAmountChance().length; i++) {
                    if(item.getItemAmountChance()[i] == 10000) {
                        toReturn.add(item.getItem());
                        if(uniqueDrop) {
                            stop = true;
                        }
                    } else {
                        if(UHCCore.getRandom().nextInt(10000) < item.getItemAmountChance()[i]) {
                            toReturn.add(item.getItem());
                            if(uniqueDrop) {
                                stop = true;
                            }
                        }
                    }
                }
                if(stop) {
                    break;
                }
            }
        }
        return toReturn;
    }

    public HItem getHItem(Material type){
        for(HItem item : items){
            if(item.getItem().getType() == type){
                return item;
            }
        }
        return null;
    }

    public byte getSlot(Material type){
        for(byte i = 0; i < items.size(); i ++){
            if(items.get(i).getItem().getType() == type){
                return i;
            }
        }
        return 0;
    }

    public void changeAmount(Material type, short[] amount){
        changeProperties(getHItem(type), null, amount, null, null);
    }

    public void changeFortune(Material type, short[][] amount){
        changeProperties(getHItem(type), null, null, null, amount);
    }

    public boolean isUniqueDrop() {
        return uniqueDrop;
    }

    private void changeProperties(HItem item, ItemStack itemStack, short[] amount, ItemStack itemStackFortune, short[][] amountFortune){
        if(itemStack != null)item.setItem(itemStack);
        if(amount != null)item.setItemAmountChance(amount);
        if(itemStackFortune != null)item.setFortune(itemStackFortune);
        if(amountFortune != null)item.setFortuneAmountChance(amountFortune);
    }
}