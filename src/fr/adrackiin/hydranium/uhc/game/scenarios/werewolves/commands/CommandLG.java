package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.commands;

import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.WereWolves;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLG implements CommandExecutor {

    public CommandLG(){
        UHCCore.getPlugin().getCommand("lg").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            commandLG(Game.getGame().getUHCPlayer(((Player)sender).getUniqueId()), args);
        }
        return true;
    }

    private void commandLG(UHCPlayer sender, String[] args) {
        if(sender.isDead() && !sender.isHost()){
            sender.getAPPlayer().performCommand("help");
            return;
        }
        if(args.length == 0){
            sender.getAPPlayer().sendMessage(Prefix.wereWolves + "§c" + "Veuillez insérer une sous commande");
            return;
        }
        switch(args[0]){
            case "vote":
                WereWolves.getWereWolves().getVoteManager().commandVote(WereWolves.getWereWolves().getWWPlayer(sender.getAPPlayer().getUUID()), args);
                break;
            case "modele":
                if(!WereWolves.getWereWolves().isDiscovered()){
                    sender.getAPPlayer().sendMessage(Prefix.wereWolves + "§c" + "Les rôles n'ont pas encore été annoncé");
                    return;
                }
                WereWolves.getWereWolves().getRoleManager().getWildChildren().choose(WereWolves.getWereWolves().getWWPlayer(sender.getAPPlayer().getUUID()), args);
                break;
            case "murder":
                if(!WereWolves.getWereWolves().isDiscovered()){
                    sender.getAPPlayer().sendMessage(Prefix.wereWolves + "§c" + "Les rôles n'ont pas encore été annoncé");
                    return;
                }
                WereWolves.getWereWolves().getRoleManager().getChaman().seeMurder(WereWolves.getWereWolves().getWWPlayer(sender.getAPPlayer().getUUID()), args);
                break;
        }
    }
}
