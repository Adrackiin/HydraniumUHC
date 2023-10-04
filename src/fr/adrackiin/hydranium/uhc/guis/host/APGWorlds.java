package fr.adrackiin.hydranium.uhc.guis.host;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.*;

public class APGWorlds extends APGuiListener {

    private APGui gui;

    public APGWorlds() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cHost").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cGénération", (byte)5, new APGuiItem[]{
                new APGuiItem(Material.GRASS, (short)0, (byte)1, (byte) 11, "§cOverworld", new String[]{"",
                        "§7Clic §cgauche §7pour se §ctéléporter", "§7Clic §cdroit §7pour §cgénérer"}),
                new APGuiItem(Material.NETHERRACK, (short)0, (byte)1, (byte) 13, "§cNether", new String[]{"",
                        "§7Clic §cgauche §7pour se §ctéléporter", "§7Clic §cdroit §7pour §cgénérer"}),
                new APGuiItem(Material.ENDER_STONE, (short)0, (byte)1, (byte) 15, "§cEnder", new String[]{"",
                        "§7Clic §cgauche §7pour se §ctéléporter", "§7Clic §cdroit §7pour §cgénérer"}),
                new APGuiItem(Material.TRIPWIRE_HOOK, (short)0, (byte)1, (byte)31, "§cOptions de génération", null)
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)21);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        switch(e.getSlot()){
            case 11:
                if(e.isClickConfigure()) {
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.PREGEN.getId()) {
                        try {
                            this.gui.getChildren(e.getSlot()).open(e.getPlayer());
                        } catch(APGuiChildrenNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        e.getPlayer().sendMessage(Prefix.uhc + "§cLes mondes ne peuvent plus être regénéré");
                    }
                } else
                    e.getPlayer().teleport(new Location(Game.getGame().getWorldUHC().getWorld(), Game.getGame().getWorldUHC().getX(), Game.getGame().getWorldUHC().getY(), Game.getGame().getWorldUHC().getZ()));
                break;
            case 13:
                if(e.isClickConfigure()){
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.PREGEN.getId()) {
                        APICore.getPlugin().getAPGuiManager().closeAll(e.getPlayer());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> Game.getGame().getWorldNether().newWorld(new WorldCreator("uhc_nether").type(WorldType.NORMAL).environment(World.Environment.NETHER)), 1L);
                    } else {
                        e.getPlayer().sendMessage(Prefix.uhc + "§cLes mondes ne peuvent plus être regénéré");
                    }
                } else
                    e.getPlayer().teleport(new Location(Game.getGame().getWorldNether().getWorld(), Game.getGame().getWorldNether().getX(), Game.getGame().getWorldNether().getY(), Game.getGame().getWorldNether().getZ()));
                break;
            case 15:
                if(e.isClickConfigure()){
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.PREGEN.getId()) {
                        APICore.getPlugin().getAPGuiManager().closeAll(e.getPlayer());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> Game.getGame().getWorldEnder().newWorld(new WorldCreator("uhc_the_end").type(WorldType.NORMAL).environment(World.Environment.THE_END)), 1L);
                    } else {
                        e.getPlayer().sendMessage(Prefix.uhc + "§cLes mondes ne peuvent plus être regénéré");
                    }
                } else
                    e.getPlayer().teleport(new Location(Game.getGame().getWorldEnder().getWorld(), Game.getGame().getWorldEnder().getX(), Game.getGame().getWorldEnder().getY(), Game.getGame().getWorldEnder().getZ()));
                break;
            case 31:
                if(Game.getGame().getGameStateManager().getGameState().getId() >= GameState.PREGEN.getId()) {
                    e.getPlayer().sendMessage("§cVous ne pouvez plus changer les mondes");
                    return;
                }
                try {
                    this.gui.getChildren(e.getSlot()).open(e.getPlayer());
                } catch(APGuiChildrenNotFoundException e1) {
                    e1.printStackTrace();
                }
        }
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

}
