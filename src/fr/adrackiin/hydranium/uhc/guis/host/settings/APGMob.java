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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class APGMob extends APGuiListener {

    private APGui gui;
    private final Settings settings = Game.getGame().getSettings();
    private final HashMap<Byte, Boolean> back = new HashMap<>();

    public APGMob() {
        back.put((byte)10, !settings.isMobForbid(get((byte)10)));
        back.put((byte)11, !settings.isMobForbid(get((byte)11)));
        back.put((byte)12, !settings.isMobForbid(get((byte)12)));
        back.put((byte)19, !settings.isMobForbid(get((byte)19)));
        back.put((byte)20, !settings.isMobForbid(get((byte)20)));
        back.put((byte)21, !settings.isMobForbid(get((byte)21)));
        back.put((byte)28, !settings.isMobForbid(get((byte)28)));
        back.put((byte)29, !settings.isMobForbid(get((byte)29)));
        back.put((byte)30, !settings.isMobForbid(get((byte)30)));
        back.put((byte)37, !settings.isMobForbid(get((byte)37)));
        back.put((byte)16, !settings.isMobForbid(get((byte)16)));
        back.put((byte)15, !settings.isMobForbid(get((byte)15)));
        back.put((byte)14, !settings.isMobForbid(get((byte)14)));
        back.put((byte)25, !settings.isMobForbid(get((byte)25)));
        back.put((byte)24, !settings.isMobForbid(get((byte)24)));
        back.put((byte)23, !settings.isMobForbid(get((byte)23)));
        back.put((byte)34, !settings.isMobForbid(get((byte)34)));
        back.put((byte)33, !settings.isMobForbid(get((byte)33)));
        back.put((byte)32, !settings.isMobForbid(get((byte)32)));
        back.put((byte)43, !settings.isMobForbid(get((byte)43)));
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cOptions").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cMobs", (byte)6, new APGuiItem[]{
                new APGuiItem(Material.MONSTER_EGG, (short)60, (byte)1, (byte)10, get((byte)10, back.get((byte)10) ? "§a" : "§c"), getDesc((byte)10)),
                new APGuiItem(Material.MONSTER_EGG, (short)55, (byte)1, (byte)11, get((byte)11, back.get((byte)11) ? "§a" : "§c"), getDesc((byte)11)),
                new APGuiItem(Material.MONSTER_EGG, (short)67, (byte)1, (byte)12, get((byte)12, back.get((byte)12) ? "§a" : "§c"), getDesc((byte)12)),
                new APGuiItem(Material.MONSTER_EGG, (short)66, (byte)1, (byte)19, get((byte)19, back.get((byte)19) ? "§a" : "§c"), getDesc((byte)19)),
                new APGuiItem(Material.MONSTER_EGG, (short)59, (byte)1, (byte)20, get((byte)20, back.get((byte)20) ? "§a" : "§c"), getDesc((byte)20)),
                new APGuiItem(Material.MONSTER_EGG, (short)52, (byte)1, (byte)21, get((byte)21, back.get((byte)21) ? "§a" : "§c"), getDesc((byte)21)),
                new APGuiItem(Material.MONSTER_EGG, (short)58, (byte)1, (byte)28, get((byte)28, back.get((byte)28) ? "§a" : "§c"), getDesc((byte)28)),
                new APGuiItem(Material.MONSTER_EGG, (short)50, (byte)1, (byte)29, get((byte)29, back.get((byte)29) ? "§a" : "§c"), getDesc((byte)29)),
                new APGuiItem(Material.MONSTER_EGG, (short)39, (byte)1, (byte)30, get((byte)30, back.get((byte)30) ? "§a" : "§c"), getDesc((byte)30)),
                new APGuiItem(Material.MONSTER_EGG, (short)54, (byte)1, (byte)37, get((byte)37, back.get((byte)37) ? "§a" : "§c"), getDesc((byte)37)),
                new APGuiItem(Material.MONSTER_EGG, (short)94, (byte)1, (byte)16, get((byte)16, back.get((byte)16) ? "§a" : "§c"), getDesc((byte)16)),
                new APGuiItem(Material.MONSTER_EGG, (short)95, (byte)1, (byte)15, get((byte)15, back.get((byte)15) ? "§a" : "§c"), getDesc((byte)15)),
                new APGuiItem(Material.MONSTER_EGG, (short)120, (byte)1, (byte)14, get((byte)14, back.get((byte)14) ? "§a" : "§c"), getDesc((byte)14)),
                new APGuiItem(Material.MONSTER_EGG, (short)65, (byte)1, (byte)25, get((byte)25, back.get((byte)25) ? "§a" : "§c"), getDesc((byte)25)),
                new APGuiItem(Material.MONSTER_EGG, (short)93, (byte)1, (byte)24, get((byte)24, back.get((byte)24) ? "§a" : "§c"), getDesc((byte)24)),
                new APGuiItem(Material.MONSTER_EGG, (short)101, (byte)1, (byte)23, get((byte)23, back.get((byte)23) ? "§a" : "§c"), getDesc((byte)23)),
                new APGuiItem(Material.MONSTER_EGG, (short)92, (byte)1, (byte)34, get((byte)34, back.get((byte)34) ? "§a" : "§c"), getDesc((byte)34)),
                new APGuiItem(Material.MONSTER_EGG, (short)90, (byte)1, (byte)33, get((byte)33, back.get((byte)33) ? "§a" : "§c"), getDesc((byte)33)),
                new APGuiItem(Material.MONSTER_EGG, (short)91, (byte)1, (byte)32, get((byte)32, back.get((byte)32) ? "§a" : "§c"), getDesc((byte)32)),
                new APGuiItem(Material.MONSTER_EGG, (short)100, (byte)1, (byte)43, get((byte)43, back.get((byte)43) ? "§a" : "§c"), getDesc((byte)43)),
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)15);
        for(byte i = 10; i <= 43; i += 1){
            if(back.containsKey(i)) {
                this.gui.getItem(i).setGlow(back.get(i));
                this.gui.update(i);
            }
        }
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        if(e.isClickConfigure()){
            switch(e.getSlot()){
                case 43:
                    try {
                        gui.getChildren(e.getSlot()).open(e.getPlayer());
                        if(back.get(e.getSlot()))return;
                    } catch(APGuiChildrenNotFoundException e1) {
                        e1.printStackTrace();
                    }

            }
        }
        settings.setMob(get(e.getSlot()));
        back.replace(e.getSlot(), !back.get(e.getSlot()));
        gui.getItem(e.getSlot()).setName(get(e.getSlot(), (back.get(e.getSlot()) ? "§a" : "§c")));
        gui.getItem(e.getSlot()).setDescription(Collections.singletonList(getDesc(e.getSlot())));
        gui.getItem(e.getSlot()).setGlow(back.get(e.getSlot()));
        gui.update(e.getSlot());
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    private EntityType get(byte slot){
        switch(slot){
            case 10: return EntityType.SILVERFISH;
            case 11: return EntityType.SLIME;
            case 12: return EntityType.ENDERMITE;
            case 19: return EntityType.WITCH;
            case 20: return EntityType.CAVE_SPIDER;
            case 21: return EntityType.SPIDER;
            case 28: return EntityType.ENDERMAN;
            case 29: return EntityType.CREEPER;
            case 30: return EntityType.SKELETON;
            case 37: return EntityType.ZOMBIE;
            case 16: return EntityType.SQUID;
            case 15: return EntityType.WOLF;
            case 14: return EntityType.VILLAGER;
            case 25: return EntityType.BAT;
            case 24: return EntityType.CHICKEN;
            case 23: return EntityType.RABBIT;
            case 34: return EntityType.COW;
            case 33: return EntityType.PIG;
            case 32: return EntityType.SHEEP;
            case 43: return EntityType.HORSE;
        }
        return null;
    }

    private String get(byte slot, String color){
        switch(slot){
            case 10: return color + "Silverfish";
            case 11: return color + "Slime";
            case 12: return color + "Endermite";
            case 19: return color + "Sorcière";
            case 20: return color + "Araignée bleue";
            case 21: return color + "Araignée";
            case 28: return color + "Enderman";
            case 29: return color + "Creeper";
            case 30: return color + "Squelette";
            case 37: return color + "Zombie";
            case 16: return color + "Poulpe";
            case 15: return color + "Loup";
            case 14: return color + "Villageois";
            case 25: return color + "Chauve-souris";
            case 24: return color + "Poulet";
            case 23: return color + "Lapin";
            case 34: return color + "Vache";
            case 33: return color + "Cochon";
            case 32: return color + "Mouton";
            case 43: return color + "Cheval";
        }
        return "";
    }

    private String[] getDesc(byte slot){
        if(slot == (byte)43){
            if(back.get(slot))return new String[]{"", "§6Spawn: " + getState(slot), "", "§6Selle: " + (settings.isHorseSaddle() ? "§aActivée" : "§cDésactivée"), "§6Armures: " + (settings.isHorseArmor() ? "§aActivées" : "§cDésactivées"), "§6Heal: " + (settings.isHorseHeal() ? "§aActivé" : "§cDésactivé")};
        }
        return new String[]{"", "§6Spawn: " + getState(slot)};
    }

    private String getState(byte slot){
        return (back.get(slot) ? "§aActivé" : "§cDésactivé");
    }

}
