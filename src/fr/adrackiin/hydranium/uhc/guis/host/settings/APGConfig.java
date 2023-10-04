package fr.adrackiin.hydranium.uhc.guis.host.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Material;

public class APGConfig extends APGuiListener {

    private APGui gui;

    public APGConfig() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cOptions").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Settings settings = Game.getGame().getSettings();
        this.gui = new APGui("§cConfiguration", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.SKULL_ITEM, (byte)3, (byte)1, (byte)10, "§cJoueurs", new String[]{"",
                        "§6Team: §e" + (settings.getTeam() == 1 ? "FFA" : "To" + settings.getTeam()),
                        "§6Slot: §e" + settings.getMaxPlayers()}),
                new APGuiItem(Material.WATCH, (byte)0, (byte)1, (byte)11, "§cTemps", new String[]{"",
                        "§6Invulnérabilité: §e" + settings.getInvulnerability() + "§7 secondes",
                        "§6Final Heal: §e" + settings.getFinalHeal() + "§7 minutes",
                        "§6PvP: §e" + settings.getPvp() + "§7 minutes",
                        "§6Bordure: §e" + settings.getBorder() + "§7 minutes",
                        "§6Temps de réduction: §e" + settings.getMeetup() + "§7 minutes"}),
                new APGuiItem(Material.BARRIER, (byte)0, (byte)1, (byte)13, "§cBordure", new String[]{"",
                        "§6Bordure: §e" + (settings.getBorderType() == UHCBorder.Type.BORDERMOVE ? "Mouvement" : "Téléportation"),
                        "§6Taille: §e" + settings.getSize() + "§7 blocs",
                        "§6Taille finale: §e" + settings.getFinalSize() + "§7 blocs"}),
                new APGuiItem(Material.APPLE, (byte)0, (byte)1, (byte)15, "§cDrop: Pomme", new String[]{"",
                        "§6Arbres: §e" + (settings.isAllTrees() ? "Tous les arbres" : "Chênes seulement"),
                        "§6Taux: §e" + settings.getAppleRate() + "%"}),
                new APGuiItem(Material.FLINT, (byte)0, (byte)1, (byte)16, "§cDrop: Silex", new String[]{"",
                        "§6Taux: §e" + settings.getFlintRate() + "%"})
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)11);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        try {
            this.gui.getChildren(e.getSlot()).open(e.getPlayer());
        } catch(APGuiChildrenNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }
}
