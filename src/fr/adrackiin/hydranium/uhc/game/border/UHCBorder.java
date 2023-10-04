package fr.adrackiin.hydranium.uhc.game.border;

import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class UHCBorder {

    private WorldBorder worldBorder;

    public UHCBorder(World world){
        setWorldBorder(world);
    }

    public void setSize(){
        worldBorder.setSize(Game.getGame().getSettings().getSize()*2);
    }

    public void setWorldBorder(World world){
        worldBorder = world.getWorldBorder();
        worldBorder.setCenter(0.0D, 0.0D);
        worldBorder.setSize(Game.getGame().getSettings().getSize()*2);
        worldBorder.setWarningDistance(10);
        worldBorder.setWarningTime(20);
    }

    public enum Type {

        BORDER_TP,
        BORDERMOVE

    }

}
