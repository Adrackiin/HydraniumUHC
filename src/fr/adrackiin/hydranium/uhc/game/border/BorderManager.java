package fr.adrackiin.hydranium.uhc.game.border;

import fr.adrackiin.hydranium.uhc.game.Game;
import org.bukkit.event.Listener;

public class BorderManager implements Listener {

    private final TeleportingBorder teleportingBorder;
    private final MovingBorder movingBorder;

    public BorderManager(){
        teleportingBorder = new TeleportingBorder();
        movingBorder = new MovingBorder();
    }

    public void init(){
        switch(Game.getGame().getSettings().getBorderType()){
            case BORDER_TP:
                teleportingBorder.initBorder();
                break;
            case BORDERMOVE:
                movingBorder.initBorderMove();
                break;
        }
    }

    public void start(){
        switch(Game.getGame().getSettings().getBorderType()){
            case BORDER_TP:
                teleportingBorder.startBorder();
                break;
            case BORDERMOVE:
                movingBorder.startBorder();
                break;
        }
    }

    public void stop(){
        switch(Game.getGame().getSettings().getBorderType()){
            case BORDER_TP:
                teleportingBorder.stop();
                break;
            case BORDERMOVE:
                movingBorder.stop();
                break;
        }
    }

    public void reset(){
        stop();
        Game.getGame().getWorldUHC().getWorld().getWorldBorder().setSize(Game.getGame().getSettings().getSize() * 2);
    }
}
