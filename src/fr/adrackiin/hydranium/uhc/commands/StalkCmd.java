package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class StalkCmd implements APCommandListener {

    private final APCommand command;

    public StalkCmd(){
        this.command = new APCommand(
                "stalk",
                new String[]{},
                "Voir les joueurs autour d'un joueur",
                new String[]{"<joueur>"},
                "hydranium.uhc.command.stalk"
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
        if(!APICore.getPlugin().doesAPPlayerExists(args[0]) || Bukkit.getPlayer(args[0]) == null){
            return "Ce joueur n'est pas connecté";
        }
        UHCPlayer target = Game.getGame().getUHCPlayer(args[0]);
        List<Player> stalker = target.checkStalk(100);
        StringBuilder stringBuilder = new StringBuilder();
        for (Player p : stalker) {
            stringBuilder.append(p.getName()).append(", ");
        }
        pl.sendMessage(Prefix.uhc + "§7" + "Joueurs proches de " + "§c" + target.getAPPlayer().getName() + ": " + (stalker.isEmpty() ? "§c" + "Aucun joueur à proximité" : "§a" + stringBuilder.substring(0, stringBuilder.toString().length() - 2)));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
