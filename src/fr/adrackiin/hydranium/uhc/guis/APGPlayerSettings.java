package fr.adrackiin.hydranium.uhc.guis;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.api.utils.APHash;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.UUID;

public class APGPlayerSettings extends APGuiListener implements APGMultiplePlayer {

    private final APGui gui;
    private final APHash<UUID, APGui> guis = new APHash<>();
//options-uhc
    public APGPlayerSettings(){
        this.gui = new APGui("§cOptions UHC", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.SHEARS, (short)0, (byte)1, (byte)13, "§cShifumi", new String[]{"", "§7Tapez un joueur pour", "§7lancer un shifumi", "", "§6État: §cDésactivé"}),
        },null, true, false);
    }

    @Override
    public void onClick(APGuiClickEvent e){
        switch(e.getSlot()){
            case 13:
                boolean shifumi = (boolean) e.getPlayer().getSettings().get("shifumi");
                e.getPlayer().getSettings().set("shifumi", !shifumi);
                APGui gui = this.getAPGui(e.getPlayer());
                gui.getItem((byte)13).setDescription(shifumi ? Arrays.asList("", "§7Tapez un joueur pour", "§7lancer un shifumi", "", "§6État: §cDésactivé") : Arrays.asList("", "§7Tapez un joueur pour", "§7lancer un shifumi", "", "§6État: §aActivé"));
                gui.getItem((byte)13).setName((shifumi ? "§c" : "§a") + "Shifumi");
                gui.update((byte)13);
        }
    }

    @Override
    public APGui getAPGui(APPlayer player){
        return guis.get(player.getUUID());
    }

    @Override
    public void create(APPlayer apPlayer){

    }

    @Override
    public APGui getAPGui(){
        return gui;
    }

    @Override
    public APHash<UUID, APGui> getAPGuis(){
        return guis;
    }
}
