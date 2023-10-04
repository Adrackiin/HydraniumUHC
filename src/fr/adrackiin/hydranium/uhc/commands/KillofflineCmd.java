package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;

public class KillofflineCmd implements APCommandListener {

    private final APCommand command;

    public KillofflineCmd(){
        this.command = new APCommand(
                "killoffline",
                new String[]{},
                "Tuer un joueur déconnecté",
                new String[]{"<joueur>"},
                "hydranium.uhc.command.killoffline"
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
        if(target.getAPPlayer().isConnected()) {
            return  "Ce joueur est connecté";
        }
        if(!target.isPlaying()){
            return "Ce joueur n'est pas en vie";
        }
        target.offlineKill();
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
