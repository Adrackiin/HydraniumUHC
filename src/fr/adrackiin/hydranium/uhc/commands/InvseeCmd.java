package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.GameMode;

public class InvseeCmd implements APCommandListener {

    private final APCommand command;

    public InvseeCmd(){
        this.command = new APCommand(
                "invsee",
                new String[]{},
                "Voir l'inventaire d'un joueur",
                new String[]{"<joueur>"},
                "hydranium.uhc.command.invsee"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 1){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(!APICore.getPlugin().doesAPPlayerExists(args[0])){
            return "Ce joueur ne fait pas parti de l'UHC";
        }
        UHCPlayer target = Game.getGame().getUHCPlayer(args[0]);
        UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
        pl.setGameMode(GameMode.SPECTATOR);
        player.openInventoryPlayer(target);
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
