package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TpCmd implements APCommandListener {

    private final APCommand command;

    public TpCmd(){
        this.command = new APCommand(
                "teleport",
                new String[]{"tp", "rtp"},
                "Se téléporter",
                new String[]{"- Téléportation random", "<joueur> - Se téléporter à un joueur", "<x> <y> <z> - Se téléporter aux coordonnées", "<joueur à tp> <joueur> - Téléporter un joueur à un joueur"},
                "hydranium.uhc.command.teleport"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length > 3){
            return -1;
        }
        return args.length + 1;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        UHCPlayer target;
        switch(syntax){
            case 1:
                if(Game.getGame().getAlivePlayers().isEmpty()){
                    return "§cAucun joueur en vie";
                }
                pl.teleport(Bukkit.getPlayer(Game.getGame().getAlivePlayers().get(UHCCore.getRandom().nextInt(Game.getGame().getAlivePlayers().getSize()))).getLocation());
                break;
            case 2:
                if(!Game.getGame().isPlayerExists(args[0])){
                    return "§7Ce joueur n'a pas été trouvé";
                }
                target = Game.getGame().getUHCPlayer(args[0]);
                if(target.isPlaying() && target.getAPPlayer().isConnected()){
                    pl.teleport(target.getAPPlayer().getLocation());
                } else {
                    if(target.getOfflineLocation() == null){
                        if(target.getAPPlayer().isConnected()){
                            pl.teleport(target.getAPPlayer().getLocation());
                        } else {
                            return "§7Impossible de se téléporter à ce joueur";
                        }
                    } else {
                        pl.teleport(target.getOfflineLocation());
                    }
                }
                break;
            case 3:
                if(!Game.getGame().isPlayerExists(args[0])){
                    return "Ce joueur n'a pas été trouvé";
                }
                if(!Game.getGame().isPlayerExists(args[1])){
                    return "Ce joueur n'a pas été trouvé";
                }
                target = Game.getGame().getUHCPlayer(args[0]);
                UHCPlayer toPlayer = Game.getGame().getUHCPlayer(args[1]);
                if(!target.isPlaying() || !target.getAPPlayer().isConnected()){
                    target.getAPPlayer().teleport(toPlayer.getOfflineLocation());
                } else {
                    target.getAPPlayer().teleport(toPlayer.getAPPlayer().getLocation());
                }
                break;
            case 4:
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                pl.teleport(new Location(pl.getLocation().getWorld(), x, y, z));
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
