package fr.adrackiin.hydranium.uhc.guis;

import fr.adrackiin.api.AdrackiinAPI;
import fr.adrackiin.api.api.event.APlayerPermissionEvent;
import fr.adrackiin.api.api.gui.*;
import fr.adrackiin.api.api.player.APlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class AGPlayer extends AGListener implements Listener {
    
    public AGPlayer(){
        super(null, UHCPlayer.class);
        addGui(
                new AGui(
                        this,
                        "default-inventory",
                        (byte)4,
                        new AGuiItem("§cHost", (byte)4, Material.REDSTONE_COMPARATOR, null).setGlowing().setPermission("hydranium.uhc.gui.host"),
                        new AGuiItem("§cOptions UHC", (byte)0, Material.TRAPPED_CHEST, null).setPermission("hydranium.uhc.minigame.shifumi")));
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }
    
    @EventHandler
    public void onPermission(APlayerPermissionEvent e){
        if(!e.getPlayer().isConnected()){
            return;
        }
        if(!e.getPermission().equals("hydranium.uhc.gui.host")){
            return;
        }
        this.removeGui(e.getPlayer());
        this.open(e.getPlayer(), e.getPlayer());
    }
    
    @Override
    public void onCreate(AGCreateEvent e){
        List<Byte> slotRemove = new ArrayList<>();
        for(AGuiItem item : this.getGui().getItems()){
            if(item.getPermission() != null && !((APlayer)e.getKeys()[0]).hasPermission(item.getPermission())){
                slotRemove.add(item.getSlot());
            }
        }
        for(byte b : slotRemove){
            this.getGui(e.getKeys()[0]).removeItem(b);
        }
    }
    
    @Override
    public void onOpen(AGOpenEvent e){
        if(Game.getGame().hasGameStart()){
            return;
        }
        e.getPlayer().clearInventory();
        PlayerInventory inv = e.getPlayer().getPlayer().getInventory();
        for(AGuiItem item : this.getGui(e.getPlayer()).getItems()){
            inv.setItem(item.getSlot(), item.getItem());
        }
    }
    
    @Override
    public void onClose(AGCloseEvent agCloseEvent){
    
    }
    
    @Override
    public void onClick(AGClickEvent e){
        switch(e.getSlot()){
            case 4:
                AdrackiinAPI.getPlugin().getGuiManager().getRootGui("host-menu").open(e.getPlayer());
                break;
            case 0:
                AdrackiinAPI.getPlugin().getGuiManager().getRootGui("options-uhc").open(e.getPlayer());
                break;
        }
    }
}
