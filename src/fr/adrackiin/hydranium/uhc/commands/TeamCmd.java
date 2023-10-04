package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.ChannelNotFoundException;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.team.GameTeam;
import fr.adrackiin.hydranium.uhc.utils.enumeration.Prefix;

public class TeamCmd implements APCommandListener {

    private final APCommand command;

    public TeamCmd(){
        this.command = new APCommand(
                "team",
                new String[]{},
                "Gestion de team",
                new String[]{"create - Créer une équipe", "leave - Quitter l'équipe", "invite <joueur> - Inviter un joueur", "accept <joueur> - Accepter une invitation"},
                "hydranium.uhc.command.team"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length < 1 || args.length > 2){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "create":
                return 1;
            case "invite":
                return 2;
            case "accept":
                return 3;
            case "leave":
                return 4;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(args.length == 2){
            if(!APICore.getPlugin().doesAPPlayerExists(args[1])){
                return "Ce joueur n'est pas connecté";
            }
        }
        UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
        switch(syntax){
            case 1:
                if(player.hasTeam()){
                    return "§7Vous êtes déjà dans une équipe";
                }
                GameTeam gameTeam = Game.getGame().getTeamManager().getFirstTeam();
                if(gameTeam == null){
                    try {
                        APICore.getPlugin().getChannelManager().getChannel("channel.host").sendMessage("§cPlus aucune équipes disponible, merci de prévenir un développeur / administrateur; s'il y a plus de 150 joueurs, merci d'annuler l'UHC ou de baisser les slots");
                    } catch(ChannelNotFoundException e){
                        e.printStackTrace();
                        return "§7Erreur interne";
                    }
                    return "§7Aucune équipes disponible, merci de prévenir un host";
                }
                player.joinTeam(gameTeam);
                break;
            case 2:
                if(!player.hasTeam()){
                    return "§7Vous n'êtes pas dans une équipe";
                }
                if(player.getGameTeam().getLeader() != player.getAPPlayer().getUUID()){
                    return "§7Vous n'êtes pas capitaine de l'équipe";
                }
                if(player.getGameTeam().getMembers().getSize() >= Game.getGame().getSettings().getTeam()){
                    return "§7Votre équipe est complète";
                }
                UHCPlayer target = Game.getGame().getUHCPlayer(args[1]);
                if(target.isHost() && !UHCCore.getPlugin().isDebug()){
                    return "§7C'est vrai que ce serait plus simple de gagner avec un host mais ... nop !";
                }
                if(target.hasTeam()){
                    return "§7Ce joueur est déjà dans une équipe";
                }
                if(target.isInvitedInTeam(player.getGameTeam())){
                    return "§7Ce joueur a déjà été invité dans votre team";
                }
                target.getInvitedInTeams().add(player.getGameTeam());
                target.getAPPlayer().sendMessage(new APText(Prefix.uhc + "§7" + pl.getName() + " vous a invité dans son équipe. §a/team accept " + pl.getName() + "§7 pour accepter")
                        .showText("§aAccepter")
                        .runCommand("/team accept " + pl.getName()));
                pl.sendMessage(Prefix.uhc + "§7" + "Vous avez invité " + target.getAPPlayer().getName() + " dans votre équipe");
                break;
            case 3:
                if(player.hasTeam()){
                    return "§7Vous êtes déjà dans une équipe";
                }
                UHCPlayer inviter = Game.getGame().getUHCPlayer(args[1]);
                if(!inviter.hasTeam()){
                    return "§7Ce joueur n'est pas dans une équipe";
                }
                if(!player.isInvitedInTeam(inviter.getGameTeam())){
                    return "§7Vous n'avez pas été invité dans cette équipe";
                }
                if(inviter.getGameTeam().getMembers().getSize() >= Game.getGame().getSettings().getTeam()){
                    return "§7Cette équipe est complète";
                }
                gameTeam = inviter.getGameTeam();
                gameTeam.sendMessageTeam("§a" + pl.getName() + " a rejoint votre Team");
                player.getInvitedInTeams().clear();
                player.joinTeam(gameTeam);
                break;
            case 4:
                if(player.getGameTeam() == null){
                    return "§7Vous n'êtes pas dans une Team";
                }
                player.leaveTeam();
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}