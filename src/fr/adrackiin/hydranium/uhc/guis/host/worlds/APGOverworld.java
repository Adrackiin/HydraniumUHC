package fr.adrackiin.hydranium.uhc.guis.host.worlds;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.gameworld.GameWorld;
import fr.adrackiin.hydranium.uhc.game.scenarios.FlatLand;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import org.bukkit.*;

public class APGOverworld extends APGuiListener {

    private APGui gui;

    public APGOverworld() {
        APGui parent;
        try {
            parent = APICore.getPlugin().getAPGuiManager().get("§cGénération").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.gui = new APGui("§cOverworld", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.STONE, (short)0, (byte)1, (byte)10, "§cMonde normal", null),
                new APGuiItem(Material.WOOD_DOOR, (short)0, (byte)1, (byte)15, "§cFlatLand - Villages", new String[]{"", "§7Générer un monde plat", "§7rempli de §cvillages"}),
                new APGuiItem(Material.WEB, (short)0, (byte)1, (byte)16, "§cFlatLand - Mineshaft", new String[]{"", "§7Générer un monde plat", "§7rempli de §cmines"})
        }, parent, true, true);
        parent.addChildren(this.gui, (byte)11);
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        Scenario sc = Game.getGame().getScenariosManager().getScenario(FlatLand.class);
        switch(e.getSlot()){
            case 10:
                APICore.getPlugin().getAPGuiManager().closeAll(e.getPlayer());
                Game.getGame().setGameWorldType(GameWorld.Type.NORMAL);
                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> Game.getGame().getWorldUHC().newWorld(new WorldCreator("uhc")
                        .environment(World.Environment.NORMAL)
                        .type(WorldType.NORMAL)), 1L);
                if(sc.isActive()){
                    sc.setScenario(false);
                    Game.getGame().getScenarios().remove(sc);
                    PubSub.removeScenario(sc);
                }
                break;
            case 15:
                APICore.getPlugin().getAPGuiManager().closeAll(e.getPlayer());
                Game.getGame().setGameWorldType(GameWorld.Type.FLAT_VILLAGE);
                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> Game.getGame().getWorldUHC().newWorld(new WorldCreator("uhc")
                    .environment(World.Environment.NORMAL)
                    .type(WorldType.FLAT)
                    .generatorSettings("3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;village(size=3 distance=9)")), 1L);
                if(!sc.isActive()){
                    sc.setScenario(true);
                    Game.getGame().getScenarios().add(sc);
                    PubSub.addScenario(sc);
                }
                break;
            case 16:
                APICore.getPlugin().getAPGuiManager().closeAll(e.getPlayer());
                Game.getGame().setGameWorldType(GameWorld.Type.FLAT_MINESHAFT);
                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> Game.getGame().getWorldUHC().newWorld(new WorldCreator("uhc")
                        .environment(World.Environment.NORMAL)
                        .type(WorldType.FLAT)
                        .generatorSettings("3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;mineshaft(chance=0.3)")), 1L);
                break;
        }
        APGuiListener temp;
        try {
            temp = APICore.getPlugin().getAPGuiManager().get("§cOptions de Génération");
        } catch(APGuiNotFoundException e1) {
            e1.printStackTrace();
            return;
        }
        if(Game.getGame().getCaveOre().isActive()){
            if(Game.getGame().getGameWorldType() != GameWorld.Type.NORMAL){
                temp.onClick(new APGuiClickEvent(this.getAPGui().getItem((byte)10).getName(), (byte)10, e.getPlayer(), false));
            }
        }
        if(Game.getGame().getLapis().isActive()){
            if(Game.getGame().getGameWorldType() != GameWorld.Type.FLAT_VILLAGE){
                temp.onClick(new APGuiClickEvent(this.getAPGui().getItem((byte)16).getName(), (byte)16, e.getPlayer(), false));
            }
        }
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }
}
