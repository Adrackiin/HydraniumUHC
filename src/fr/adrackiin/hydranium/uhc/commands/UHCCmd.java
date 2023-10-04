package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.events.GameStateChangeEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.state.GameState;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.uhc.utils.pluginmessage.PluginMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UHCCmd implements APCommandListener {

    private final APCommand command;

    public UHCCmd(){
        this.command = new APCommand(
                "uhc",
                new String[]{},
                "Gestion de l'UHC",
                new String[]{"validate - Valider et prégénérer l'UHC", "open - Ouvrir l'UHC", "close - Fermer l'UHC", "start - Lancer l'UHC", "chat - Mute / unmute le tchat", "stop - Stopper l'UHC"},
                "hydranium.uhc.command.uhc"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 1){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "validate":
                return 1;
            case "open":
                return 2;
            case "close":
                return 3;
            case "start":
                return 4;
            case "mute":
                return 5;
            case "stop":
                return 6;
            case "forcepvp":
                return 7;
            case "forceborder":
                return 8;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        switch(syntax){
            case 1:
                if(Game.getGame().getGameStateManager().isGameState(GameState.CONFIG)){
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.PREGEN));
                } else {
                    return "§7L'UHC a déjà été validé";
                }
                break;
            case 2:
                if(Game.getGame().getGameStateManager().isGameState(GameState.WAITING_WHITELIST) || Game.getGame().getGameStateManager().isGameState(GameState.CLOSE)) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.OPEN));
                } else {
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.OPEN.getId()) {
                        return "§7Pré-Whitelist non-fermée";
                    } else {
                        return "§7UHC déjà ouvert";
                    }
                }
                break;
            case 3:
                if(Game.getGame().getGameStateManager().isGameState(GameState.OPEN)) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.CLOSE));
                } else {
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.OPEN.getId()) {
                        return "§7UHC non-ouvert";
                    } else {
                        return "§7UHC déjà fermé";
                    }
                }
                break;
            case 4:
                if(Game.getGame().getGameStateManager().isGameState(GameState.CLOSE)) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.START));
                } else {
                    if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.START.getId()) {
                        return "§7UHC non-fermé";
                    } else {
                        return "§7UHC non-lancé";
                    }
                }
                break;
            case 5:
                Game.getGame().setTchat(Game.getGame().isTchat());
                APICore.getPlugin().getPlayerManager().sendMessageAll(Game.getGame().isTchat() ? "§7Le tchat a été §alibéré" : "§7Le tchat a été §cemprisonné");
                break;
            case 6:
                if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.VICTORY.getId()){
                    return "§7L'UHC n'est pas fini";
                }
                for(Player p : Bukkit.getOnlinePlayers()){
                    PluginMessageManager.connect(p.getUniqueId());
                }
                Bukkit.shutdown();
                break;
            case 7:
                if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                    Game.getGame().getTimer().forcePvp();
                } else {
                    pl.sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                }
                break;
            case 8:
                if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                    Game.getGame().getTimer().forceBorder();
                } else {
                    pl.sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                }
                break;
        }
        return null;
    }
                /*case "preopen":
                    if(Game.getGame().getGameStateManager().isGameState(GameState.WAITING_WHITELIST)) {
                        Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.WHITELIST));
                    } else {
                        if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.WAITING_WHITELIST.getId()) {
                            sender.getAPPlayer().sendMessage(Prefix.uhc + "§c" + "L'UHC n'a pas été validé");
                        } else {
                            sender.getAPPlayer().sendMessage(Prefix.uhc + "§c" + "La Pré-Whitelist a déjà été ouverte");
                        }
                    }
                    break;
                case "preclose":
                    if(Game.getGame().getGameStateManager().isGameState(GameState.WHITELIST)) {
                        Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(GameState.CLOSE_WHITELIST));
                    } else {
                        if(Game.getGame().getGameStateManager().getGameState().getId() < GameState.CLOSE_WHITELIST.getId()) {
                            sender.getAPPlayer().sendMessage(Prefix.uhc + "§c" + "La Pré-Whitelist n'a pas été ouverte");
                        } else {
                            sender.getAPPlayer().sendMessage(Prefix.uhc + "§c" + "La Pré-Whitelist a déjà été fermée");
                        }
                    }
                    break;
                case "forcefh":
                    if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                        Game.getGame().getTimer().forceFinalHeal();
                    } else {
                        sender.getAPPlayer().sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                    }
                    break;
                case "forcepvp":
                    if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                        Game.getGame().getTimer().forcePvp();
                    } else {
                        sender.getAPPlayer().sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                    }
                    break;
                case "forceborder":
                    if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                        Game.getGame().getTimer().forceBorder();
                    } else {
                        sender.getAPPlayer().sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                    }
                    break;
                case "forcereduc":
                    if(Game.getGame().getGameStateManager().getGameState().getId() > GameState.TELEPORATTION.getId()){
                        Game.getGame().getBorderManager().forceReduc();
                    } else {
                        sender.getAPPlayer().sendMessage(Prefix.uhc + "§7" + "L'UHC n'a pas encore démarré");
                    }
                    break;*/

    @Override
    public APCommand getCommand(){
        return command;
    }
}
