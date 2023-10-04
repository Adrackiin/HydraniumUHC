package fr.adrackiin.hydranium.uhc.guis.host.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGNether extends APGuiListener {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGNether() {
        back.put((byte)10, true);
        back.put((byte)11, true);
        back.put((byte)12, false);
        back.put((byte)13, true);
        back.put((byte)14, true);
        back.put((byte)16, true);
        back.put((byte)25, true);
        back.put((byte)34, true);
        back.put((byte)43, true);
        back.put((byte)28, true);
        back.put((byte)29, true);
        back.put((byte)30, true);
        back.put((byte)31, true);
        back.put((byte)32, true);
        back.put((byte)37, true);
        back.put((byte)38, true);
        back.put((byte)39, true);
        back.put((byte)40, true);
        back.put((byte)41, true);
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cOptions").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cNether", (byte)6, new APGuiItem[]{
                new APGuiItem(Material.NETHERRACK, (short)0, (byte)1, (byte)10, get((byte)10, back.get((byte)10) ? "§a" : "§c"), getDesc((byte)10)),
                new APGuiItem(Material.OBSIDIAN, (short)0, (byte)1, (byte)11, get((byte)11, back.get((byte)11) ? "§a" : "§c"), getDesc((byte)11)),
                new APGuiItem(Material.QUARTZ_ORE, (short)0, (byte)1, (byte)12, get((byte)12, back.get((byte)12) ? "§a" : "§c"), getDesc((byte)12)),
                new APGuiItem(Material.FLINT_AND_STEEL, (short)0, (byte)1, (byte)13, get((byte)13, back.get((byte)13) ? "§a" : "§c"), getDesc((byte)13)),
                new APGuiItem(Material.LAVA_BUCKET, (short)0, (byte)1, (byte)14, get((byte)14, back.get((byte)14) ? "§a" : "§c"), getDesc((byte)14)),
                new APGuiItem(Material.MONSTER_EGG, (short)57, (byte)1, (byte)16, get((byte)16, back.get((byte)16) ? "§a" : "§c"), getDesc((byte)16)),
                new APGuiItem(Material.MONSTER_EGG, (short)61, (byte)1, (byte)25, get((byte)25, back.get((byte)25) ? "§a" : "§c"), getDesc((byte)25)),
                new APGuiItem(Material.MONSTER_EGG, (short)56, (byte)1, (byte)34, get((byte)34, back.get((byte)34) ? "§a" : "§c"), getDesc((byte)34)),
                new APGuiItem(Material.MONSTER_EGG, (short)62, (byte)1, (byte)43, get((byte)43, back.get((byte)43) ? "§a" : "§c"), getDesc((byte)43)),
                new APGuiItem(Material.POTION, getData((byte)28), (byte)1, (byte)28, get((byte)28, back.get((byte)28) ? "§a" : "§c"), getDesc((byte)28)),
                new APGuiItem(Material.POTION, getData((byte)29), (byte)1, (byte)29, get((byte)29, back.get((byte)29) ? "§a" : "§c"), getDesc((byte)29)),
                new APGuiItem(Material.POTION, getData((byte)30), (byte)1, (byte)30, get((byte)30, back.get((byte)30) ? "§a" : "§c"), getDesc((byte)30)),
                new APGuiItem(Material.POTION, getData((byte)31), (byte)1, (byte)31, get((byte)31, back.get((byte)31) ? "§a" : "§c"), getDesc((byte)31)),
                new APGuiItem(Material.POTION, getData((byte)32), (byte)1, (byte)32, get((byte)32, back.get((byte)32) ? "§a" : "§c"), getDesc((byte)32)),
                new APGuiItem(Material.POTION, getData((byte)37), (byte)1, (byte)37, get((byte)37, back.get((byte)37) ? "§a" : "§c"), getDesc((byte)37)),
                new APGuiItem(Material.POTION, getData((byte)38), (byte)1, (byte)38, get((byte)38, back.get((byte)38) ? "§a" : "§c"), getDesc((byte)38)),
                new APGuiItem(Material.POTION, getData((byte)39), (byte)1, (byte)39, get((byte)39, back.get((byte)39) ? "§a" : "§c"), getDesc((byte)39)),
                new APGuiItem(Material.POTION, getData((byte)40), (byte)1, (byte)40, get((byte)40, back.get((byte)40) ? "§a" : "§c"), getDesc((byte)40)),
                new APGuiItem(Material.POTION, getData((byte)41), (byte)1, (byte)41, get((byte)41, back.get((byte)41) ? "§a" : "§c"), getDesc((byte)41)),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)21);
        for(byte i = 10; i <= 43; i ++){
            if(back.containsKey(i)) {
                if(this.gui.getItem(i).getType() == Material.POTION){
                    if(back.get(i)){
                        this.gui.getItem(i).setData(getData(i));
                    } else {
                        this.gui.getItem(i).setData((short)0);
                    }
                } else {
                    this.gui.getItem(i).setGlow(back.get(i));
                }
                this.gui.update(i);
            }
        }
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 10:
                settings.setNether(!settings.isNether());
                break;
            case 11:
                settings.setNetherPortalProtection(!settings.isNetherPortalProtection());
                break;
            case 12:
                return;
            case 13:
                settings.setFlintSteelNether(!settings.isFlintSteelNether());
                break;
            case 14:
                settings.setLavaBucketNether(!settings.isLavaBucketNether());
                break;
            case 16:
                settings.setMob(EntityType.PIG_ZOMBIE);
                break;
            case 25:
                settings.setMob(EntityType.BLAZE);
                break;
            case 34:
                settings.setMob(EntityType.GHAST);
                break;
            case 43:
                settings.setMob(EntityType.MAGMA_CUBE);
                break;
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
                if(e.isClickConfigure()){
                    try {
                        gui.getChildren(e.getSlot()).open(e.getPlayer());
                        if(back.get(e.getSlot()))return;
                    } catch(APGuiChildrenNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                settings.setPotion(getPotionType(e.getSlot()), !settings.isPotion(getPotionType(e.getSlot())));
                break;
        }
        back.replace(e.getSlot(), !back.get(e.getSlot()));
        gui.getItem(e.getSlot()).setName(get(e.getSlot(), (back.get(e.getSlot()) ? "§a" : "§c")));
        gui.getItem(e.getSlot()).setDescription(Collections.singletonList(getDesc(e.getSlot())));
        gui.getItem(e.getSlot()).setGlow(back.get(e.getSlot()));
        if(this.gui.getItem(e.getSlot()).getType() == Material.POTION){
            if(back.get(e.getSlot())){
                this.gui.getItem(e.getSlot()).setData(getData(e.getSlot()));
            } else {
                this.gui.getItem(e.getSlot()).setData((short)0);
            }
        }
        gui.update(e.getSlot());
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private String get(byte slot, String color){
        switch(slot){
            case 10: return color + "Nether";
            case 11: return color + "Protection portail";
            case 12: return color + "Nerf XP Quartz";
            case 13: return color + "Briquet (Nether)";
            case 14: return color + "Seau de lave (Nether)";
            case 16: return color + "Cochon-Zombie";
            case 25: return color + "Blaze";
            case 34: return color + "Ghast";
            case 43: return color + "Cube de Magma";
            case 28: return color + "Potion de Vitesse";
            case 29: return color + "Potion de Force";
            case 30: return color + "Potion d'Invisibilité";
            case 31: return color + "Potion de Résistance au feu";
            case 32: return color + "Potion de Régénération";
            case 37: return color + "Potion de Soin";
            case 38: return color + "Potion de Lenteur";
            case 39: return color + "Potion de Poison";
            case 40: return color + "Potion de Faiblesse";
            case 41: return color + "Potion de Dégât";
        }
        return "";
    }

    private String[] getDesc(byte slot){
        if(back.containsKey(slot)){
            if(back.get(slot)) {
                switch(slot) {
                    case 28:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.SPEED) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.SPEED) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.SPEED) ? "§aActivé" : "§cDésactivé")};
                    case 29:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.STRENGTH) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.STRENGTH) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.STRENGTH) ? "§aActivé" : "§cDésactivé")};
                    case 30:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.INVISIBILITY) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.INVISIBILITY) ? "§aActivé" : "§cDésactivé")};
                    case 31:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.FIRE_RESISTANCE) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.FIRE_RESISTANCE) ? "§aActivé" : "§cDésactivé")};
                    case 32:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.REGEN) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.REGEN) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.REGEN) ? "§aActivé" : "§cDésactivé")};
                    case 37:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.INSTANT_HEAL) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.INSTANT_HEAL) ? "§aActivé" : "§cDésactivé")};
                    case 38:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.SLOWNESS) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.SLOWNESS) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.SLOWNESS) ? "§aActivé" : "§cDésactivé")};
                    case 39:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.POISON) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.POISON) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.POISON) ? "§aActivé" : "§cDésactivé")};
                    case 40:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.WEAKNESS) ? "§aActivé" : "§cDésactivé"),
                                "§6Allongée: §e" + (settings.isPotionDuration(PotionType.WEAKNESS) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.WEAKNESS) ? "§aActivé" : "§cDésactivé")};
                    case 41:
                        return new String[]{"", "§6État: §aActivé", "",
                                "§6Level II: §e" + (settings.isPotionLevel(PotionType.INSTANT_DAMAGE) ? "§aActivé" : "§cDésactivé"),
                                "§6Jetable: §e" + (settings.isPotionSplash(PotionType.INSTANT_DAMAGE) ? "§aActivé" : "§cDésactivé")};
                }
            }
            return new String[]{"", "§6État: " + getState(slot)};
        }
        return new String[]{};
    }

    private String getState(byte slot){
        return (back.get(slot) ? "§aActivé" : "§cDésactivé");
    }

    private short getData(byte slot){
        switch(slot){
            case 28: return (short)8194;
            case 29: return (short)8201;
            case 30: return (short)8206;
            case 31: return (short)8195;
            case 32: return (short)8193;
            case 37: return (short)8197;
            case 38: return (short)8202;
            case 39: return (short)8196;
            case 40: return (short)8200;
            case 41: return (short)8204;
        }
        return -1;
    }

    private PotionType getPotionType(byte slot){
        switch(slot){
            case 28: return PotionType.SPEED;
            case 29: return PotionType.STRENGTH;
            case 30: return PotionType.INVISIBILITY;
            case 31: return PotionType.FIRE_RESISTANCE;
            case 32: return PotionType.REGEN;
            case 37: return PotionType.INSTANT_HEAL;
            case 38: return PotionType.SLOWNESS;
            case 39: return PotionType.POISON;
            case 40: return PotionType.WEAKNESS;
            case 41: return PotionType.INSTANT_DAMAGE;
        }
        return PotionType.WATER;
    }

}
