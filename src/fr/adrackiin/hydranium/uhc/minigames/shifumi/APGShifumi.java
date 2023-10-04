package fr.adrackiin.hydranium.uhc.minigames.shifumi;

import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.Material;

public class APGShifumi extends APGuiListener {

    private final APGui gui;

    public APGShifumi(){
        this.gui = new APGui("§cShifumi", (byte)1, new APGuiItem[]{
                new APGuiItem(Material.COBBLESTONE, (short)0, (byte)1, (byte)2, "Pierre", new String[]{"", "§7La §cPierre §7gagne contre les §cCiseaux", "§7La §cPierre §7perd contre la §cFeuille"}),
                new APGuiItem(Material.PAPER, (short)0, (byte)1, (byte)4, "Feuille", new String[]{"", "§7La §cFeuille §7gagne contre la §cPierre", "§7La §cFeuille §7perd contre les §cCiseaux"}),
                new APGuiItem(Material.SHEARS, (short)0, (byte)1, (byte)6, "Ciseaux", new String[]{"", "§7Les §cCiseaux §7gagnent contre la §cFeuille", "§7Les §cCiseaux §7perdent contre la §cPierre"})
        }, null, true, false);
    }

    @Override
    public void onClick(APGuiClickEvent e){
        Shifumi.Object choice;
        switch(e.getSlot()){
            case 2:
                choice = Shifumi.Object.STONE;
                break;
            case 4:
                choice = Shifumi.Object.PAPER;
                break;
            case 6:
                choice = Shifumi.Object.CISSORS;
                break;
            default:
                return;
        }
        Game.getGame().getMiniGameManager().getShifumi().getSFMPlayer(e.getPlayer().getUUID()).setChoice(choice);
        e.getPlayer().closeInventory();
    }

    @Override
    public APGui getAPGui(){
        return this.gui;
    }
}
